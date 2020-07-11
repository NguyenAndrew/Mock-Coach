package com.andyln.mockcoachmethodable;

import com.andyln.MockCoach;
import org.junit.jupiter.api.Test;

import static com.andyln.mockcoachmethodable.VerifyBeforeFirst.verifyBeforeFirst;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class VerifyBeforeFirstTest {

    private static MockCoach mockCoach = mock(MockCoach.class);

    @Test
    public void success() {
        verifyBeforeFirst().in(mockCoach);
        verify(mockCoach).verifyBeforeFirst();
    }
}