package com.example.doanlttbdd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "story";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STORY = "story";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CONTENT ="content";
    private Context context;
     private final String TAG ="DatabaseHelper";

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
                COLUMN_DESCRIPTION + " TEXT" +
                COLUMN_CONTENT + "TEXT " +
                ")";
        db.execSQL(CREATE_STORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY);
        onCreate(db);
    }
    public long insertBook(String title, String author, String description, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CONTENT, content);

        return db.insert(TABLE_STORY, null, values);

    }
    public List<Story> getAllStories() {
        List<Story> storyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STORY;
        // Mở kết nối với cơ sở dữ liệu
        SQLiteDatabase db = getReadableDatabase();

        // Thực hiện truy vấn để lấy danh sách truyện
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Kiểm tra xem có dữ liệu trả về từ truy vấn hay không
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Đọc dữ liệu từ con trỏ và tạo đối tượng truyện
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));

                Story book = new Story( title, author, description, content);

                // Thêm đối tượng truyện vào danh sách
                storyList.add(book);
            } while (cursor.moveToNext());
        }

        // Đóng con trỏ và đóng kết nối với cơ sở dữ liệu
        cursor.close();
        db.close();

        return storyList;
    }
}
