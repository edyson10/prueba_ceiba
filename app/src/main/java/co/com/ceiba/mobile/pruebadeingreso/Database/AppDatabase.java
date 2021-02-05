package co.com.ceiba.mobile.pruebadeingreso.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import co.com.ceiba.mobile.pruebadeingreso.Database.DTO.User;
import co.com.ceiba.mobile.pruebadeingreso.Interface.UserDAO;

@Database(entities = User.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * The only instance
     */
    private static AppDatabase sInstance;

    public abstract UserDAO userDao();
}
