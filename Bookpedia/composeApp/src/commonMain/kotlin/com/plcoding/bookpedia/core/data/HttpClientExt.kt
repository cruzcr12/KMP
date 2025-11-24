package com.plcoding.bookpedia.core.data

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext


suspend inline fun <reified T> safeApiCall(
    execute: () -> HttpResponse
): Result<T, DataError.Remote>{
    val response = try {
        execute()
    } catch (e: SocketTimeoutException){
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: UnresolvedAddressException){
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: Exception){
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    return responseToResult(response)
}


/**
 * Function that tries to get the body of the response and returns a Result.Error if it fails.
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Remote> {
    return when(response.status.value){
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException){
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        400 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        403, 429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Remote.INTERNAL_SERVER_ERROR)
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}