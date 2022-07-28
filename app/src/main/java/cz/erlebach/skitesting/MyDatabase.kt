package cz.erlebach.skitesting

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.erlebach.skitesting.db.SkiDao
import cz.erlebach.skitesting.db.TestSessionDao
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.utils.DateConverters

/**
 * ORM ROOM lokální databáze
 */
@Database(entities = [Ski::class, TestSession::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class MyDatabase : RoomDatabase() {

    // todo ostatni dao
    abstract fun skiDao(): SkiDao
    abstract fun testSessionDao(): TestSessionDao

    companion object {

        // list konstant
        const val dbName = "mydatabase"
        const val skiTableName: String="skiTable"
        const val testSessionsTableName: String="testSessionTable"

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