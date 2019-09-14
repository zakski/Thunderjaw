package it.unibo.tuprolog.solve.exception

import it.unibo.tuprolog.solve.ExecutionContext

/**
 * An exception thrown if there are problems during state machine execution, and solution process should be halted
 *
 * TODO find if more detailed documentation is present in standard prolog
 *
 * @param message the detail message string.
 * @param cause the cause of this exception.
 * @param context The current context at exception creation
 *
 * @author Enrico
 */
internal class HaltException(
        override val message: String? = null,
        override val cause: Throwable? = null,
        override val context: ExecutionContext
) : TuPrologRuntimeException(message, cause, context) {

    constructor(cause: Throwable?, context: ExecutionContext) : this(cause?.toString(), cause, context)
}
