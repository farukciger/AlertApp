package com.alert.alertapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    /*Burdaki custom adapter RecylerViewdeki verileri istedğimiz formatta göstermek için yazıldı.
    * Veri tabanındaki verileri ana ekranda göstercek.*/
    private Context context;
    int imaj;
    public static RCClickInterface clickInterface;
    private Cursor mCursor;
    public CustomAdapter(Context context, Cursor cursor,RCClickInterface rcClickInterface) {
        this.clickInterface=rcClickInterface;
        this.imaj= R.drawable.baseline_alarm_24;
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
        int index3=mCursor.getColumnIndex("id");
        String baslik=mCursor.getString(index);
        String aciklama=mCursor.getString(index2);
        String id=mCursor.getString(index3);
        holder.textViewTitle.setText(baslik);
        holder.textViewSubtitle.setText(aciklama);
        holder.idtxt.setText(id);
        holder.imageView.setImageResource(imaj);
        String  item = mCursor.getString(position);
        String content = (String) holder.idtxt.getText();
    }

    @Override
    public int getItemCount() {

        return mCursor.getCount();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitle;
        public TextView textViewSubtitle;
        public   TextView idtxt;
        public ImageView imageView;
        public RCClickInterface rcClickInterface;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.icon_image);
            textViewTitle = itemView.findViewById(R.id.baslik);
            textViewSubtitle = itemView.findViewById(R.id.aciklama);
            idtxt=itemView.findViewById(R.id.txtid);
            this.rcClickInterface=clickInterface;
            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickInterface != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String content = idtxt.getText().toString();
                            clickInterface.onItemClick(content);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (clickInterface != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String content = idtxt.getText().toString();
                            try {
                                clickInterface.onItemLongClick(content);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }return true;
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}

