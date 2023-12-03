package com.example.alertapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecylerCustomAdapter extends RecyclerView.Adapter<RecylerCustomAdapter.ViewvHolder> {
    Context context;
    RClickInterface clickInterface;
    ArrayList<Records> record=new ArrayList<>();

    public RecylerCustomAdapter(Context context, RClickInterface clickInterface, ArrayList<Records> record) {
        this.context = context;
        this.clickInterface = clickInterface;
        this.record = record;
    }

    @NonNull
    @Override
    public ViewvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        ViewvHolder viewvHolder=new ViewvHolder(view);
        return viewvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewvHolder holder, int position) {
        holder.imaj.setImageResource(record.get(position).imaj);
        holder.baslik.setText(record.get(position).baslik);
        holder.aciklama.setText(record.get(position).aciklama);
    }

    @Override
    public int getItemCount() {
        return record.size();
    }

    public class ViewvHolder extends RecyclerView.ViewHolder{
        ImageView imaj;
        TextView baslik;
        TextView aciklama;
        LinearLayout parentLayout;
        public ViewvHolder(@NonNull View itemView) {
            super(itemView);
            imaj=itemView.findViewById(R.id.icon_image);
            baslik=itemView.findViewById(R.id.baslik);
            aciklama=itemView.findViewById(R.id.aciklama);
            parentLayout=itemView.findViewById(R.id.parentLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickInterface.onItemClick(getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    clickInterface.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });

        }
    }
}
