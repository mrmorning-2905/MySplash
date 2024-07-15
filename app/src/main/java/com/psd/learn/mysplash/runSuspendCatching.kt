package com.psd.learn.mysplash

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalContracts::class)
suspend inline fun <R> runSuspendCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> R
): Result<R> {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return try {
        Result.success(withContext(context) { block() })
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun <T> Flow<T>.mapToResult(): Flow<Result<T>> =
    map { Result.success(it) }
        .catchAndReturn { Result.failure(it) }

fun <T> Flow<T>.catchAndReturn(
    itemSupplier: suspend (cause: Throwable) -> T
): Flow<T> =
    catch { cause -> emit(itemSupplier(cause)) }
