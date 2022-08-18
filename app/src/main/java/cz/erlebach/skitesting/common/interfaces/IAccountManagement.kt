package cz.erlebach.skitesting.common.interfaces

interface IAccountManagement {

    /** Přihlášení k účtu */
    fun login()

    /** Odhlášení z uživatelského účtu */
    fun logout()
}