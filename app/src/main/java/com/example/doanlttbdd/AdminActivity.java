package com.example.doanlttbdd;

import static android.widget.Toast.makeText;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextId, editTextAuthor, editTextDescription, editTextContent ;
    private int selectedBookPosition;
    private ListView listViewBooks;
    private Button buttonAdd, buttonDelete;
    private BookAdapter bookAdapter;
    private BottomNavigationView bottomNavigationView;

    private DatabaseHelper databaseHelper;
    private List<Story> bookList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        editTextId = findViewById(R.id.editTextId);
        listViewBooks = findViewById(R.id.list_view_stories);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextContent = findViewById(R.id.editTextContent);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);
        databaseHelper = new DatabaseHelper(this);
        bookList = databaseHelper.getAllStories();
        bookAdapter = new BookAdapter(this, new ArrayList<>());
        listViewBooks.setAdapter(bookAdapter);
        updateBookList();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String idString = editTextId.getText().toString();
                String author = editTextAuthor.getText().toString();
                String description = editTextDescription.getText().toString();
                String content = editTextContent.getText().toString();

                if (!TextUtils.isEmpty(idString)) {
                    long id = Long.parseLong(idString); // Chuyển đổi idString thành kiểu long

                    long result = databaseHelper.insertBook(id, title, author, description, content);

                    if (result != -1) {
                        Story story = new Story();
                        story.setId((int) id); // Gắn giá trị id vào đối tượng Story

                        // Tiếp tục xử lý với đối tượng Story

                        makeText(AdminActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                        editTextId.setText("");
                        editTextTitle.setText("");
                        editTextAuthor.setText("");
                        editTextDescription.setText("");
                        editTextContent.setText("");
                        updateBookList();
                    } else {
                        makeText(AdminActivity.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    makeText(AdminActivity.this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString = editTextId.getText().toString();
                if (!TextUtils.isEmpty(idString)) {
                    long storyId = Long.parseLong(idString);
                    boolean deleted = databaseHelper.deleteBook(storyId);
                    if (deleted) {
                        // Xóa thành công, cập nhật lại danh sách truyện
                        updateBookList();
                        Toast.makeText(AdminActivity.this, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        // Chuyển đến MainActivity (trang chủ)

                            Intent homeIntent = new Intent(AdminActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                            finish(); // Đóng Activity hiện tại


                        return true;
                    case R.id.menu_account:
                        // Chuyển đến LoginActivity (màn hình đăng nhập)
                        Intent accountIntent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(accountIntent);
                        return true;
                    case R.id.menu_setting:
                        // Chỉnh đổi chế độ ban đêm/ngày
                        int nightMode = AppCompatDelegate.getDefaultNightMode();
                        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        recreate();
                        return true;
                }
                return false;
            }
        });
         updateBookList();
    }
    private void updateBookList() {
        // Lấy danh sách truyện từ cơ sở dữ liệu
        List<Story> bookList = databaseHelper.getAllStories();

        // Cập nhật danh sách truyện trong BookAdapter
        bookAdapter.setBookList(bookList);

        // Cập nhật hiển thị ListView
        bookAdapter.notifyDataSetChanged();
    }
    public static class BookAdapter extends ArrayAdapter<Story> {
        private List<Story> bookList;
        private Context context;

        public BookAdapter(Context context, List<Story> bookList) {
            super(context, 0, bookList);
            this.context = context;
            this.bookList = bookList;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_story, parent, false);
            }

            Story book = bookList.get(position);
            TextView textViewTitle = view.findViewById(R.id.textViewTitle);
            TextView textViewAuthor = view.findViewById(R.id.textViewAuthor);
            TextView textViewDescription = view.findViewById(R.id.textViewDescription);

            textViewTitle.setText(book.getTitle());
            textViewAuthor.setText(book.getAuthor());
            textViewDescription.setText(book.getDescription());

            return view;
        }

        @Override
        public int getCount() {
            return bookList != null ? bookList.size() : 0;
        }

        public List<Story> getBookList() {
            return bookList;
        }
        public void setBookList(List<Story> bookList) {
            this.bookList = bookList;
        }
    }
}
