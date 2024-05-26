package viewmodel.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL

interface IClearAllTablesFix {
    fun clearAllTables()
}

@Database(entities = [RecurringExpense::class], version = 4)
abstract class RecurringExpenseDatabase : RoomDatabase(), IClearAllTablesFix {
    abstract fun recurringExpenseDao(): RecurringExpenseDao

    override fun clearAllTables() {}

    companion object {
        fun getRecurringExpenseDatabase(builder: Builder<RecurringExpenseDatabase>): RecurringExpenseDatabase {
            return builder
                .addMigrations(migration_1_2)
                .addMigrations(migration_2_3)
                .addMigrations(migration_3_4)
                .setDriver(BundledSQLiteDriver())
                .build()
        }

        private val migration_1_2 =
            object : Migration(1, 2) {
                override fun migrate(connection: SQLiteConnection) {
                    connection.execSQL(
                        "ALTER TABLE recurring_expenses ADD COLUMN everyXRecurrence INTEGER DEFAULT 1",
                    )
                    connection.execSQL("ALTER TABLE recurring_expenses ADD COLUMN recurrence INTEGER DEFAULT 3")
                }
            }

        private val migration_2_3 =
            object : Migration(2, 3) {
                override fun migrate(connection: SQLiteConnection) {
                    connection.execSQL("ALTER TABLE recurring_expenses ADD COLUMN firstPayment INTEGER DEFAULT 0")
                }
            }

        private val migration_3_4 =
            object : Migration(3, 4) {
                override fun migrate(connection: SQLiteConnection) {
                    connection.execSQL("ALTER TABLE recurring_expenses ADD COLUMN color INTEGER DEFAULT 0")
                }
            }
    }
}
