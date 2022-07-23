package cz.erlebach.skitesting.interfaces

interface IAccountManagement {

    /** Přihlášení k účtu */
    fun login()

    /** Odhlášení z uživatelského účtu */
    fun logout()
}