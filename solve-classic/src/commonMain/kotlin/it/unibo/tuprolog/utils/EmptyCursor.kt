package it.unibo.tuprolog.utils

internal object EmptyCursor : AbstractCursor<Nothing>() {
    override val next: Cursor<Nothing>
        get() = throw NoSuchElementException()

    override val current: Nothing?
        get() = throw NoSuchElementException()

    override val hasNext: Boolean
        get() = false

    override val isOver: Boolean
        get() = true

    override fun toString(): String {
        return super<AbstractCursor>.toString()
    }
}