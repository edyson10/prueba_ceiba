package co.com.ceiba.mobile.pruebadeingreso.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class ConexionSQLite extends SQLiteOpenHelper {

    final String CREAR_TABLA_USER = "CREATE TABLE users ( id INTEGER NOT NULL, name text NOT NULL, email text NOT NULL, phone text NOT NULL )";

    public ConexionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREAR_TABLA_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int versionAnterior, int versionNueva) {
        if (versionAnterior < 2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        }
    }
}
