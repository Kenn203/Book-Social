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
    private Context mContext;
    private List<LibraryItem> mLibraryItemList;

    public LibraryItemAdapter(Context context, List<LibraryItem> libraryItemList) {
        this.mContext = context;
        this.mLibraryItemList = libraryItemList;
    }

    @NonNull
    @Override
    public LibraryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryItemAdapter.ViewHolder holder, int position) {
        LibraryItem libraryItem = mLibraryItemList.get(position);
        holder.bind(libraryItem);
    }

    @Override
    public int getItemCount() {
        return mLibraryItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvAuthor;
        private TextView mTvTitle;
        private ImageView mIvBookCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAuthor = itemView.findViewById(R.id.tvAuthor);
            mTvTitle = itemView.findViewById(R.id.tvTitle);
            mIvBookCover = itemView.findViewById(R.id.ivBookCover);
        }

        public void bind(LibraryItem item) {
            mTvAuthor.setText(item.getAuthorName());
            mTvTitle.setText(item.getBookTitle());
            String image = item.getCoverUrl();

            if (image != null) {
                Glide.with(mContext).load(image).into(mIvBookCover);
            }
        }
    }
}
