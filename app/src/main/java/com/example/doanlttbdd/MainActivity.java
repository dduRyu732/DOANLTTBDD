package com.example.doanlttbdd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView storyRecyclerView;
    private DatabaseHelper databaseHelper;
    private ImageButton buttonAccountInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        BookAdapter bookAdapter = new BookAdapter();
        List<story> BookList = databaseHelper.getAllStories();
        bookAdapter.setBookList(BookList);
        storyRecyclerView = findViewById(R.id.recycler_view_stories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        storyRecyclerView.setLayoutManager(layoutManager);
        Log.d("MainActivity","Số Lượng Truyện: "+ BookList.size());

        storyRecyclerView = findViewById(R.id.recycler_view_stories);
        storyRecyclerView.setAdapter(bookAdapter);

        buttonAccountInfo = findViewById(R.id.buttonAccountInfo);
        buttonAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intentInfo = new Intent(MainActivity.this, AccountInfoActivity.class );
               startActivity(intentInfo);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        // Chuyển đến MainActivity (trang chủ)
                        if (!(MainActivity.this instanceof MainActivity)) {
                            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                            finish(); // Đóng Activity hiện tại
                        }

                        return true;
                    case R.id.menu_account:
                        // Chuyển đến LoginActivity (màn hình đăng nhập)
                        Intent accountIntent = new Intent(MainActivity.this, LoginActivity.class);
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
    }
    public static class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
        private List<story> bookList;

        public void setBookList(List<story> bookList) {
            this.bookList = bookList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_story, parent, false);
            return new BookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
            story book = bookList.get(position);
            holder.bind(book);
        }

        @Override
        public int getItemCount() {
            return bookList != null ? bookList.size() : 0;
        }

        class BookViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewTitle;
            private TextView textViewAuthor;
            private TextView textViewDescription;

            public BookViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);
            }

            public void bind(story book) {
                textViewTitle.setText(book.getTitle());
                textViewAuthor.setText(book.getAuthor());
                textViewDescription.setText(book.getDescription());
            }
        }
    }
}