package it.unibo.tuprolog.core

interface EmptyList : Atom, List {

    override val isCouple: Boolean
        get() = false

    override val isList: Boolean
        get() = true

    override val isEmptyList: Boolean
        get() = true

    override fun toArray(): Array<Term> {
        return arrayOf()
    }

    override fun toList(): kotlin.collections.List<Term> {
        return emptyList()
    }

    override fun toSequence(): Sequence<Term> {
        return emptySequence()
    }

    companion object {
        operator fun invoke(): EmptyList {
            return EmptyListImpl
        }
    }
}