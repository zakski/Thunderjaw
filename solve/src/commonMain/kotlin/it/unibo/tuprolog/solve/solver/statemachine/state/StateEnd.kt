package it.unibo.tuprolog.solve.solver.statemachine.state

import it.unibo.tuprolog.core.Substitution
import it.unibo.tuprolog.solve.Solve
import it.unibo.tuprolog.solve.solver.SolverStrategies
import kotlinx.coroutines.CoroutineScope

/**
 * Base class of states representing the computation end
 *
 * @author Enrico
 */
internal sealed class StateEnd(
        override val solveRequest: Solve.Request,
        override val executionStrategy: CoroutineScope,
        override val solverStrategies: SolverStrategies
) : AbstractState(solveRequest, executionStrategy, solverStrategies), FinalState {

    override fun behave(): Sequence<State> = emptySequence()

    internal data class True(
            override val solveRequest: Solve.Request,
            override val executionStrategy: CoroutineScope,
            override val solverStrategies: SolverStrategies,
            override val answerSubstitution: Substitution.Unifier = solveRequest.context.actualSubstitution
    ) : StateEnd(solveRequest, executionStrategy, solverStrategies), SuccessFinalState

    internal data class TrueWithChoicePoint(
            override val solveRequest: Solve.Request,
            override val executionStrategy: CoroutineScope,
            override val solverStrategies: SolverStrategies,
            override val answerSubstitution: Substitution.Unifier = solveRequest.context.actualSubstitution
    ) : StateEnd(solveRequest, executionStrategy, solverStrategies), SuccessFinalState

    internal data class False(
            override val solveRequest: Solve.Request,
            override val executionStrategy: CoroutineScope,
            override val solverStrategies: SolverStrategies
    ) : StateEnd(solveRequest, executionStrategy, solverStrategies), FailFinalState

    internal data class Halt(
            override val solveRequest: Solve.Request,
            override val executionStrategy: CoroutineScope,
            override val solverStrategies: SolverStrategies
    ) : StateEnd(solveRequest, executionStrategy, solverStrategies), FailFinalState

    internal data class Timeout(
            override val solveRequest: Solve.Request,
            override val executionStrategy: CoroutineScope,
            override val solverStrategies: SolverStrategies
    ) : StateEnd(solveRequest, executionStrategy, solverStrategies), FailFinalState
}
