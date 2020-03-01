package com.andyln;

import java.util.HashMap;
import java.util.Map;

public class MockCoach {
    private Object[] mocks;
    private MockCoachRunnable[] whens;
    private MockCoachRunnable[] verifies;

    private Map<Object, Integer> mockMap;

    private boolean containsMoreThanOneMock;

    /*
     * If true, mocks are in a circle chain
     * If false, mocks are in a path graph chain
     */
    private boolean isMocksInCircleChain;

    /**
     * Constructs a MockCoach. May have better experience constructing MockCoach using {@link MockCoachBuilder},
     * as it reduces boilerplate by using varargs instead of arrays.
     * @see com.andyln.MockCoachBuilder#MockCoachBuilder()
     * @param mocks These mocks are any object that are injected or autowired into an object under test.
     * @param whens Whens is an array of runnables, where each runnable may contain multiple when statements.
     * Example of a single when: {@code
     * () -> &#123;
     *     when(mock.method()).thenReturn(someValue);
     *     when(mock.anotherMethod()).thenReturn(anotherValue);
     * &#125;
     * }
     * @param verifies Verifies is an array of runnables, where each runnable may contain multiple verify statements.
     * Example of a single verify: {@code
     * () -> &#123;
     *     verify(mock, times(1)).method();
     *     verify(mock, times(1)).anotherMethod());
     * &#125;
     * }
     * @throws IllegalArgumentException Prevents calling constructor with any mocks/whens/verifies that are empty, not the same length, or not permitted type.
     */
    public MockCoach(Object[] mocks, MockCoachRunnable[] whens, MockCoachRunnable[] verifies) {
        if (mocks == null) {
            throw new IllegalArgumentException("mocks/whens/verifies cannot be null!");
        }

        if (mocks.length != whens.length) {
            throw new IllegalArgumentException("whens length does not match mocks length!");
        }

        if (mocks.length != verifies.length) {
            throw new IllegalArgumentException("verifies length does not match mocks length!");
        }

        if (mocks.length == 0) {
            throw new IllegalArgumentException("mocks/whens/verifies cannot be empty!");
        }

        mockMap = new HashMap<>();

        containsMoreThanOneMock = mocks.length > 1;

        if (!containsMoreThanOneMock) {
            // Only contains single mock in mocks
            mockMap.put(mocks[0], 0);
            this.mocks = mocks;
            this.whens = whens;
            this.verifies = verifies;
            return;
        }

        isMocksInCircleChain = mocks[0] == mocks[mocks.length-1];

        int lengthOfMocksToCheck = isMocksInCircleChain ? mocks.length - 1 : mocks.length;
        for (int i = 0; i < lengthOfMocksToCheck; i++) {
            if (mocks[i] == null) {
                throw new IllegalArgumentException(String.format("mocks[%d] cannot be null!", i));
            }

            if (mocks[i] instanceof Integer) {
                throw new IllegalArgumentException(String.format("mocks[%d] cannot be instance of Integer! Please use LegacyMockCoachBuilder and LegacyMockCoach for Integer support.", i));
            }

            if (mocks[i] instanceof Character) {
                throw new IllegalArgumentException(String.format("mocks[%d] cannot be instance of Character! Please use LegacyMockCoachBuilder and LegacyMockCoach for Character support.", i));
            }

            if (mocks[i] instanceof String) {
                throw new IllegalArgumentException(String.format("mocks[%d] cannot be instance of String! Please use LegacyMockCoachBuilder and LegacyMockCoach for String support.", i));
            }

            if (mocks[i] instanceof Enum<?>) {
                throw new IllegalArgumentException(String.format("mocks[%d] cannot be instance of Enum! Please use LegacyMockCoachBuilder and LegacyMockCoach for Enum support.", i));
            }


            Object potentiallyDuplicateMock = mockMap.put(mocks[i], i);
            boolean isDuplicateMock = potentiallyDuplicateMock != null;
            if (isDuplicateMock) {
                throw new IllegalArgumentException(String.format("mocks[%d] cannot be the same as a previous mock in mocks!", i));
            }
        }

        this.mocks = mocks;
        this.whens = whens;
        this.verifies = verifies;
    }

