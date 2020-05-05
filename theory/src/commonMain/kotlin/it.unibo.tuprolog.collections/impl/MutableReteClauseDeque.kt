package it.unibo.tuprolog.collections.impl

import it.unibo.tuprolog.collections.AbstractMutableReteClauseCollection
import it.unibo.tuprolog.collections.MutableClauseDeque
import it.unibo.tuprolog.collections.RetrieveResult
import it.unibo.tuprolog.collections.rete.ReteNode
import it.unibo.tuprolog.core.Clause
import it.unibo.tuprolog.theory.Theory

internal class MutableReteClauseDeque private constructor(
    private val rete: ReteNode<*, Clause>
) : MutableClauseDeque, AbstractMutableReteClauseCollection<MutableReteClauseDeque>(rete) {

    /** Construct a [MutableReteClauseDeque] from given clauses */
    constructor(clauses: Iterable<Clause>) : this(ReteNode.ofList(clauses)) {
        Theory.checkClausesCorrect(clauses)
    }

    override fun getFirst(clause: Clause): Sequence<Clause> =
        rete.get(clause)

    override fun getLast(clause: Clause): Sequence<Clause> =
        getFirst(clause).toList().asReversed().asSequence()

    override fun addFirst(clause: Clause): MutableReteClauseDeque {
        rete.put(clause, beforeOthers = true)
        return this
    }

    override fun addLast(clause: Clause): MutableReteClauseDeque {
        rete.put(clause)
        return this
    }

    override fun retrieveFirst(clause: Clause): RetrieveResult<out MutableReteClauseDeque> {
        val retracted = rete.remove(clause)

        return when {
            retracted.none() ->
                RetrieveResult.Failure(this)
            else ->
                RetrieveResult.Success(
                    this, retracted.toList()
                )
        }
    }

    override fun retrieveLast(clause: Clause): RetrieveResult<out MutableReteClauseDeque> {
        TODO("Not yet implemented")
    }
}