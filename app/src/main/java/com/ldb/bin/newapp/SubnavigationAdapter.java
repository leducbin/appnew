package com.ldb.bin.newapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by Bin on 09/12/2017.
 */

public class SubnavigationAdapter extends  RecyclerView.Adapter<SubnavigationAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private ArrayList<Subnavigation> subnavigationArrayList;

    public SubnavigationAdapter(Context context, int layout, ArrayList<Subnavigation> subnavigationArrayList) {
        this.context = context;
        this.layout = layout;
        this.subnavigationArrayList = subnavigationArrayList;
    }

    @Override
    public SubnavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.dong_menu,parent,false);

        return new SubnavigationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(subnavigationArrayList.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return subnavigationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txtMenu);

        }
    }
}