    /**
     * Runs all whens before, and not including, when corresponding to mock.
     * @param mock Any mock within mocks.
     * @throws IllegalStateException Calling this method for first/last mocks in circle chain
     * (because first and last mock are the same, there would no way to tell which mock to use).
     * For circle chains, call either whenBeforeFirst() or whenBeforeLast()
     * @throws IllegalArgumentException Calling with object not in mocks.
     * @throws Exception Resolves any exception that may be thrown within whens.
     */
    public void whenBefore(Object mock) throws Exception {
        if (containsMoreThanOneMock && isMocksInCircleChain && mock == mocks[0])
        {
            throw new IllegalStateException("Cannot call whenBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use whenBeforeFirst() or whenBeforeLast()");
        }

        Integer objectIndexOfMock = mockMap.get(mock);

        if (objectIndexOfMock == null) {
            throw new IllegalArgumentException("Cannot call whenBefore(Object mock) for mock not in mocks!");
        }

        int indexOfMock = objectIndexOfMock;

        for (int i = 0; i < indexOfMock; i++) {
            whens[i].run();
        }
    }

    /**
     * Placeholder for testing with mocks in circle chain. This method exists, because it creates a better user experience
     * when refactoring tests.
     * @throws IllegalStateException Calling this method for mocks in directed path chain
     * (using this method in directed path chain, may cause confusion on which mock is being referred to).
     * For directed path chains, call whenBefore(INSERT_FIRST_MOCK_HERE)
     * @throws Exception Does not actually throw exception, keeps consistency of exceptions thrown for other methods.
     */
    public void whenBeforeFirst() throws Exception {
        if (containsMoreThanOneMock && !isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call whenBeforeFirst() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_FIRST_MOCK_HERE)");
        }

