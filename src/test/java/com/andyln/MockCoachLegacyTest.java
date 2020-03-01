package com.andyln;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MockCoachLegacyTest {

    private Object mock1 = mock(Object.class);
    private Object mock2 = mock(Object.class);
    private Object[] singleMock = {mock1};
    private Object[] twoMocks = {mock1, mock2};
    private Object[] threeMocksInCircleChain = {mock1, mock2, mock1};

    private MockCoachRunnable when1 = mock(MockCoachRunnable.class);
    private MockCoachRunnable when2 = mock(MockCoachRunnable.class);
    private MockCoachRunnable when3 = mock(MockCoachRunnable.class);
    private MockCoachRunnable[] singleWhen = {when1};
    private MockCoachRunnable[] twoWhens = {when1, when2};
    private MockCoachRunnable[] threeWhens = {when1, when2, when3};

    private MockCoachRunnable verify1 = mock(MockCoachRunnable.class);
    private MockCoachRunnable verify2 = mock(MockCoachRunnable.class);
    private MockCoachRunnable verify3 = mock(MockCoachRunnable.class);
    private MockCoachRunnable[] singleVerify = {verify1};
    private MockCoachRunnable[] twoVerifies = {verify1, verify2};
    private MockCoachRunnable[] threeVerifies = {verify1, verify2, verify3};

    enum MockEnum {
        SECOND
    }

    private MockCoachLegacy mockCoachLegacySingleMock = new MockCoachLegacy(singleMock, singleWhen, singleVerify);
    private MockCoachLegacy mockCoachLegacyTwoMocks = new MockCoachLegacy(twoMocks, twoWhens, twoVerifies);
    private MockCoachLegacy mockCoachLegacyThreeMocksInCircleChain = new MockCoachLegacy(threeMocksInCircleChain, threeWhens, threeVerifies);

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    class Constructor {

        @Test
        public void success() {
            new MockCoachLegacy(twoMocks, twoWhens, twoVerifies);
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() {
            new MockCoachLegacy(singleMock, singleWhen, singleVerify);
        }

        @Test
        public void whenMocksAreInCircleChain_ThenSuccess() {
            new MockCoachLegacy(threeMocksInCircleChain, threeWhens, threeVerifies);
        }

        @Test
        public void whenMocksContainsInteger_ThenSuccess() {
            Object[] validMocks = { mock1, 2};
            new MockCoachLegacy(validMocks, twoWhens, twoVerifies);
        }

        @Test
        public void whenMocksContainsCharacter_ThenSuccess() {
            Object[] validMocks = { mock1, 'B'};
            new MockCoachLegacy(validMocks, twoWhens, twoVerifies);
        }

        @Test
        public void whenMocksContainsString_ThenSuccess() {
            Object[] validMocks = { mock1, "Second Mock"};
            new MockCoachLegacy(validMocks, twoWhens, twoVerifies);
        }

        @Test
        public void whenMocksContainsEnum_ThenSuccess() {
            Object[] validMocks = { mock1, MockEnum.SECOND};
            new MockCoachLegacy(validMocks, twoWhens, twoVerifies);
        }

        @Test
        public void whenMocksIsNull_ThenThrowIllegalArgumentException() {
            String expectedMessage = "mocks/whens/verifies cannot be null!";

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoachLegacy(null, null, null)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        public void whenMocksLengthDoesNotEqualWhensLength_ThenThrowIllegalArgumentException() {
            String expectedMessage = "whens length does not match mocks length!";

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoachLegacy(twoMocks, singleWhen, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        public void whenMocksLengthDoesNotEqualVerifiesLength_ThenThrowIllegalArgumentException() {
            String expectedMessage = "verifies length does not match mocks length!";

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoachLegacy(twoMocks, twoWhens, singleVerify)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        public void whenMocksIsEmpty_ThenThrowIllegalArgumentException() {
            String expectedMessage = "mocks/whens/verifies cannot be empty!";
            Object[] emptyMocks = new Object[0];
            MockCoachRunnable[] emptyWhens = new MockCoachRunnable[0];
            MockCoachRunnable[] emptyVerifies = new MockCoachRunnable[0];

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoachLegacy(emptyMocks, emptyWhens, emptyVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        public void whenMocksContainsNull_ThenThrowIllegalArgumentException() {
            String expectedMessage = "mocks[1] cannot be null!";
            Object[] invalidMocks = { mock1, null};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoachLegacy(invalidMocks, twoWhens, twoVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

        @Test
        public void whenMocksInACyclicGraphThatIsNotACircleChain_ThenThrowIllegalArgumentException() {
            String expectedMessage = "mocks[2] cannot be the same as a previous mock in mocks!";
            Object[] invalidMocks = { mock1, mock2, mock2};

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MockCoachLegacy(invalidMocks, threeWhens, threeVerifies)
            );

            assertEquals(expectedMessage, actualException.getMessage());
        }

    }

    @Nested
    class WhenBefore {

        @Test
        public void success() throws Exception {
            mockCoachLegacyTwoMocks.whenBefore(mock2);

            verify(when1, times(1)).run();
            verify(when2, times(0)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.whenBefore(mock1);

            verify(when1, times(0)).run();
        }

        @Test
        public void whenWhenBeforeMiddleMockInCircleChainMocks_ThenSuccess() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.whenBefore(mock2);

            verify(when1, times(1)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

        @Test
        public void whenWhenBefore_CalledWithFirstMockInCircleChainMocks_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call whenBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use whenBeforeFirst() or whenBeforeLast()";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyThreeMocksInCircleChain.whenBefore(mock1); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

        @Test
        public void whenWhenBefore_CalledWithMockNotInMocks_ThenThrowIllegalIllegalArgumentException() throws Exception {
            String expectedMessage = "Cannot call whenBefore(Object mock) for mock not in mocks!";
            Object mockNotInMocks = mock(Object.class);

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> { mockCoachLegacyThreeMocksInCircleChain.whenBefore(mockNotInMocks); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

    }

    @Nested
    class WhenBeforeFirst {

        @Test
        public void success() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.whenBeforeFirst();

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
            verify(when3, times(0)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.whenBeforeFirst();

            verify(when1, times(0)).run();
        }

        @Test
        public void whenWhenBeforeFirst_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call whenBeforeFirst() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_FIRST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyTwoMocks.whenBeforeFirst(); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
        }

    }

    @Nested
    class WhenBeforeLast {

        @Test
        public void success() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.whenBeforeLast();

            verify(when1, times(1)).run();
            verify(when2, times(1)).run();
            verify(when3, times(0)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.whenBeforeLast();

            verify(when1, times(0)).run();
        }

        @Test
        public void whenWhenBeforeLast_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call whenBeforeLast() for mocks in a path graph! For mocks in a path graph, use whenBefore(INSERT_LAST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyTwoMocks.whenBeforeLast(); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(when1, times(0)).run();
            verify(when2, times(0)).run();
        }

    }

    @Nested
    class WhenEverything {

        @Test
        public void success() throws Exception {
            mockCoachLegacyTwoMocks.whenEverything();

            verify(when1, times(1)).run();
            verify(when2, times(1)).run();
        }

        @Test
        public void whenThreeMocksInCircleChain_Thensuccess() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.whenEverything();

            verify(when1, times(1)).run();
            verify(when2, times(1)).run();
            verify(when3, times(1)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.whenEverything();

            verify(when1, times(1)).run();
        }

    }

    @Nested
    class VerifyBefore {

        @Test
        public void success() throws Exception {
            mockCoachLegacyTwoMocks.verifyBefore(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.verifyBefore(mock1);

            verify(verify1, times(0)).run();
        }

        @Test
        public void whenVerifyBeforeMiddleMockInCircleChainMocks_ThenSuccess() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.verifyBefore(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        public void whenVerifyBefore_CalledWithFirstMockInCircleChainMocks_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyBefore(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyBeforeFirst() or verifyBeforeLast()";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyThreeMocksInCircleChain.verifyBefore(mock1); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        public void whenVerifyBefore_CalledWithMockNotInMocks_ThenThrowIllegalIllegalArgumentException() throws Exception {
            String expectedMessage = "Cannot call verifyBefore(Object mock) for mock not in mocks!";
            Object mockNotInMocks = mock(Object.class);

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> { mockCoachLegacyThreeMocksInCircleChain.verifyBefore(mockNotInMocks); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

    }

    @Nested
    class Verify {

        @Test
        public void success() throws Exception {
            mockCoachLegacyTwoMocks.verify(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.verify(mock1);

            verify(verify1, times(1)).run();
        }

        @Test
        public void whenVerifyMiddleMockInCircleChainMocks_ThenSuccess() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.verify(mock2);

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        public void whenVerify_CalledWithFirstMockInCircleChainMocks_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verify(Object mock) for first/last mock in a circle chain! For mocks in a circle chain, use verifyFirst() or verifyLast()";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyThreeMocksInCircleChain.verify(mock1); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        public void whenVerify_CalledWithMockNotInMocks_ThenThrowIllegalIllegalArgumentException() throws Exception {
            String expectedMessage = "Cannot call verify(Object mock) for mock not in mocks!";
            Object mockNotInMocks = mock(Object.class);

            IllegalArgumentException actualException = assertThrows(
                    IllegalArgumentException.class,
                    () -> { mockCoachLegacyThreeMocksInCircleChain.verify(mockNotInMocks); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

    }

    @Nested
    class VerifyBeforeFirst {

        @Test
        public void success() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.verifyBeforeFirst();

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.verifyBeforeFirst();

            verify(verify1, times(0)).run();
        }

        @Test
        public void whenVerifyBeforeFirst_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyBeforeFirst() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_FIRST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyTwoMocks.verifyBeforeFirst(); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

    }

    @Nested
    class VerifyBeforeLast {

        @Test
        public void success() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.verifyBeforeLast();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.verifyBeforeLast();

            verify(verify1, times(0)).run();
        }

        @Test
        public void whenVerifyBeforeLast_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyBeforeLast() for mocks in a path graph! For mocks in a path graph, use verifyBefore(INSERT_LAST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyTwoMocks.verifyBeforeLast(); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

    }

    @Nested
    class VerifyFirst {

        @Test
        public void success() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.verifyFirst();

            verify(verify1, times(1)).run();
            verify(verify2, times(0)).run();
            verify(verify3, times(0)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.verifyFirst();

            verify(verify1, times(1)).run();
        }

        @Test
        public void whenVerifyFirst_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyFirst() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_FIRST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyTwoMocks.verifyFirst(); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

    }

    @Nested
    class VerifyLast {

        @Test
        public void success() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.verifyLast();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(1)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.verifyLast();

            verify(verify1, times(1)).run();
        }

        @Test
        public void whenVerifyLast_CalledOnDirectedPathGraph_ThenThrowIllegalStateException() throws Exception {
            String expectedMessage = "Cannot call verifyLast() for mocks in a path graph! For mocks in a path graph, use verify(INSERT_LAST_MOCK_HERE)";

            IllegalStateException actualException = assertThrows(
                    IllegalStateException.class,
                    () -> { mockCoachLegacyTwoMocks.verifyLast(); }
            );

            assertEquals(expectedMessage, actualException.getMessage());

            verify(verify1, times(0)).run();
            verify(verify2, times(0)).run();
        }

    }

    @Nested
    class VerifyEverything {

        @Test
        public void success() throws Exception {
            mockCoachLegacyTwoMocks.verifyEverything();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
        }

        @Test
        public void whenThreeMocksInCircleChain_Thensuccess() throws Exception {
            mockCoachLegacyThreeMocksInCircleChain.verifyEverything();

            verify(verify1, times(1)).run();
            verify(verify2, times(1)).run();
            verify(verify3, times(1)).run();
        }

        @Test
        public void whenSingleMockInMocks_ThenSuccess() throws Exception {
            mockCoachLegacySingleMock.verifyEverything();

            verify(verify1, times(1)).run();
        }

    }

}