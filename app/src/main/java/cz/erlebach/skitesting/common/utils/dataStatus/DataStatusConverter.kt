package cz.erlebach.skitesting.common.utils.dataStatus

import androidx.room.TypeConverter

class DataStatusConverter {

        @TypeConverter
        fun fromDataStatus(status: DataStatus): String {
            return status.name
        }
        @TypeConverter
        fun toDataStatus(status: String): DataStatus {
            return DataStatus.valueOf(status)
        }

}