package it.unibo.tuprolog.solve.solver.statemachine.state

import it.unibo.tuprolog.primitive.Signature
import it.unibo.tuprolog.solve.Solve
import it.unibo.tuprolog.solve.solver.SolverStrategies
import it.unibo.tuprolog.solve.solver.statemachine.StateUtils.isWellFormed
import it.unibo.tuprolog.solve.solver.statemachine.StateUtils.prepareForExecution
import kotlinx.coroutines.CoroutineScope

/**
 * The state responsible of making the choice of which goal will be solved next
 *
 * @author Enrico
 */
internal class StateGoalSelection(
        override val solveRequest: Solve.Request,
        override val executionStrategy: CoroutineScope,
        override val solverStrategies: SolverStrategies
) : AbstractTimedState(solveRequest, executionStrategy, solverStrategies) {

    override fun behaveTimed(): Sequence<State> = sequence {
        val currentGoal = with(solveRequest) { signature.withArgs(arguments) }

        when {
            // current goal is already demonstrated
            with(currentGoal) { this != null && solverStrategies.successCheckStrategy(this, solveRequest.context) } ->
                yield(StateEnd.True(solveRequest, executionStrategy, solverStrategies))

            else -> when {
                // vararg primitive
                currentGoal == null ->
                    yield(StateGoalEvaluation(solveRequest, executionStrategy, solverStrategies))

                isWellFormed(currentGoal) ->
                    prepareForExecution(currentGoal).also { preparedGoal ->
                        yield(StateGoalEvaluation(
                                solveRequest.copy(
                                        signature = Signature.fromIndicator(preparedGoal.indicator)!!,
                                        arguments = preparedGoal.argsList
                                ),
                                executionStrategy,
                                solverStrategies
                        ))
                    }

                // goal non well-formed
                else ->
                    yield(StateEnd.False(solveRequest, executionStrategy, solverStrategies))
            }
        }
    }
}
