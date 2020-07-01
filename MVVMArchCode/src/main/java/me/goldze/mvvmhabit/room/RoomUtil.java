package me.goldze.mvvmhabit.room;

import android.util.ArrayMap;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import me.goldze.mvvmhabit.base.BaseApplication;

public abstract class RoomUtil {
    private static final ArrayMap<String, AbstractAppDatabase> mDBEntityMap = new ArrayMap<>();

    public static synchronized <T extends AbstractAppDatabase> T getDB(Class<T> cls, String dbName,
                                                                       RoomDatabase.Callback callback,
                                                                       Migration[] migrations) {
        final String name = cls.getName();
        AbstractAppDatabase database = mDBEntityMap.get(name);
        if (database == null) {
            RoomDatabase.Builder<T> builder = Room.databaseBuilder(BaseApplication.getInstance(), cls,
                    dbName);
            if (callback != null) {
                builder.addCallback(callback);
            }
            if (migrations != null) {
                builder.addMigrations(migrations);
            }
            database = builder.build();
            mDBEntityMap.put(name, database);
        }
        return (T) database;
    }

    public static <T extends AbstractAppDatabase> T getDB(Class<T> cls, RoomDatabase.Callback callback, Migration[] migrations) {
        return getDB(cls, "default.db", callback, migrations);
    }

    public static <T extends AbstractAppDatabase> T getDB(Class<T> cls) {
        return getDB(cls, null, null);
    }

    public static <T extends AbstractAppDatabase> T getDB(Class<T> cls, Migration[] migrations) {
        return getDB(cls, null, migrations);
    }
}
