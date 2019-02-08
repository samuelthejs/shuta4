package com.example.davkimfray.shuta4;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class shutaProvider extends ContentProvider {

    public static final String PROVIDER_NAME =  "com.example.davkimfray.shuta4.shutaProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/students";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String timestamp = "timeIn";
    static final int students = 1 ;
    static final int students_id = 2;
    static final UriMatcher uriMatcher ;
    static final String DATABASE_NAME = "shutadb";
     static final  String STUDENTS_TABLE_NAME = "students";

    //database declarations
    static final int DATABASE_VERSION = 1 ;
    static final String CREATE_DB_TABLE =  " CREATE TABLE " + STUDENTS_TABLE_NAME +
            " (  _id INTEGER primary key autoincrement , " +
            " timesIn TEXT  ) ; " ;
    private static HashMap<String , String> LOGIN_HASH_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"students",1);
        uriMatcher.addURI(PROVIDER_NAME,"students/#",2);

    }

    private SQLiteDatabase db ;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();

        return  (db ==null)? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder ) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(STUDENTS_TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case students:
                queryBuilder.setProjectionMap(LOGIN_HASH_MAP);
            case students_id:
                queryBuilder.appendWhere( _ID  +" = "+ uri.getPathSegments().get(1));
                break;
      default:
          throw new IllegalArgumentException("unknown uri " + uri);
        }

          Cursor c = queryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
          c.setNotificationUri(getContext().getContentResolver(),uri);
          return  c;

    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case  students:
                return  "vnd.android.cursor.dir/vnd.example.students";
            case students_id:
                return "vnd.android.cursor.item/vnd.example.students";
                default:
                    throw new IllegalArgumentException("unsupported uri"+  uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert(STUDENTS_TABLE_NAME,"",values);
        throw new SQLException("failed to add data into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
        case students:
            count = db.update(STUDENTS_TABLE_NAME,values,selection,selectionArgs);
        case students_id:

            count =db.update(STUDENTS_TABLE_NAME,values, _ID + " =" + uri.getPathSegments().get(1) +
                            (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs );
                            break;
        default:
            throw new IllegalArgumentException("unknown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    private static  class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper( Context context) {
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("  DROP TABLE IF EXISTS " + STUDENTS_TABLE_NAME);
            onCreate(db);

        }
    }
}
