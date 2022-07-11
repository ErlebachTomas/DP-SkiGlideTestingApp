package cz.erlebach.skitesting

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.erlebach.skitesting.db.SkiDao
import cz.erlebach.skitesting.model.Ski

/**
 * ORM ROOM lokální databáze
 */
@Database(entities = [Ski::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    // todo ostatni dao
    abstract fun skiDao(): SkiDao

    companion object {

        // list konstant
        const val dbName = "mydatabase"
        const val skiTableName: String="skiTable"


        /**
         * Singleton, držící jednu instantci databáze
         */
        @Volatile
        private var INSTANCE: MyDatabase? = null

        /**
         * Vrátí instanci db
         * */
        fun getDatabase(context: Context): MyDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    this.dbName
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}