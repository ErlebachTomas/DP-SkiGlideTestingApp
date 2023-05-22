package cz.erlebach.skitesting.fragments.template


/**
 * Thread-Safe Singleton design pattern
 * @author aminography
 * @see <a href="https://stackoverflow.com/a/53580852"> Thread-Safe Singleton with parameter in Kotlin</a>
 * */
open class SingletonHolder<out T, in A>(private val constructor: (A) -> T) {

    @Volatile
    private var instance: T? = null

    /**
     * Vrátí instanci třídy
     */
    fun getInstance(arg: A): T =
        instance ?: synchronized(this) {
            instance ?: constructor(arg).also { instance = it }
        }
}