# Mock Coach

The Java library implementing the Mock Coach design pattern. Used in tests to reduce complex logic and boilerplate code in overall codebase.

Can reduce hours and days of writing unit tests into minutes! 

Useful for both new and previously existing codebases, Mock Coach accomplishes this feat by converting both old and new implicit testing strategies into explicit and reusable test strategies automated by a reusable coach!

This library provides two libraries to use:

1. Mock Coach (Default), library used to quickly write tests, and reduce complexities in business logic. Takes a "test improves business logic, and business logic improves test" approach.
2. Mock Coach Legacy (Backup), library used to write tests quickly (slightly slower and less automated than default approach), where code can't be refactored or is too costly to refactor. 
    
## Service Dipath Chain and Service Cyclic Graph

For testing methods and creating new business logic, Mock Coach (Default) helps encourage writing business logic as Service Dipath Chains, over Service Cyclic Graphs (SCG). 

Examples of Service Dipath Chains:

A -> B -> C

A -> B -> C -> D -> E

A -> B -> C -> A

Example of Service Cyclic Graphs:

A -> B -> C -> B -> D -> E

A -> B -> C -> D -> E -> F -> ... -> X -> B -> Y -> Z

A -> B -> C -> B -> ... -> B -> C

A -> B -> C -> D -> C -> B -> A

Service Dipath Chains are recommended in most cases over Service Cyclic Graphs, because it is faster to understand and work with code when services are used in order one-by-one compared to when service usage is intertwined (both in business logic and in tests).

If the method you are testing happens to be a Service Cyclic Graph, you can either **1. Refactor using sample suggestions** or **2. Use Mock Code Legacy (Backup)** Here are more detailed explanations of these two options:

1. Separate and/or move the service calls into multiple methods within a facade. Call the facade's methods within the current method to achieve same functionality. Sample suggestions: 
    1. A -> B -> C -> B -> D -> E can be converted to A -> N -> D -> E (where N internally calls B -> C).
    2. A -> B -> C -> D -> E -> F -> ... -> X -> B -> Y -> Z can be coverted to A -> N -> Y -> Z (where N internally calls B -> ... -> B)
    3. A -> B -> C -> B -> ... -> B -> C can be coverted to A -> N (where N internally calls B -> C)
    4. A -> B -> C -> D -> C -> B -> A can be converted in the following ways:
        1. A -> N -> A (where N internally calls B -> C, D -> C, B as separate method calls). Useful if you want to create stricter boundaries between service layers, at the cost of creating more methods.
        2. A -> N -> D -> C -> B -> A (where N internally calls B -> C -> D). Useful if you want to reduce amount of methods, at the cost of using services between multiple service layers.  
2. Perform your mocks/when/verifies with Mock Coach Legacy. Mock Coach Legacy is 100% compatible with testing on previously existing codebases, and is encouraged for smaller cyclic graphs where splitting the code can cause more confusion than not. Mock Coach Legacy creates an additional cost and overhead of managing the directed graph of mock usage (compared to the default Mock Coach handling that for you).

## How to Install

This library will eventually be added to Maven Central. In the mean time, follow these steps to use Mock Coach:

1. Create MockCoach and MockCoachBuilder classes within your test folder. Create MockCoachRunnable interface within your test folder.
2. Copy the class and interface code from this Git repository to the corresponding files created in Step 1.
3. Fix any compile errors

## Example Usages

### Creating MockCoach

![Mock Coach](docs/images/MockCoach.PNG)

```
private AdditionService additionService = mock(AdditionService.class);
private MultiplicationService multiplicationService = mock(MultiplicationService.class);
private SubtractionService subtractionService = mock(SubtractionService.class);
private SubMultiService subMultiService = mock(SubMultiService.class);

private Calculator calculator = new Calculator(additionService, multiplicationService, subtractionService, subMultiService);

MockCoach mockCoach = new MockCoachBuilder()
        .mock(
                additionService,
                multiplicationService,
                subtractionService,
                subMultiService
        ).when(
                (/* Addition Service */) -> {
                    when(additionService.add(anyInt(), anyInt())).thenReturn(SAMPLE_ADDITION_OUTPUT);
                },
                (/* Multiplication Service */) -> {
                    when(multiplicationService.multiply(anyInt(), anyInt())).thenReturn(SAMPLE_MULTIPLICATION_OUTPUT);
                },
                (/* Subtraction Service */) -> {
                    when(subtractionService.subtract(anyInt(), anyInt())).thenReturn(SAMPLE_SUBTRACTION_OUTPUT);
                },
                (/* Sub Multi Service */) -> {
                    when(subMultiService.subtractThenMultiplyBy2(anyInt(), anyInt())).thenReturn(SAMPLE_SUB_MULTI_OUTPUT);
                }
        ).verify(
                (/* Addition Service */) -> {
                    verify(additionService, times(1)).add(anyInt(), anyInt());
                },
                (/* Multiplication Service */) -> {
                    verify(multiplicationService, times(1)).multiply(anyInt(), anyInt());
                },
                (/* Subtraction Service */) -> {
                    verifyZeroInteractions(subtractionService);
                },
                (/* Sub Multi Service */) -> {
                    verify(subMultiService, times(1)).subtractThenMultiplyBy2(anyInt(), anyInt());
                }
        ).build();
```

### Unit Testing - Success Case
![Success Case](docs/images/SuccessCase.PNG)
```
    @Test
    public void givenAnInput_whenCalculatorCalculates_thenWeExpectAnOutput() throws Exception {
        // Given (Setup)
        int expected = SAMPLE_SUB_MULTI_OUTPUT;
        int x = 10;
        mockCoach.whenEverything();

        // When (Run the thing that you want to test)
        int y = calculator.calculateY(x);

        // Then (Asserting what you want to be true, is actually true)
        assertEquals(expected, y);

        // Verify
        mockCoach.verifyEverything();
    }
```

### Unit Testing - Exception and Other Cases
![Exception Case](docs/images/ExceptionCase.PNG)
```
    @Test
    public void givenAnInput_whenMultiplicationServiceThrowsAnException_thenCalculatorBubblesThatExceptionUp() throws Exception {
        // Given (Setup)
        int x = 10;
        mockCoach.whenBefore(multiplicationService);
        when(multiplicationService.multiply(anyInt(), anyInt())).thenThrow(new RuntimeException(SAMPLE_EXCEPTION_MESSAGE));

        try {
            // When (Run the thing that you want to test)
            calculator.calculateY(x);
            fail("Should have failed");
        } catch (Exception e) {
            // Then (Asserting what you want to be true, is actually true)
            assertEquals(SAMPLE_EXCEPTION_MESSAGE, e.getMessage());
        }

        // Verify
        mockCoach.verify(multiplicationService);
        verifyZeroInteractions(subtractionService);
    }
```
