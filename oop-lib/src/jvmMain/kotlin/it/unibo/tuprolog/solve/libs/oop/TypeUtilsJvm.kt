package it.unibo.tuprolog.solve.libs.oop

import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.utils.Optional
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance

actual val KClass<*>.companionObjectRef: Optional<out Any>
    get() = Optional.of(companionObjectInstance)

actual val KClass<*>.companionObjectType: Optional<out KClass<*>>
    get() = Optional.of(companionObject)

actual fun kClassFromName(qualifiedName: String): Optional<out KClass<*>> {
    require(CLASS_NAME_PATTERN.matches(qualifiedName)) {
        "`$qualifiedName` must match ${CLASS_NAME_PATTERN.pattern} while it doesn't"
    }
    return try {
        Optional.of(Class.forName(qualifiedName).kotlin)
    } catch (e: ClassNotFoundException) {
        Optional.none()
    }
}

private val classNamePattern = "^$id(\\.$id)*$".toRegex()

actual val CLASS_NAME_PATTERN: Regex
    get() = classNamePattern

actual val KClass<*>.allSupertypes: Sequence<KClass<*>>
    get() = supertypes.asSequence()
        .map { it.classifier }
        .filterIsInstance<KClass<*>>()
        .flatMap { sequenceOf(it) + it.allSupertypes }
        .distinct()

actual val KCallable<*>.actualParameterTypes: List<KClass<*>>
    get() = parameters.filterNot { it.kind == KParameter.Kind.INSTANCE }.map { it.type.classifier as KClass<*> }

private fun List<KParameter>.match(types: List<Set<KClass<*>>>): Boolean {
    if (size != types.size) return false
    for (i in this.indices) {
        val possible = types[i]
        when (val formal = this[i].type.classifier) {
            is KClass<*> -> {
                if (possible.none { formal isSupertypeOf it }) return false
            }
            else -> return false
        }
    }
    return true
}

actual fun KClass<*>.findMethod(methodName: String, admissibleTypes: List<Set<KClass<*>>>): KCallable<*> =
    members.filter { it.name == methodName }
        .firstOrNull { method ->
            method.parameters.filterNot { it.kind == KParameter.Kind.INSTANCE }.match(admissibleTypes)
        } ?: throw MethodInvocationException(this, methodName, admissibleTypes)

actual val KClass<*>.fullName: String
    get() = qualifiedName!!

actual val KClass<*>.name: String
    get() = simpleName!!

actual fun KClass<*>.invoke(
    methodName: String,
    arguments: List<Term>,
    instance: Any?
): Result {
    val converter = TermToObjectConverter.default
    val methodRef = findMethod(methodName, arguments.map { converter.admissibleTypes(it) })
    val argumentsExpectedTypes = methodRef.actualParameterTypes
    require(argumentsExpectedTypes.size == arguments.size) {
        """
            |
            |Error while invoking ${methodRef.name} the expected argument types 
            |   ${argumentsExpectedTypes.map { it.name }} 
            |are not as many as the as the actual parameters (${argumentsExpectedTypes.size} vs. ${arguments.size}):
            |   $arguments
            |
            """.trimMargin()
    }
    val args = arguments.mapIndexed { i, it ->
        converter.convertInto(argumentsExpectedTypes[i], it)
    }.toTypedArray()
    val result = if (instance == null) methodRef.call(*args) else methodRef.call(instance, *args)
    return Result.Value(result)
}