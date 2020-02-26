package com.andyln;

import java.util.HashSet;
import java.util.Set;

public class MockCoach {
    private Object[] mocks;
    private MockCoachRunnable[] whens;
    private MockCoachRunnable[] verifies;

    // If true, mocks are in a circle chain
    // If false, mocks are in a path graph chain
    private boolean isMocksInCircleChain;

    public MockCoach(Object[] mocks, MockCoachRunnable[] whens, MockCoachRunnable[] verifies) {
        if (mocks == null) {
            throw new IllegalArgumentException("mocks must not be null!");
        }

        if (mocks.length == 0) {
            throw new IllegalArgumentException("mocks must not be empty!");
        }

        if (mocks.length != whens.length) {
            throw new IllegalArgumentException("whens length does not match mocks length!");
        }

        if (mocks.length != verifies.length) {
            throw new IllegalArgumentException("verifies length does not match whens length!");
        }

        isMocksInCircleChain = mocks[0] == mocks[mocks.length-1];

        Set<Object> mockSet = new HashSet<>();
        int lengthOfMocksToCheck = isMocksInCircleChain ? mocks.length : mocks.length - 1;
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

            boolean isDuplicateMock = !mockSet.add(mocks[i]);
            if (isDuplicateMock) {
                throw new IllegalArgumentException(String.format("mocks[%d] cannot be the same as a previous mock in mocks!", i));
            }
        }

        this.mocks = mocks;
        this.whens = whens;
        this.verifies = verifies;
    }

    public void whenBefore(Object mock) throws Exception {
        if (isMocksInCircleChain && mock == mocks[0])
        {
            throw new IllegalStateException("Cannot call whenBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use whenBeforeFirst() or whenBeforeLast()");
        }

        for (int i = 0; i < this.mocks.length; i++) {
            if (this.mocks[i] == mock) {
                return;
            }
            whens[i].run();
        }
    }

    public void whenBeforeFirst() throws Exception {
        if (!isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call whenBeforeFirst() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_FIRST_MOCK_HERE)");
        }

        // Does nothing. Used as a placeholder.
    }

    public void whenBeforeLast() throws Exception {
        if (!isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call whenBeforeLast() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_LAST_MOCK_HERE)");
        }

        for (int i = 0; i < this.mocks.length - 1; i++) {
            whens[i].run();
        }
    }

    public void whenEverything() throws Exception {
        for (int i = 0; i < this.mocks.length; i++) {
            whens[i].run();
        }
    }

    public void verifyBefore(Object mock) throws Exception {
        if (isMocksInCircleChain && mock == mocks[0])
        {
            throw new IllegalStateException("Cannot call verifyBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyBeforeFirst() or verifyBeforeLast()");
        }

        for (int i = 0; i < this.mocks.length; i++) {
            if (this.mocks[i] == mock) {
                return;
            }
            verifies[i].run();
        }
    }

    public void verify(Object mock) throws Exception {
        if (isMocksInCircleChain && mock == mocks[0])
        {
            throw new IllegalStateException("Cannot call verify(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyFirst() or verifyLast()");
        }

        for (int i = 0; i < this.mocks.length; i++) {
            verifies[i].run();
            if (this.mocks[i] == mock) {
                return;
            }
        }
    }

    public void verifyBeforeFirst() throws Exception {
        if (!isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyBeforeFirst() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_FIRST_MOCK_HERE)");
        }

        // Does nothing. Used as a placeholder.
    }

    public void verifyBeforeLast() throws Exception {
        if (!isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyBeforeLast() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_LAST_MOCK_HERE)");
        }

        for (int i = 0; i < this.mocks.length - 1; i++) {
            verifies[i].run();
        }
    }

    public void verifyFirst() throws Exception {
        if (!isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyFirst() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_FIRST_MOCK_HERE)");
        }

        verifies[0].run();
    }

    public void verifyLast() throws Exception {
        if (!isMocksInCircleChain) {
            throw new IllegalStateException("Cannot call verifyLast() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_LAST_MOCK_HERE)");
        }

        this.verifyEverything();
    }

    public void verifyEverything() throws Exception {
        for (int i = 0; i < this.mocks.length; i++) {
            verifies[i].run();
        }
    }
}
