package com.example.bookclub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.bookclub.Adapters.BookAdapter;
import com.example.bookclub.models.Book;
import com.example.bookclub.models.LibraryItem;
import com.example.bookclub.net.BookClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private BookClient client;
    private ArrayList<Book> abooks;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        rvBooks = findViewById(R.id.rvBooks);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        abooks = new ArrayList<>();

        bookAdapter = new BookAdapter(this, abooks);
        bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(
                        SearchActivity.this,
                        "An item at position " + position + " clicked!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, BookDetailsActivity.class);
                Book book = abooks.get(position);
                intent.putExtra("EXTRA_BOOK", (Parcels.wrap(book)));
                startActivity(intent);

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(SearchActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_book:
                        Toast.makeText(SearchActivity.this, "Book", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_user:
                        Toast.makeText(SearchActivity.this, "User", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search:
                    default:
                        Toast.makeText(SearchActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        rvBooks.setAdapter(bookAdapter);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        //fetchBooks("Malcolm Gladwell");
    }

    private void fetchBooks(String query) {
        client = new BookClient();
        client.getBooks(query, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                try {
                    JSONArray docs;
                    if (response != null) {
                        docs = response.jsonObject.getJSONArray("docs");
                        final ArrayList<Book> books = Book.fromJson(docs);
                        abooks.clear();
                        for (Book book : books) {
                            abooks.add(book); // add book through the adapter
                        }
                        bookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String responseString, Throwable throwable) {
                Log.e(SearchActivity.class.getSimpleName(),
                        "Request failed with code " + statusCode + ". Response message: " + responseString);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                fetchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            private void filterList(String text) {
                List<Book> filteredList = new ArrayList<>();
                for (Book book : abooks) {
                    if (book.getTitle().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(book);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, getString(R.string.search_list), Toast.LENGTH_SHORT).show();
                } else {
                    bookAdapter.setFilteredList(filteredList);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            Toast.makeText(SearchActivity.this, getString(R.string.logout), Toast.LENGTH_SHORT).show();
            onClickLogout();
            return true;
        } else if (id == R.id.action_library) {
            Toast.makeText(SearchActivity.this, getString(R.string.in_library), Toast.LENGTH_SHORT).show();
            onClickLibrary();
            return true;
        } else if (id == R.id.action_scan) {
            Toast.makeText(SearchActivity.this, getString(R.string.scan), Toast.LENGTH_SHORT).show();
            onClickScan();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickScan() {
        //initialize intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(SearchActivity.this);
        //Set prompt text
        intentIntegrator.setPrompt("For flash use volume up key");
        //Set beep
        intentIntegrator.setBeepEnabled(true);
        //Locked orientation
        intentIntegrator.setOrientationLocked(true);
        //set capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //initialize scan
        intentIntegrator.initiateScan();
    }

    private void onClickLibrary() {
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }

    public void onClickLogout() {
        ParseUser.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );

        if (intentResult.getContents() != null) {
            //when result content is not null
            //initialize alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
            //set title
            builder.setTitle("Result");
            //set message
            builder.setMessage(intentResult.getContents());
            //set positive button
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("OK", intentResult.getContents());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(SearchActivity.this, getText(R.string.copied), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.not_scanned), Toast.LENGTH_SHORT).show();
        }
    }
}
