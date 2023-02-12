package cz.erlebach.skitesting.repository

import kotlinx.coroutines.flow.*

/**
 * Obecná flow funkce která poskytuje přístup k offline datům z databáze a zároveň i online ze sítě. Může pracovat s jakýmkoli typem dat.
 * @param T_RESULT generický typ offline výsledku
 * @param R_REQUEST generický typ online požadavku
 */
inline fun <T_RESULT, R_REQUEST> networkBoundResource(
    /** Získá lokální data */
    crossinline localFlow: () -> Flow<T_RESULT>,
    /** Síťový požadavek  */
    crossinline fetchFromRemote: suspend () -> R_REQUEST,
    /** Synchronizuje výsledek síťového požadavku s místní ROOM databází. */
    crossinline sync: suspend (R_REQUEST) -> Unit,
    /** Rozhoduje o tom, zda se má provést síťový požadavek nebo jen použít místní data */
    crossinline shouldFetch: (T_RESULT) -> Boolean = { true } // defaultně true
) = flow {

    val data = localFlow().first() //načtení offline dat z místní ROOM paměti

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data)) //  zpráva pro UI, že se provádí práce na pozadí

        try {
            sync(fetchFromRemote()) // zpracování online dat, synchronizace s ROOM databází
            localFlow().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            localFlow().map { Resource.Error(throwable, it) } //chyba
        }
    } else {
        localFlow().map { Resource.Success(it) } //Vytvoří dotaz do databáze a odešlete jej do UI
    }

    emitAll(flow)
}