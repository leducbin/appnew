package com.ldb.bin.newapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Episodes> data;
    private int i;
    Activity activity;

    public RecyclerViewAdapter(Context context, ArrayList<Episodes> data, int i) {
        this.context = context;
        this.data = data;
        this.i = i;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.dong_epi,parent,false);

        return new RecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(Integer.toString(data.get(position).getNumber()));
        holder.textView.setTextSize(20);
        if(position == i){
            holder.textView.setBackgroundResource(R.drawable.border_select);
        }
            else
        {
            holder.textView.setBackgroundResource(0);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_ep);

        }
    }
}