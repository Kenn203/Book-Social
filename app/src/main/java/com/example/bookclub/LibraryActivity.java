package com.example.bookclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
    protected LibraryItemAdapter libraryItemAdapter;
    protected List<LibraryItem> allLibraryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_item);


        rvLibraryItem = findViewById(R.id.rvLibraryItem);
        allLibraryItems = new ArrayList<>();
        libraryItemAdapter = new LibraryItemAdapter(this, allLibraryItems);
        rvLibraryItem.setAdapter(libraryItemAdapter);
        rvLibraryItem.setLayoutManager(new LinearLayoutManager(this));
        populateLibraryItem();
    }

    private void populateLibraryItem(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        JSONArray itemsInLibrary = currentUser.getJSONArray("LibraryItem");
        if(itemsInLibrary == null){
            return;
        }
        for (int i = 0; i < itemsInLibrary.length(); i++){
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
                if(e == null){
                    allLibraryItems.add(books);
                    libraryItemAdapter.notifyDataSetChanged();
                }
                else{

                }
            }
        });
    }
}
