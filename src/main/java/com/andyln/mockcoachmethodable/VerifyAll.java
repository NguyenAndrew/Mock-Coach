package com.andyln.mockcoachmethodable;

import com.andyln.MockCoach;

public class VerifyAll implements Methodable {

    public static VerifyAll verifyAll() {
        return new VerifyAll();
    }

    @Override
    public void in(MockCoach mockCoach) {
        mockCoach.verifyAll();
    }
}
