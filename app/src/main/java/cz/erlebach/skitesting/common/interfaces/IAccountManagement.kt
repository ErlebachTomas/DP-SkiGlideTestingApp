package cz.erlebach.skitesting.common.interfaces

interface IAccountManagement {

    /** Přihlášení k účtu
     * @return true pokud se přihlášení zdařilo
     * */
    fun login() : Boolean

    /** Odhlášení z uživatelského účtu
     * @return true podud se odhlášení zdařilo */
    fun logout(): Boolean
}