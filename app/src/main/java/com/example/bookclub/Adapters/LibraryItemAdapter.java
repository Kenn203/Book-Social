package com.example.bookclub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookclub.R;
import com.example.bookclub.models.LibraryItem;

import java.util.List;

public class LibraryItemAdapter extends RecyclerView.Adapter<LibraryItemAdapter.ViewHolder> {
    private Context context;
    private List<LibraryItem> libraryItemList;

    public LibraryItemAdapter(Context context, List<LibraryItem> libraryItemList){
        this.context = context;
        this.libraryItemList = libraryItemList;
    }

    @NonNull
    @Override
    public LibraryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_library, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LibraryItemAdapter.ViewHolder holder, int position) {
        LibraryItem libraryItem = libraryItemList.get(position);
        holder.bind(libraryItem);

    }

    @Override
    public int getItemCount() {return libraryItemList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAuthor;
        private TextView tvTitle;
        private ImageView ivBookCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
        }

        public void bind(LibraryItem item){
            tvAuthor.setText(item.getAuthorName());
            tvTitle.setText(item.getBookTitle());
            String image = item.getCoverUrl();

            if (image != null){
                Glide.with(context).load(image).into(ivBookCover);
            }
        }
    }
}
