package it.unibo.tuprolog.collections

import it.unibo.tuprolog.collections.impl.MutableReteClauseMultiSet
import it.unibo.tuprolog.core.Clause
import it.unibo.tuprolog.core.Scope
import it.unibo.tuprolog.core.TermComparator
import it.unibo.tuprolog.utils.itemWiseEquals
import it.unibo.tuprolog.utils.itemWiseHashCode
import kotlin.js.JsName
import kotlin.jvm.JvmStatic

interface MutableClauseMultiSet : ClauseMultiSet {

    /** Adds a [Clause] to this [MutableClauseMultiSet] **/
    override fun add(clause: Clause): MutableClauseMultiSet

    /** Adds all the given [Clause] to this [MutableClauseMultiSet] **/
    override fun addAll(clauses: Iterable<Clause>): MutableClauseMultiSet

    /** Retrieves the first unifying [Clause] from this [MutableClauseMultiSet] as a [RetrieveResult]**/
    override fun retrieve(clause: Clause): RetrieveResult<out MutableClauseMultiSet>

    /** Retrieves all the unifying [Clause] from this [MutableClauseMultiSet] as a [RetrieveResult]**/
    override fun retrieveAll(clause: Clause): RetrieveResult<out MutableClauseMultiSet>

    companion object {

        /** Creates an empty [MutableClauseMultiSet] **/
        @JvmStatic
        @JsName("empty")
        fun empty(): MutableClauseMultiSet = of(emptyList())

        /** Creates a [MutableClauseMultiSet] with given clauses */
        @JvmStatic
        @JsName("of")
        fun of(vararg clause: Clause): MutableClauseMultiSet = of(clause.asIterable())

        /** Let developers easily create a [MutableClauseMultiSet] programmatically while avoiding variables names clashing */
        @JvmStatic
        @JsName("ofScopes")
        fun of(vararg clause: Scope.() -> Clause): MutableClauseMultiSet =
            of(clause.map {
                Scope.empty(it)
            })

        /** Creates a [MutableClauseMultiSet] from the given [Sequence] of [Clause] */
        @JvmStatic
        @JsName("ofSequence")
        fun of(clauses: Sequence<Clause>): MutableClauseMultiSet = of(clauses.asIterable())

        /** Creates a [MutableClauseMultiSet] from the given [Iterable] of [Clause] */
        @JvmStatic
        @JsName("ofIterable")
        fun of(clauses: Iterable<Clause>): MutableClauseMultiSet =
            MutableReteClauseMultiSet(clauses)

        @JvmStatic
        @JsName("equals")
        fun equals(multiSet1: MutableClauseMultiSet, multiSet2: MutableClauseMultiSet): Boolean {
            return ClauseMultiSet.equals(multiSet1, multiSet2)
        }

        @JvmStatic
        @JsName("hashCode")
        fun hashCode(multiSet: MutableClauseMultiSet): Int {
            return itemWiseHashCode(
                MutableClauseMultiSet::class,
                multiSet.sortedWith(TermComparator.DefaultComparator)
            )
        }
    }

}