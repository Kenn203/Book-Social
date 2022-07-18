package com.example.bookclub.Adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookclub.R;
import com.example.bookclub.models.LibraryItem;
import com.example.bookclub.utils.ItemTouchHelperAdapter;

import java.util.List;

public class LibraryItemAdapter extends RecyclerView.Adapter<LibraryItemAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private List<LibraryItem> mLibraryItemList;
    private ItemTouchHelper mTouchHelper;

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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        LibraryItem fromLibraryItem = mLibraryItemList.get(fromPosition);
        mLibraryItemList.remove(fromLibraryItem);
        mLibraryItemList.add(toPosition, fromLibraryItem);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        mLibraryItemList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        private TextView mTvAuthor;
        private TextView mTvTitle;
        private ImageView mIvBookCover;
        private GestureDetector mGestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAuthor = itemView.findViewById(R.id.tv_author);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mIvBookCover = itemView.findViewById(R.id.iv_book_cover);
            mGestureDetector = new GestureDetector(itemView.getContext(),  this);
            itemView.setOnTouchListener(this);
        }

        public void bind(LibraryItem item) {
            mTvAuthor.setText(item.getAuthorName());
            mTvTitle.setText(item.getBookTitle());
            String image = item.getCoverUrl();

            if (image != null) {
                Glide.with(mContext).load(image).into(mIvBookCover);
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }
}
