package com.example.bookclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bookclub.Adapters.LibraryItemAdapter;
import com.example.bookclub.models.LibraryItem;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {
    private RecyclerView rvLibraryItem;
    protected LibraryItemAdapter mLibraryItemAdapter;
    protected List<LibraryItem> mAllLibraryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_item);


        rvLibraryItem = findViewById(R.id.rvLibraryItem);
        mAllLibraryItems = new ArrayList<>();
        mLibraryItemAdapter =  new LibraryItemAdapter(this, mAllLibraryItems);
        rvLibraryItem.setAdapter(mLibraryItemAdapter);
        rvLibraryItem.setLayoutManager(new LinearLayoutManager(this));
        populateLibraryItems();
    }

    private void populateLibraryItems() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        JSONArray itemsInLibrary = currentUser.getJSONArray("LibraryItem");
        if (itemsInLibrary == null) {
            return;
        }
        for (int i = 0; i < itemsInLibrary.length(); i++) {
            try {
                JSONObject libraryItem = (JSONObject) itemsInLibrary.get(i);
                String itemId = (String) libraryItem.get("objectId");
                queryLibraryitems(itemId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void queryLibraryitems(String itemId) {
        ParseQuery<LibraryItem> query = ParseQuery.getQuery(LibraryItem.class);
        query.addDescendingOrder("createdAt");

        query.getInBackground(itemId, new GetCallback<LibraryItem>() {
            @Override
            public void done(LibraryItem books, ParseException e) {
                if (e == null) {
                    mAllLibraryItems.add(books);
                    mLibraryItemAdapter.notifyDataSetChanged();
                } else {
                    Log.d("LibraryActivity", "Check if all books are added");
                }
            }
        });
    }
}
