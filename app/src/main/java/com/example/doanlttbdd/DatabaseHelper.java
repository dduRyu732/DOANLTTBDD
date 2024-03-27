package com.example.doanlttbdd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "story";
    private static final int DATABASE_VERSION = 8;
    private static final String TABLE_STORY = "story";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CONTENT ="content";
    private Context context;
     private final String TAG ="DatabaseHelper";
    private DatabaseHelper databaseHelper;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        Log.d(TAG, "DBManager: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STORY_TABLE = "CREATE TABLE " + TABLE_STORY +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_AUTHOR + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CONTENT + " TEXT " +
                ")";
        db.execSQL(CREATE_STORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY);
        onCreate(db);

    }
    public long insertBook(long id, String title, String author, String description, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CONTENT, content);

        long result = db.insert(TABLE_STORY, null, values);
        db.close();

        return result;

    }
    public boolean deleteBook(long storyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("story", "id = ?", new String[]{String.valueOf(storyId)});
        db.close();

        return rowsAffected > 0;
    }
    public long updateBook(long id, String title, String author, String description, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CONTENT, content);

        long result = db.update(TABLE_STORY, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        return result;
    }
    public Story getStory(long storyId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {COLUMN_TITLE, COLUMN_AUTHOR, COLUMN_DESCRIPTION, COLUMN_CONTENT};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(storyId)};

        Cursor cursor = db.query(TABLE_STORY, projection, selection, selectionArgs, null, null, null);

        Story story = null;

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));

            story = new Story(storyId, title, author, description, content);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return story;
    }

    public List<Story> getAllStories() {
        List<Story> storyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STORY;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") long storyId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));

                Story story = new Story(storyId, title, author, description, content);

                storyList.add(story);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return storyList;
    }




    // Trong phương thức retrieveStoryFromSQLite()

}
