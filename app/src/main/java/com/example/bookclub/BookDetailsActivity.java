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
import android.view.View;
import android.widget.Button;
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
import com.example.bookclub.models.LibraryItem;
import com.example.bookclub.utils.Constants;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BookDetailsActivity extends AppCompatActivity {
    private Book book;
    private ImageView mBookImg;
    private TextView mBookTitle;
    private TextView mBookAuthor;
    private TextView mBookPublisher;
    private TextView mBookPages;
    private Button mAddToLibrary;
    private FloatingActionButton mShareButton;
    private FloatingActionButton mViewButton;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        queue = Volley.newRequestQueue(this);

        mAddToLibrary = findViewById(R.id.tvAddToLibrary);
        mBookImg = findViewById(R.id.ivBookImg);
        mBookTitle = findViewById(R.id.tvTitleID);
        mBookAuthor = findViewById(R.id.tvAuthorID);
        mBookPublisher = findViewById(R.id.tvPublishedBy);
        mBookPages = findViewById(R.id.tvPages);

        mAddToLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                LibraryItem libraryItem = new LibraryItem(book);
                libraryItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(BookDetailsActivity.this, "Adding to library unsuccessful", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentUser.add("LibraryItem", libraryItem);
                        currentUser.saveInBackground();
                        Toast.makeText(BookDetailsActivity.this, "Adding to library successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        //Floating Action Buttons
        mShareButton = findViewById(R.id.mShareID);
        mViewButton = findViewById(R.id.mViewID);

        mShareButton.setOnClickListener((v -> {
            shareIntent();
        }));
        mViewButton.setOnClickListener((v -> {
            Log.d("BookDetailsActivity", "Checking for ISBN " + book.getISBN());
            launchViewIntent(book.getISBN());
        }));

        //Get the book intent
        book = (Book) Parcels.unwrap(getIntent().getParcelableExtra("EXTRA_BOOK"));
        Integer bookPages = book.getPages();
        String publisher = book.getPublisher();

        Picasso.get()
                .load(book.getCoverUrl())
                .placeholder(R.drawable.ic_nocover)
                .into(mBookImg);

        mBookTitle.setText(book.getTitle());
        mBookAuthor.setText(book.getAuthor());
        String pages = Integer.toString(bookPages);
        mBookPages.setText(pages);
        mBookPublisher.setText(publisher);
    }

    private void shareIntent() {
        ImageView shareImage = (ImageView) findViewById(R.id.ivBookImg);
        TextView shareTitle = (TextView) findViewById(R.id.tvTitleID);

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

    public void launchViewIntent(final String id) {
        Intent intent = new Intent(BookDetailsActivity.this, WebViewActivity.class);
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