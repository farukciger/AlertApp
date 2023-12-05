package com.example.alertapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    int imaj;
    private Cursor mCursor;
    private ArrayList<String> titles;
    private ArrayList<String> subtitles;
    public CustomAdapter(Context context, Cursor cursor) {
        this.imaj=R.drawable.baseline_alarm_24;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        int index=mCursor.getColumnIndex("baslik");
        int index2=mCursor.getColumnIndex("aciklama");
        String baslik=mCursor.getString(index);
        String aciklama=mCursor.getString(index2);
        holder.textViewTitle.setText(baslik);
        holder.textViewSubtitle.setText(aciklama);
        holder.imageView.setImageResource(imaj);
    }

    @Override
    public int getItemCount() {

        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewSubtitle;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.icon_image);
            textViewTitle = itemView.findViewById(R.id.baslik);
            textViewSubtitle = itemView.findViewById(R.id.aciklama);
        }
    }
}

