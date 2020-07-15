package com.andyln.mockcoachmethodable;

import com.andyln.MockCoach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static com.andyln.mockcoachmethodable.VerifyAll.verifyAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class VerifyAllTest {

    private static MockCoach mockCoach = mock(MockCoach.class);

    @Test
    public void success() {
        verifyAll().in(mockCoach);
        verify(mockCoach).verifyAll();
    }

    @Test
    public void forgotIn_ThenThrowRuntimeException() {
        RuntimeException placeholderException = new RuntimeException();
        verifyAll();
        String expectedMessage = String.format(
                "Missing .in(MockCoach) at com.andyln.mockcoachmethodable.VerifyAllTest.forgotIn_ThenThrowRuntimeException(VerifyAllTest.java:%d)",
                placeholderException.getStackTrace()[0].getLineNumber() + 1
        );

        RuntimeException actualException = assertThrows(
                RuntimeException.class,
                VerifyAll::verifyAll
        );

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void clearWorks_ThenSuccess() {
        verifyAll();
        assertThrows(
                RuntimeException.class,
                VerifyAll::verifyAll
        );
        verifyAll();
    }

    @AfterAll
    public static void cleanUp() {
        MethodableState.clear();
    }

}