package com.ccx.corecarbon.room_util;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.DeleteColumn;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;

@Database(version = 2,
        entities = {Form.class},
        autoMigrations = {
                @AutoMigration(from = 1, to = 2, spec = AppDatabase.MigrationFrom1To2.class)
        }
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FormDao formDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Cook Stove")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }

    @DeleteColumn.Entries({
            @DeleteColumn(tableName = "Form", columnName = "serialNumberImage"),
            @DeleteColumn(tableName = "Form", columnName = "housePhoto"),
            @DeleteColumn(tableName = "Form", columnName = "bankDocument"),
    })
    static class MigrationFrom1To2 implements AutoMigrationSpec {
    }
}
