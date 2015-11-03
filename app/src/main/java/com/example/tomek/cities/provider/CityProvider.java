package com.example.tomek.cities.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.tomek.cities.model.City;

import java.sql.SQLException;

/**
 * Created by Tomek on 2015-11-03.
 */
public class CityProvider extends ContentProvider{

    public static final String AUTHORITY = "com.example.tomek.cities.provider";
    public static final String PROVIDER_NAME = AUTHORITY + ".CityProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/city";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final int ALL_CITIES = 10;
    public static final int SINGLE_CITY = 20;
    public static final String CITY_TABLE = "city";
    public static final String[] ALL_COLUMNS = new String[]{};
    public static final String DATABASE_NAME = "CityProvider";
    public static final int DATABASE_VERSION = 1;
    public static final String TAG = "CityProvider";

    public static final String CREATE_SQL = "CREATE TABLE " + CITY_TABLE + " ("
            + City.CityColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + City.CityColumns.NAME + " TEXT NOT NULL);";

    public static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public MyDatabaseHelper databaseHelper;

    static {
        uriMatcher.addURI(PROVIDER_NAME,"city",ALL_CITIES);
        uriMatcher.addURI(PROVIDER_NAME,"city/#",SINGLE_CITY);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new MyDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        projection = projection == null ? ALL_COLUMNS : projection;

        sortOrder = sortOrder == null ? City.CityColumns.NAME : sortOrder;
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)){
            case ALL_CITIES:
                return database.query(CITY_TABLE,projection,selection,selectionArgs,null,null,sortOrder);
            case SINGLE_CITY:
                String taskId = uri.getLastPathSegment();
                selection = fixSelectionString(selection);
                selectionArgs = fixSelectionArgs(selectionArgs,taskId);
                return database.query(CITY_TABLE,projection,selection,selectionArgs,null,null,sortOrder);
            default:
                throw new IllegalArgumentException("Niepoprawny Uri: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Uri result = null;
        try {
            result = doInsert(uri,values,database);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.close();
        getContext().getContentResolver().notifyChange(uri,null);
        return result;
    }

    private Uri doInsert(Uri uri, ContentValues values, SQLiteDatabase database) throws SQLException {
        Uri result = null;
        switch (uriMatcher.match(uri)){
            case ALL_CITIES:
                long id = database.insert(CITY_TABLE,"",values);
                if (id == -1) {
                    throw new SQLException("Błąd wstawiania danych!");
                }
                result = Uri.withAppendedPath(uri,String.valueOf(id));
        }
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsDeleted = database.delete(CITY_TABLE,selection,selectionArgs);
        database.close();
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsUpdated = database.update(CITY_TABLE,values,selection,selectionArgs);
        database.close();
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }

    public static String[] fixSelectionArgs(String[] selectionArgs, String taskId){
        if (selectionArgs == null){
            selectionArgs = new String[]{taskId};
        }else{
            String[] newSelectionArgs = new String[selectionArgs.length+1];
            newSelectionArgs[0] = taskId;
            System.arraycopy(selectionArgs,0,newSelectionArgs,1,newSelectionArgs.length);
        }
        return  selectionArgs;
    }

    public static String fixSelectionString(String selection){
        selection = selection == null ? City.CityColumns._ID + " = ?" :
                City.CityColumns._ID + " = ? AND (" + selection + ")";
        return selection;
    }

    private class MyDatabaseHelper extends SQLiteOpenHelper {

        public MyDatabaseHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP IF TABLE EXISTS " + CITY_TABLE);
            onCreate(db);
        }

    }
}