        // Does nothing. Used as a placeholder.
    }

    /**
     * Runs all whens before, and not including, when corresponding to last mock in mocks.
     * @throws IllegalStateException Calling this method for mocks in directed path chain
     * (using this method in directed path chain, may cause confusion on which mock is being referred to).
     * For directed path chains, call whenBefore(INSERT_LAST_MOCK_HERE)
     * @throws Exception Resolves any exception that may be thrown within whens.
     */
    public void whenBeforeLast() throws Exception {
        if (containsMoreThanOneMock && !isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call whenBeforeLast() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_LAST_MOCK_HERE)");
        }

        int indexOfLastMock = this.mocks.length - 1;
        for (int i = 0; i < indexOfLastMock; i++) {
            whens[i].run();
        }
    }

    /**
     * Runs all whens.
     * @throws Exception Resolves any exception that may be thrown within whens.
     */
    public void whenEverything() throws Exception {
        for (int i = 0; i < this.mocks.length; i++) {
            whens[i].run();
        }
    }

    /**
     * Runs all verifies before, and not including, verify corresponding to mock.
     * @param mock Any mock within mocks.
     * @throws IllegalStateException Calling this method for first/last mocks in circle chain
     * (because first and last mock are the same, there would no way to tell which mock to use).
     * For circle chains, call either verifyBeforeFirst() or verifyBeforeLast()
     * @throws IllegalArgumentException Calling with object not in mocks.
     * @throws Exception Resolves any exception that may be thrown within verifies.
     */
    public void verifyBefore(Object mock) throws Exception {
        if (containsMoreThanOneMock && isMocksInCircleChain && mock == mocks[0])
        {
            throw new IllegalStateException("Cannot call verifyBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyBeforeFirst() or verifyBeforeLast()");
        }

        Integer objectIndexOfMock = mockMap.get(mock);

        if (objectIndexOfMock == null) {
            throw new IllegalArgumentException("Cannot call verifyBefore(Object mock) for mock not in mocks!");
        }

        int indexOfMock = objectIndexOfMock;

        for (int i = 0; i < indexOfMock; i++) {
            verifies[i].run();
        }
    }

    /**
     * Runs all verifies before, and including, verify corresponding to mock.
     * @param mock Any mock within mocks.
     * @throws IllegalStateException Calling this method for first/last mocks in circle chain
     * (because first and last mock are the same, there would no way to tell which mock to use).
     * For circle chains, call either verifyFirst() or verifyLast()
     * @throws IllegalArgumentException Calling with object not in mocks.
     * @throws Exception Resolves any exception that may be thrown within verifies.
     */
    public void verify(Object mock) throws Exception {
        if (containsMoreThanOneMock && isMocksInCircleChain && mock == mocks[0])
        {
            throw new IllegalStateException("Cannot call verify(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyFirst() or verifyLast()");
        }

        Integer objectIndexOfMock = mockMap.get(mock);

        if (objectIndexOfMock == null) {
            throw new IllegalArgumentException("Cannot call verify(Object mock) for mock not in mocks!");
        }

        int indexOfMock = objectIndexOfMock;

        for (int i = 0; i <= indexOfMock; i++) {
            verifies[i].run();
        }
    }

    /**
     * Placeholder for testing with mocks in circle chain. This method exists, because it creates a better user experience
     * when refactoring tests.
     * @throws IllegalStateException Calling this method for mocks in directed path chain
     * (using this method in directed path chain, may cause confusion on which mock is being referred to).
     * For directed path chains, call verifyBefore(INSERT_FIRST_MOCK_HERE)
     * @throws Exception Does not actually throw exception, keeps consistency of exceptions thrown for other methods.
     */
    public void verifyBeforeFirst() throws Exception {
        if (containsMoreThanOneMock && !isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyBeforeFirst() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_FIRST_MOCK_HERE)");
        }

        // Does nothing. Used as a placeholder.
    }

    /**
     * Runs all verifies before, and not including, verify corresponding to last mock in mocks.
     * @throws IllegalStateException Calling this method for mocks in directed path
     * (using this method in directed path chain, may cause confusion on which mock is being referred to).
     * For directed path chains, call verifyBefore(INSERT_LAST_MOCK_HERE)
     * @throws Exception Resolves any exception that may be thrown within verifies.
     */
    public void verifyBeforeLast() throws Exception {
        if (containsMoreThanOneMock && !isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyBeforeLast() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_LAST_MOCK_HERE)");
        }

        for (int i = 0; i < this.mocks.length - 1; i++) {
            verifies[i].run();
        }
    }

    /**
     * Placeholder for testing with mocks in circle chain. This method exists, because it creates a better user experience
     * when refactoring tests.
     * @throws IllegalStateException Calling this method for mocks in directed path chain
     * (using this method in directed path chain, may cause confusion on which mock is being referred to).
     * For directed path chains, call verify(INSERT_FIRST_MOCK_HERE)
     * @throws Exception Does not actually throw exception, keeps consistency of exceptions thrown for other methods.
     */
    public void verifyFirst() throws Exception {
        if (containsMoreThanOneMock && !isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyFirst() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_FIRST_MOCK_HERE)");
        }

        verifies[0].run();
    }

    /**
     * Runs all verifies. This method exists, because it creates a better user experience
     * when refactoring tests.
     * @throws IllegalStateException Calling this method for mocks in directed path chain
     * (using this method in directed path chain, may cause confusion on which mock is being referred to).
     * For directed path chains, call verify(INSERT_LAST_MOCK_HERE)
     * @throws Exception Resolves any exception that may be thrown within verifies.
     */
    public void verifyLast() throws Exception {
        if (containsMoreThanOneMock && !isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyLast() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_LAST_MOCK_HERE)");
        }

        this.verifyEverything();
    }

    /**
     * Runs all verifies.
     * @throws Exception Resolves any exception that may be thrown within verifies.
     */
    public void verifyEverything() throws Exception {
        for (int i = 0; i < this.mocks.length; i++) {
            verifies[i].run();
        }
    }
}
