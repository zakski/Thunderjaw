package it.unibo.tuprolog.collections

import it.unibo.tuprolog.collections.impl.ReteClauseQueue
import it.unibo.tuprolog.core.Clause
import it.unibo.tuprolog.core.Scope

interface ClauseQueue : ClauseCollection {

    /** Gives a freshly produced [ClauseQueue] including the given [Clause] in the first position and the content
     *  of this one **/
    fun addFirst(clause: Clause): ClauseQueue

    /** Gives a freshly produced [ClauseQueue] including the given [Clause] in the last position and the content
     *  of this one **/
    fun addLast(clause: Clause): ClauseQueue

    /** Produces a [Sequence] of the clauses that would unify over the given [Clause], scanning from data structure from
     *  the first position to the last one **/
    fun getFifoOrdered(clause: Clause): Sequence<Clause>

    /** Produces a [Sequence] of the clauses that would unify over the given [Clause], scanning from data structure from
     *  the last position to the first **/
    fun getLifoOrdered(clause: Clause): Sequence<Clause>

    /** Produces a [Sequence] of the clauses that would unify over the given [Clause]. Analogous to [getFifoOrdered] **/
    operator fun get(clause: Clause): Sequence<Clause> =
        getFifoOrdered(clause)

    /** Gives a freshly produced [ClauseQueue] including the given [Clause] and the content of this one.
     *  Analogous to [addFirst] **/
    override fun add(clause: Clause): ClauseQueue =
        addLast(clause)

    /** Gives a freshly produced [ClauseQueue] including all the given [Clause] and the content of this one **/
    override fun addAll(clauses: Iterable<Clause>): ClauseQueue

    /** Retrieve the first [Clause] unifying the given one, searching from the first position **/
    fun retrieveFirst(clause: Clause): RetrieveResult<out ClauseQueue>

    /** Retrieve the first [Clause] unifying the given one. Analogous to [retrieveFirst] **/
    override fun retrieve(clause: Clause): RetrieveResult<out ClauseQueue> =
        retrieveFirst(clause)

    /** Retrieve all the [Clause] unifying the given one **/
    override fun retrieveAll(clause: Clause): RetrieveResult<out ClauseQueue>

    companion object {

        /** Creates an empty [ClauseQueue] **/
        fun empty(): ClauseQueue = of(emptyList())

        /** Creates a [ClauseQueue] with given clauses */
        fun of(vararg clause: Clause): ClauseQueue = of(clause.asIterable())

        /** Let developers easily create a [ClauseQueue] programmatically while avoiding variables names clashing */
        fun of(vararg clause: Scope.() -> Clause): ClauseQueue =
            of(clause.map {
                Scope.empty(it)
            })

        /** Creates a [ClauseQueue] from the given [Sequence] of [Clause] */
        fun of(clauses: Sequence<Clause>): ClauseQueue = of(clauses.asIterable())

        /** Creates a [ClauseQueue] from the given [Iterable] of [Clause] */
        fun of(clauses: Iterable<Clause>): ClauseQueue =
            ReteClauseQueue(clauses)
    }

}

