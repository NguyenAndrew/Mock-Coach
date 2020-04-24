package com.andyln;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MockCoachTest {

    private final Object mock1 = mock(Object.class);
    private final Object mock2 = mock(Object.class);
    private final Object mock3 = mock(Object.class);
    private final Object[] singleMock = {mock1};
    private final Object[] twoMocks = {mock1, mock2};
    private final Object[] threeMocksInCircleChain = {mock1, mock2, mock1};

    private final MockCoachRunnable when1 = mock(MockCoachRunnable.class);
    private final MockCoachRunnable when2 = mock(MockCoachRunnable.class);
    private final MockCoachRunnable when3 = mock(MockCoachRunnable.class);
    private final MockCoachRunnable[] singleWhen = {when1};
    private final MockCoachRunnable[] twoWhens = {when1, when2};
    private final MockCoachRunnable[] threeWhens = {when1, when2, when3};

    private final MockCoachRunnable verify1 = mock(MockCoachRunnable.class);
    private final MockCoachRunnable verify2 = mock(MockCoachRunnable.class);
    private final MockCoachRunnable verify3 = mock(MockCoachRunnable.class);
    private final MockCoachRunnable[] singleVerify = {verify1};
    private final MockCoachRunnable[] twoVerifies = {verify1, verify2};
    private final MockCoachRunnable[] threeVerifies = {verify1, verify2, verify3};

    enum MockEnum {
        SECOND
    }

    private final MockCoach mockCoachSingleMock = new MockCoach(singleMock, singleWhen, singleVerify);
    private final MockCoach mockCoachTwoMocks = new MockCoach(twoMocks, twoWhens, twoVerifies);
    private final MockCoach mockCoachThreeMocksInCircleChain = new MockCoach(threeMocksInCircleChain, threeWhens, threeVerifies);

    @Nested
    class ConstructorWithObjectsAndMockCoachRunnables {

        @Test
        void success() {
            new MockCoach(twoMocks, twoWhens, twoVerifies);
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() {
            new MockCoach(singleMock, singleWhen, singleVerify);
        }

        @Test
        void whenMocksAreInCircleChain_ThenSuccess() {
            new MockCoach(threeMocksInCircleChain, threeWhens, threeVerifies);
        }

        @Test
        void whenMocksIsNull_ThenThrowIllegalArgumentException() {
            String expectedMessage = "mocks/whens/verifies cannot be null!";

            Object[] mocks = null;
            MockCoachRunnable[] whens = null;
            MockCoachRunnable[] verifies = null;

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(mocks, whens, verifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksLengthDoesNotEqualWhensLength_ThenThrowIllegalArgumentException() {
            String expectedMessage = "whens length does not match mocks length!";

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(twoMocks, singleWhen, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksLengthDoesNotEqualVerifiesLength_ThenThrowIllegalArgumentException() {
            String expectedMessage = "verifies length does not match mocks length!";

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(twoMocks, twoWhens, singleVerify)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksIsEmpty_ThenThrowIllegalArgumentException() {
            String expectedMessage = "mocks/whens/verifies cannot be empty!";
            Object[] emptyMocks = new Object[0];
            MockCoachRunnable[] emptyWhens = new MockCoachRunnable[0];
            MockCoachRunnable[] emptyVerifies = new MockCoachRunnable[0];

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(emptyMocks, emptyWhens, emptyVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksContainsNull_ThenThrowIllegalArgumentException() {
            String expectedMessage = "m2 cannot be null!";
            Object[] invalidMocks = {mock1, null};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(invalidMocks, twoWhens, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksContainsInteger_ThenThrowIllegalArgumentException() {
            String expectedMessage = "m2 cannot be instance of Integer! Please use LegacyMockCoachBuilder and LegacyMockCoach for Integer support.";
            Object[] invalidMocks = {mock1, 2};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(invalidMocks, twoWhens, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksContainsCharacter_ThenThrowIllegalArgumentException() {
            String expectedMessage = "m2 cannot be instance of Character! Please use LegacyMockCoachBuilder and LegacyMockCoach for Character support.";
            Object[] invalidMocks = {mock1, 'B'};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(invalidMocks, twoWhens, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksContainsString_ThenThrowIllegalArgumentException() {
            String expectedMessage = "m2 cannot be instance of String! Please use LegacyMockCoachBuilder and LegacyMockCoach for String support.";
            Object[] invalidMocks = {mock1, "Second Mock"};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(invalidMocks, twoWhens, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksContainsEnum_ThenThrowIllegalArgumentException() {
            String expectedMessage = "m2 cannot be instance of Enum! Please use LegacyMockCoachBuilder and LegacyMockCoach for Enum support.";
            Object[] invalidMocks = {mock1, MockEnum.SECOND};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(invalidMocks, twoWhens, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        void whenMocksInACyclicGraphThatIsNotACircleChain_ThenThrowIllegalArgumentException() {
            String expectedMessage = "m3 cannot be the same as a previous mock in mocks!";
            Object[] invalidMocks = {mock1, mock2, mock2};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoach(invalidMocks, threeWhens, threeVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

    }

    @Nested
    class WhenBefore {

        @Test
        void success() throws Exception {
            mockCoachTwoMocks.whenBefore(mock2);

            verify(when1, times(1)).run();
            verify(when2, times(0)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.whenBefore(mock1);

            verify(when1, times(0)).run();
        }

        @Test
        void whenWhenBeforeMiddleMockInCircleChainMocks_ThenSuccess() throws Exception {
            mockCoachThreeMocksInCircleChain.whenBefore(mock2);

            verify(when1, times(1)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

        @Test
        void whenWhenBefore_CalledWithFirstMockInCircleChainMocks_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call whenBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use whenBeforeFirst() or whenBeforeLast()";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> mockCoachThreeMocksInCircleChain.whenBefore(mock1)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

        @Test
        void whenWhenBefore_CalledWithMockNotInMocks_ThenThrowIllegalIllegalArgumentException() throws Exception {
            String expectedMessage = "Cannot call whenBefore(Object mock) for mock not in mocks!";
            Object mockNotInMocks = mock(Object.class);

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> mockCoachThreeMocksInCircleChain.whenBefore(mockNotInMocks)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

        @Test
        void whenWhenBefore_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "w1 throws an exception! Please check your whens.";

            doThrow(new Exception()).when(when1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    () -> mockCoachThreeMocksInCircleChain.whenBefore(mock2)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(1)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

    }

    @Nested
    class WhenBeforeFirst {

        @Test
        void success() throws Exception {
            mockCoachThreeMocksInCircleChain.whenBeforeFirst();

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.whenBeforeFirst();

            verify(when1, times(0)).run();
        }

        @Test
        void whenWhenBeforeFirst_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call whenBeforeFirst() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_FIRST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    mockCoachTwoMocks::whenBeforeFirst
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
        }

    }

    @Nested
    class WhenBeforeLast {

        @Test
        void success() throws Exception {
            mockCoachThreeMocksInCircleChain.whenBeforeLast();

            verify(when1, times(1)).run();
            verify(when2, times(1)).run();
            verify(when3, times(0)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.whenBeforeLast();

            verify(when1, times(0)).run();
        }

        @Test
        void whenWhenBeforeLast_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call whenBeforeLast() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_LAST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    mockCoachTwoMocks::whenBeforeLast
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
        }

        @Test
        void whenWhenBeforeLast_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "w1 throws an exception! Please check your whens.";

            doThrow(new Exception()).when(when1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    mockCoachThreeMocksInCircleChain::whenBeforeLast
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(1)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

    }

    @Nested
    class WhenAll {

        @Test
        void success() throws Exception {
            mockCoachTwoMocks.whenAll();

            verify(when1, times(1)).run();
            verify(when2, times(1)).run();
        }

        @Test
        void whenThreeMocksInCircleChain_Thensuccess() throws Exception {
            mockCoachThreeMocksInCircleChain.whenAll();

            verify(when1, times(1)).run();
            verify(when2, times(1)).run();
            verify(when3, times(1)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.whenAll();

            verify(when1, times(1)).run();
        }

        @Test
        void whenWhenAll_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "w1 throws an exception! Please check your whens.";

            doThrow(new Exception()).when(when1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    mockCoachThreeMocksInCircleChain::whenAll
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(1)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

    }

    @Nested
    class VerifyBefore {

        @Test
        void success() throws Exception {
            mockCoachTwoMocks.verifyBefore(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.verifyBefore(mock1);

            verify(verify1, times(0)).run();
        }

        @Test
        void whenVerifyBeforeMiddleMockInCircleChainMocks_ThenSuccess() throws Exception {
            mockCoachThreeMocksInCircleChain.verifyBefore(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenVerifyBefore_CalledWithFirstMockInCircleChainMocks_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyBeforeFirst() or verifyBeforeLast()";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> mockCoachThreeMocksInCircleChain.verifyBefore(mock1)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenVerifyBefore_CalledWithMockNotInMocks_ThenThrowIllegalIllegalArgumentException() throws Exception {
            String expectedMessage = "Cannot call verifyBefore(Object mock) for mock not in mocks!";
            Object mockNotInMocks = mock(Object.class);

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> mockCoachThreeMocksInCircleChain.verifyBefore(mockNotInMocks)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenVerifyBefore_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "v1 throws an exception! Please check your verifies.";

            doThrow(new Exception()).when(verify1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    () -> mockCoachThreeMocksInCircleChain.verifyBefore(mock2)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

    }

    @Nested
    class Verify {

        @Test
        void success() throws Exception {
            mockCoachTwoMocks.verify(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.verify(mock1);

            verify(verify1, times(1)).run();
        }

        @Test
        void whenVerifyMiddleMockInCircleChainMocks_ThenSuccess() throws Exception {
            mockCoachThreeMocksInCircleChain.verify(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenVerify_CalledWithFirstMockInCircleChainMocks_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verify(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyFirst() or verifyLast()";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> mockCoachThreeMocksInCircleChain.verify(mock1)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenVerify_CalledWithMockNotInMocks_ThenThrowIllegalIllegalArgumentException() throws Exception {
            String expectedMessage = "Cannot call verify(Object mock) for mock not in mocks!";
            Object mockNotInMocks = mock(Object.class);

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> mockCoachThreeMocksInCircleChain.verify(mockNotInMocks)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenVerify_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "v1 throws an exception! Please check your verifies.";

            doThrow(new Exception()).when(verify1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    () -> mockCoachThreeMocksInCircleChain.verify(mock2)
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

    }

    @Nested
    class VerifyBeforeFirst {

        @Test
        void success() throws Exception {
            mockCoachThreeMocksInCircleChain.verifyBeforeFirst();

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.verifyBeforeFirst();

            verify(verify1, times(0)).run();
        }

        @Test
        void whenVerifyBeforeFirst_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyBeforeFirst() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_FIRST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    mockCoachTwoMocks::verifyBeforeFirst
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

    }

    @Nested
    class VerifyBeforeLast {

        @Test
        void success() throws Exception {
            mockCoachThreeMocksInCircleChain.verifyBeforeLast();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.verifyBeforeLast();

            verify(verify1, times(0)).run();
        }

        @Test
        void whenVerifyBeforeLast_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyBeforeLast() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_LAST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    mockCoachTwoMocks::verifyBeforeLast
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

        @Test
        void whenVerifyBeforeLast_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "v1 throws an exception! Please check your verifies.";

            doThrow(new Exception()).when(verify1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    mockCoachThreeMocksInCircleChain::verifyBeforeLast
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

    }

    @Nested
    class VerifyFirst {

        @Test
        void success() throws Exception {
            mockCoachThreeMocksInCircleChain.verifyFirst();

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.verifyFirst();

            verify(verify1, times(1)).run();
        }

        @Test
        void whenVerifyFirst_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyFirst() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_FIRST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    mockCoachTwoMocks::verifyFirst
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

        @Test
        void whenVerifyFirst_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "v1 throws an exception! Please check your verifies.";

            doThrow(new Exception()).when(verify1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    mockCoachThreeMocksInCircleChain::verifyFirst
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

    }

    @Nested
    class VerifyLast {

        @Test
        void success() throws Exception {
            mockCoachThreeMocksInCircleChain.verifyLast();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(1)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.verifyLast();

            verify(verify1, times(1)).run();
        }

        @Test
        void whenVerifyLast_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyLast() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_LAST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    mockCoachTwoMocks::verifyLast
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

        @Test
        void whenVerifyLast_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "v1 throws an exception! Please check your verifies.";

            doThrow(new Exception()).when(verify1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    mockCoachThreeMocksInCircleChain::verifyLast
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

    }

    @Nested
    class VerifyAll {

        @Test
        void success() throws Exception {
            mockCoachTwoMocks.verifyAll();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
        }

        @Test
        void whenThreeMocksInCircleChain_Thensuccess() throws Exception {
            mockCoachThreeMocksInCircleChain.verifyAll();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(1)).run();
        }

        @Test
        void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachSingleMock.verifyAll();

            verify(verify1, times(1)).run();
        }

        @Test
        void whenVerifyAll_CalledWithMockThatThrowsException_ThenThrowRuntimeException() throws Exception {
            String expectedMessage = "v1 throws an exception! Please check your verifies.";

            doThrow(new Exception()).when(verify1).run();

            RuntimeException actualException = assertThrows(
                    RuntimeException.class,
                    mockCoachThreeMocksInCircleChain::verifyAll
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }
    }

    @Nested
    public class DefaultEmptyConstructor {
        @Test
        void success() {
            new MockCoach();
        }
    }

    @Nested
    public class OverloadedConstructors {
        // List of Mocks
        Object m1 = mock(Object.class);
        Object m2 = mock(Object.class);
        Object m3 = mock(Object.class);
        Object m4 = mock(Object.class);
        Object m5 = mock(Object.class);
        Object m6 = mock(Object.class);
        Object m7 = mock(Object.class);
        Object m8 = mock(Object.class);
        Object m9 = mock(Object.class);
        Object m10 = mock(Object.class);
        Object m11 = mock(Object.class);
        Object m12 = mock(Object.class);
        Object m13 = mock(Object.class);
        Object m14 = mock(Object.class);
        Object m15 = mock(Object.class);
        Object m16 = mock(Object.class);

        @Test
        void OneMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void TwoMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void ThreeMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void FourMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void FiveMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void SixMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void SevenMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void EightMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void NineMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void TenMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    },
                    m10,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void ElevenMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    },
                    m10,
                    () -> {
                    },
                    () -> {
                    },
                    m11,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void TwelveMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    },
                    m10,
                    () -> {
                    },
                    () -> {
                    },
                    m11,
                    () -> {
                    },
                    () -> {
                    },
                    m12,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void ThirteenMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    },
                    m10,
                    () -> {
                    },
                    () -> {
                    },
                    m11,
                    () -> {
                    },
                    () -> {
                    },
                    m12,
                    () -> {
                    },
                    () -> {
                    },
                    m13,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void FourteenMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    },
                    m10,
                    () -> {
                    },
                    () -> {
                    },
                    m11,
                    () -> {
                    },
                    () -> {
                    },
                    m12,
                    () -> {
                    },
                    () -> {
                    },
                    m13,
                    () -> {
                    },
                    () -> {
                    },
                    m14,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void FifteenMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    },
                    m10,
                    () -> {
                    },
                    () -> {
                    },
                    m11,
                    () -> {
                    },
                    () -> {
                    },
                    m12,
                    () -> {
                    },
                    () -> {
                    },
                    m13,
                    () -> {
                    },
                    () -> {
                    },
                    m14,
                    () -> {
                    },
                    () -> {
                    },
                    m15,
                    () -> {
                    },
                    () -> {
                    }
            );
        }

        @Test
        void SixteenMock_Success() {
            new MockCoach(
                    m1,
                    () -> {
                    },
                    () -> {
                    },
                    m2,
                    () -> {
                    },
                    () -> {
                    },
                    m3,
                    () -> {
                    },
                    () -> {
                    },
                    m4,
                    () -> {
                    },
                    () -> {
                    },
                    m5,
                    () -> {
                    },
                    () -> {
                    },
                    m6,
                    () -> {
                    },
                    () -> {
                    },
                    m7,
                    () -> {
                    },
                    () -> {
                    },
                    m8,
                    () -> {
                    },
                    () -> {
                    },
                    m9,
                    () -> {
                    },
                    () -> {
                    },
                    m10,
                    () -> {
                    },
                    () -> {
                    },
                    m11,
                    () -> {
                    },
                    () -> {
                    },
                    m12,
                    () -> {
                    },
                    () -> {
                    },
                    m13,
                    () -> {
                    },
                    () -> {
                    },
                    m14,
                    () -> {
                    },
                    () -> {
                    },
                    m15,
                    () -> {
                    },
                    () -> {
                    },
                    m16,
                    () -> {
                    },
                    () -> {
                    }
            );
        }
    }
}