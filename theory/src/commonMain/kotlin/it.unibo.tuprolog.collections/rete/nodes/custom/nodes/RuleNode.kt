package it.unibo.tuprolog.collections.rete.nodes.custom.nodes

import it.unibo.tuprolog.collections.rete.nodes.custom.IndexedClause
import it.unibo.tuprolog.collections.rete.nodes.custom.Utils.functorOfNestedFirstArgument
import it.unibo.tuprolog.collections.rete.nodes.custom.ReteNode
import it.unibo.tuprolog.core.Clause

internal class RuleNode(
    private val ordered: Boolean
    ) : ReteNode {

    private val functors: MutableMap<String, FunctorReteNode> = mutableMapOf()

    override fun get(clause: Clause): Sequence<Clause> =
        functors[clause.nestedFunctor()]?.get(clause) ?: emptySequence()

    override fun assertA(clause: IndexedClause) =
        clause.nestedFunctor().let {
            if(ordered){
                functors.getOrPut(it){
                    FunctorReteNode(ordered, 0)
                }.assertA(clause)
            } else{
                assertZ(clause)
            }
        }

    override fun assertZ(clause: IndexedClause) =
        clause.nestedFunctor().let {
            functors.getOrPut(it){
                FunctorReteNode(ordered, 0)
            }.assertZ(clause)
        }

    override fun retractFirst(clause: Clause): Sequence<Clause> =
        functors[clause.nestedFunctor()]?.retractFirst(clause) ?: emptySequence()

    override fun retractAll(clause: Clause): Sequence<Clause> =
        functors[clause.nestedFunctor()]?.retractAll(clause) ?: emptySequence()

    private fun Clause.nestedFunctor(): String =
        this.head!!.functorOfNestedFirstArgument(0)

    private fun IndexedClause.nestedFunctor(): String =
        this.innerClause.head!!.functorOfNestedFirstArgument(0)


}