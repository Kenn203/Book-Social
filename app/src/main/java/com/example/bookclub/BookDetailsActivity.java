package com.example.bookclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookclub.models.Book;
import com.example.bookclub.utils.Constants;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BookDetailsActivity extends AppCompatActivity {
    private Book book ;
    private ImageView mBookImg;
    private TextView mBookTitle;
    private TextView mBookAuthor;
    private TextView mBookPublisher;
    private TextView mBookPages;
    private FloatingActionButton shareButton;
    private FloatingActionButton viewButton;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        queue = Volley.newRequestQueue(this);

        mBookImg = findViewById(R.id.mBookImg);
        mBookTitle = findViewById(R.id.mTitleID);
        mBookAuthor = findViewById(R.id.mAuthorID);
        mBookPublisher = findViewById(R.id.mPublishedBy);
        mBookPages = findViewById(R.id.mPages);

        shareButton = findViewById(R.id.mShareID);
        viewButton = findViewById(R.id.mViewID);

        shareButton.setOnClickListener((v -> {
            shareIntent();
        }));
        viewButton.setOnClickListener((v -> {
            viewIntent(book.getBookIMDB());
        }));

        book = (Book) Parcels.unwrap(getIntent().getParcelableExtra("EXTRA_BOOK"));
        Log.d("BookDetailsActivity","Book : " + book);
        String bookID = book.getBookIMDB();

        Picasso.get()
                .load(book.getCoverUrl())
                .placeholder(R.drawable.cover_image)
                .into(mBookImg);

        mBookTitle.setText(book.getTitle());
        mBookAuthor.setText(book.getAuthor());

        getBookDetails(bookID);
    }

    public void getBookDetails(String id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.BASE_LEFT_URL + id + Constants.BASE_RIGHT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.has("publishers")) {
                            try {
                                JSONArray publishersArray = response.getJSONArray("publishers");
                                int arrayLength = publishersArray.length();
                                String[] publishers = new String[arrayLength];
                                for (int i = 0; i < arrayLength; i++) {
                                    publishers[i] = publishersArray.getString(i);
                                }
                                mBookPublisher.setText(TextUtils.join(",", publishers));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mBookPublisher.setText(getString(R.string.publisher_na));
                        }

                        if (response.has("number_of_pages")) {
                            try {
                                mBookPages.setText(String.format("%s Pages", Integer.toString(response.getInt("number_of_pages"))));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mBookPages.setText(getString(R.string.pages_na));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);

    }

    private void shareIntent() {
        ImageView shareImage = (ImageView) findViewById(R.id.mBookImg);
        TextView shareTitle = (TextView) findViewById(R.id.mTitleID);

        Uri bmpUri = getLocalBitmapUri(shareImage);
        // Construct a ShareIntent with link to image
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, (String) shareTitle.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        //Launch share menu
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    private Uri getLocalBitmapUri(ImageView shareImage) {
        //Extract Bitmap from ImageView drawable
        Drawable drawable = shareImage.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) shareImage.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.close();
            bmpUri = FileProvider.getUriForFile(BookDetailsActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void viewIntent(final String id) {
        Intent intent = new Intent(BookDetailsActivity.this, LibraryActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            Toast.makeText(BookDetailsActivity.this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
            onLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLogout() {
        ParseUser.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}