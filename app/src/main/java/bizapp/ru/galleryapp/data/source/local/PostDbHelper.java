package bizapp.ru.galleryapp.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by android on 11.07.2018.
 */

class PostDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Gallery.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = " ,";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PostsPersistenceContract.PostsEntry.TABLE_NAME + " (" +
                    PostsPersistenceContract.PostsEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PostsPersistenceContract.PostsEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PostsPersistenceContract.PostsEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    PostsPersistenceContract.PostsEntry.COLUMN_NAME_SOURCE + TEXT_TYPE +
                    " )";


    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
