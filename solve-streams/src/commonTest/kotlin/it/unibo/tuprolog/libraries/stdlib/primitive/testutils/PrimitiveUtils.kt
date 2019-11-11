package it.unibo.tuprolog.libraries.stdlib.primitive.testutils

import it.unibo.tuprolog.solve.Solution
import it.unibo.tuprolog.solve.Solve
import it.unibo.tuprolog.solve.exception.PrologError
import it.unibo.tuprolog.solve.solver.ExecutionContextImpl
import it.unibo.tuprolog.solve.solver.fsm.impl.StateEnd
import it.unibo.tuprolog.solve.solver.fsm.impl.StateGoalEvaluation
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Utils singleton to help testing Primitive implementations
 *
 * @author Enrico
 */
internal object PrimitiveUtils {

    /** Utility function to assert that there's only one Solution of given type, with given query and substitution */
    internal fun assertOnlyOneSolution(expectedSolution: Solution, solutions: Sequence<Solve.Response>) {
        assertEquals(1, solutions.count(), "Expected only one solution, but ${solutions.toList()}")
        with(solutions.single().solution) {
            assertEquals(expectedSolution::class, this::class)
            assertEquals(expectedSolution.query, query)
            assertEquals(expectedSolution.substitution, substitution)
        }
    }

    /**
     * Utility function to test whether the cause of errors generated is correctly filled
     *
     * It passes request to StateGoalEvaluation, then it executes the primitive exercising the error situation;
     * in the end the generated solution's error chain is checked to match with [expectedErrorSolution]'s chain
     */
    internal fun assertErrorCauseChainComputedCorrectly(request: Solve.Request<ExecutionContextImpl>, expectedErrorSolution: Solution.Halt) {
        val nextState = StateGoalEvaluation(request).behave().toList().single()

        assertEquals(StateEnd.Halt::class, nextState::class)
        assertEquals(expectedErrorSolution.exception::class, (nextState as StateEnd.Halt).exception::class)

        var expectedCause = expectedErrorSolution.exception.cause
        var actualCause = nextState.exception.cause

        while (expectedCause != null) {
            val expectedCauseStruct = (expectedCause as? PrologError)?.errorStruct
            val actualCauseStruct = (actualCause as? PrologError)?.errorStruct

            assertNotNull(expectedCauseStruct)
            assertNotNull(actualCauseStruct)

            assertTrue("Expected `$expectedCauseStruct` not structurally equals to actual `$actualCauseStruct`") {
                expectedCauseStruct.structurallyEquals(actualCauseStruct)
            }

            expectedCause = expectedCause.cause
            actualCause = actualCause?.cause
        }
    }
}
