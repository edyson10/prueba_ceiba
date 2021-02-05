package co.com.ceiba.mobile.pruebadeingreso.Interface;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.Database.DTO.User;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM " + User.TABLE_NAME)
    List<User> getAllUsuarios();

    @Query("SELECT COUNT(*) FROM " + User.TABLE_NAME)
    int count();

    @Insert
    long insert(User usuario);

    @Insert
    void instarAll(User... usuarios);
}
