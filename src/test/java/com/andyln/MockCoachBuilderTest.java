package com.andyln;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MockCoachBuilderTest {

    /* START - Below is an example of how to generate mocks/whens/verifies
     * Needed to create one for the build_ReturnsMockCoach_success() test,
     * so thought it would be a good idea to show an example as well.
     *
     * Also shows how to create MockCoach, using either MockCoachBuilder or MockCoach
     * (MockCoachBuilder can use either vararg or array syntax. MockCoach constructor can only use arrays).
     *
     * MockCoach methodNameMockCoach = new MockCoachBuilder()
     *     .mock(
     *         exampleMock
     *     ).when(
     *         () -> {
     *             when(exampleMock.mock(any(Object.class))).thenReturn(exampleMock);
     *             when(exampleMock.when(any(MockCoachRunnable.class))).thenReturn(exampleMock);
     *             when(exampleMock.verify(any(MockCoachRunnable.class))).thenReturn(exampleMock);
     *         }
     *     ).verify(
     *         () -> {
     *             verify(exampleMock, times(1)).mock(any(Object.class));
     *             verify(exampleMock, times(1)).when(any(MockCoachRunnable.class));
     *             verify(exampleMock, times(1)).verify(any(MockCoachRunnable.class));
     *         }
     *     ).build();
     *
     * vs
     *
     * MockCoach methodNameMockCoach = new MockCoach(
     *     new Object[]{
     *         exampleMock
     *     },
     *     new MockCoachRunnable[]{
     *         () -> {
     *             when(exampleMock.mock(any(Object.class))).thenReturn(exampleMock);
     *             when(exampleMock.when(any(MockCoachRunnable.class))).thenReturn(exampleMock);
     *             when(exampleMock.verify(any(MockCoachRunnable.class))).thenReturn(exampleMock);
     *         }
     *     },
     *     new MockCoachRunnable[]{
     *         () -> {
     *             verify(exampleMock, times(1)).mock(any(Object.class));
     *             verify(exampleMock, times(1)).when(any(MockCoachRunnable.class));
     *             verify(exampleMock, times(1)).verify(any(MockCoachRunnable.class));
     *         }
     *     }
     * );
     *
     */
    private final MockCoachBuilder exampleMock = mock(MockCoachBuilder.class);

    private final Object[] mocks = { exampleMock };

    private final MockCoachRunnable[] whens = {
            () -> {
                when(exampleMock.mock(any(Object.class))).thenReturn(exampleMock);
                when(exampleMock.when(any(MockCoachRunnable.class))).thenReturn(exampleMock);
                when(exampleMock.verify(any(MockCoachRunnable.class))).thenReturn(exampleMock);
            }
    };

    private final MockCoachRunnable[] verifies = {
            () -> {
                verify(exampleMock, times(1)).mock(any(Object.class));
                verify(exampleMock, times(1)).when(any(MockCoachRunnable.class));
                verify(exampleMock, times(1)).verify(any(MockCoachRunnable.class));
            }
    };
    /* END - of example*/

    private final MockCoachBuilder mockCoachBuilder = new MockCoachBuilder();

    @Test
    void mock_ReturnsSameBuilder_success() {
        MockCoachBuilder mockCoachBuilderWithMocks = mockCoachBuilder.mock(mocks);
        assertEquals(mockCoachBuilder, mockCoachBuilderWithMocks);
    }

    @Test
    void when_ReturnsSameBuilder_success() {
        MockCoachBuilder mockCoachBuilderWithMocks = mockCoachBuilder.when(whens);
        assertEquals(mockCoachBuilder, mockCoachBuilderWithMocks);
    }

    @Test
    void verify_ReturnsSameBuilder_success() {
        MockCoachBuilder mockCoachBuilderWithMocks = mockCoachBuilder.verify(verifies);
        assertEquals(mockCoachBuilder, mockCoachBuilderWithMocks);
    }

    @Test
    void build_ReturnsMockCoach_success() {
        MockCoach mockCoach = new MockCoachBuilder()
                .mock(
                        exampleMock
                ).when(
                    () -> {
                        when(exampleMock.mock(any(Object.class))).thenReturn(exampleMock);
                        when(exampleMock.when(any(MockCoachRunnable.class))).thenReturn(exampleMock);
                        when(exampleMock.verify(any(MockCoachRunnable.class))).thenReturn(exampleMock);
                    }
                ).verify(
                    () -> {
                        verify(exampleMock, times(1)).mock(any(Object.class));
                        verify(exampleMock, times(1)).when(any(MockCoachRunnable.class));
                        verify(exampleMock, times(1)).verify(any(MockCoachRunnable.class));
                    }
                ).build();
        assertNotNull(mockCoach);
    }
}


