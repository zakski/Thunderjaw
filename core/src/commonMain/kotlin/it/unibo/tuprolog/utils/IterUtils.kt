@file:JvmName("IterUtils")

package it.unibo.tuprolog.utils

import kotlin.jvm.JvmName

fun <T> merge(comparator: Comparator<T>, iterables: Iterable<Iterable<T>>): Sequence<T> {
    return sequence {
        val pipeline = iterables.asSequence().map { it.cursor() }.filterNot { it.isOver }.toMutableList()
        while (pipeline.isNotEmpty()) {
            val (minIndex, minValue) = pipeline.asSequence().map { it.current!! }.indexed().minWith(
                Comparator<IntIndexed<T>> { a, b -> comparator.compare(a.value, b.value) }
            )!!
            yield(minValue)
            pipeline[minIndex].next.let {
                if (it.isOver) {
                    pipeline.removeAt(minIndex)
                } else {
                    pipeline[minIndex] = it
                }
            }
        }
    }
}

fun <T> merge(iterables: Iterable<Iterable<T>>, comparator: (T, T) -> Int): Sequence<T> {
    return merge(Comparator(comparator), iterables)
}

fun <T> merge(vararg iterables: Iterable<T>, comparator: (T, T) -> Int): Sequence<T> {
    return merge(Comparator(comparator), *iterables)
}

fun <T> merge(comparator: Comparator<T>, vararg iterables: Iterable<T>): Sequence<T> {
    return merge(comparator, listOf(*iterables))
}

fun <T> merge(iterables: Sequence<Iterable<T>>, comparator: (T, T) -> Int): Sequence<T> {
    return merge(Comparator(comparator), iterables)
}

fun <T> merge(comparator: Comparator<T>, iterables: Sequence<Iterable<T>>): Sequence<T> {
    return merge(comparator, iterables.asIterable())
}

fun <T> mergeSequences(iterables: Iterable<Sequence<T>>, comparator: (T, T) -> Int): Sequence<T> {
    return merge(Comparator(comparator), iterables.map { it.asIterable() })
}

fun <T> mergeSequences(comparator: Comparator<T>, iterables: Iterable<Sequence<T>>): Sequence<T> {
    return merge(comparator, iterables.map { it.asIterable() })
}

fun <T> mergeSequences(iterables: Sequence<Sequence<T>>, comparator: (T, T) -> Int): Sequence<T> {
    return merge(Comparator(comparator), iterables.map { it.asIterable() }.asIterable())
}

fun <T> mergeSequences(comparator: Comparator<T>, iterables: Sequence<Sequence<T>>): Sequence<T> {
    return merge(comparator, iterables.map { it.asIterable() }.asIterable())
}

fun <T> mergeSequences(vararg iterables: Sequence<T>, comparator: (T, T) -> Int): Sequence<T> {
    return merge(Comparator(comparator), iterables.map { it.asIterable() })
}

fun <T> mergeSequences(comparator: Comparator<T>, vararg iterables: Sequence<T>): Sequence<T> {
    return merge(comparator, iterables.map { it.asIterable() })
}

fun <T, U, R> Sequence<T>.product(other: Sequence<U>, combinator: (T, U) -> R): Sequence<R> =
    flatMap { x ->
        other.map { y -> combinator(x, y) }
    }

fun <T, U> Sequence<T>.product(other: Sequence<U>): Sequence<Pair<T, U>> =
    product(other, ::Pair)

fun <T, R> Sequence<T>.squared(combinator: (T, T) -> R): Sequence<R> =
    product(this, combinator)

fun <T> Sequence<T>.squared(): Sequence<Pair<T, T>> =
    product(this)

fun <T> Sequence<T>.longIndexed(): Sequence<LongIndexed<T>> =
    zip(LongRange(0, Long.MAX_VALUE).asSequence()) { it, i ->
        LongIndexed.of(i, it)
    }

fun <T> Sequence<T>.indexed(): Sequence<IntIndexed<T>> =
    zip(IntRange(0, Int.MAX_VALUE).asSequence()) { it, i ->
        IntIndexed.of(i, it)
    }

fun <T> interleave(iterables: Iterable<Iterable<T>>): Sequence<T> =
    sequence {
        val pipeline = iterables.asSequence()
            .map { it.iterator() }
            .filter { it.hasNext() }
            .toList()
        var nNonEmpty = pipeline.size
        while (nNonEmpty > 0) {
            nNonEmpty = 0
            for (iter in pipeline) {
                if (iter.hasNext()) {
                    nNonEmpty++
                    yield(iter.next())
                }
            }
        }
    }

fun <T> interleave(vararg iterables: Iterable<T>): Sequence<T> =
    interleave(iterables.asIterable())

fun <T> interleave(iterables: Sequence<Iterable<T>>): Sequence<T> =
    interleave(iterables.asIterable())

fun <T> interleaveSequences(vararg iterables: Sequence<T>): Sequence<T> =
    interleave(sequenceOf(*iterables).map { it.asIterable() }.asIterable())

fun <T> interleaveSequences(iterables: Sequence<Sequence<T>>): Sequence<T> =
    interleave(iterables.map { it.asIterable() }.asIterable())

fun <T> interleaveSequences(iterables: Iterable<Sequence<T>>): Sequence<T> =
    interleave(iterables.map { it.asIterable() })