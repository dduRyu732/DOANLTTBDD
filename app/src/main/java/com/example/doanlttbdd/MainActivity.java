package com.example.doanlttbdd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView storyListView;
    private DatabaseHelper databaseHelper;
    private ImageButton buttonAccountInfo;



    ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = findViewById(R.id.viewflipper);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        BookAdapter bookAdapter = new BookAdapter(MainActivity.this);
        List<Story> bookList = databaseHelper.getAllStories();
        bookAdapter.setBookList(bookList);
        storyListView = findViewById(R.id.list_view_stories);
        storyListView.setAdapter(bookAdapter);


        buttonAccountInfo = findViewById(R.id.buttonAccountInfo);
        buttonAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInfo = new Intent(MainActivity.this, AccountInfoActivity.class);
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

    public static class BookAdapter extends ArrayAdapter<Story> {
        private List<Story> bookList;
        private Context context;

        public BookAdapter(Context context) {
            super(context, 0);
            this.context = context;
        }

        public void setBookList(List<Story> bookList) {
            this.bookList = bookList;
            notifyDataSetChanged();
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
    }

}