package com.ldb.bin.newapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by Bin on 09/13/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private Context context;
    private int layout;
    private ArrayList<ListId> listIds;
    private ArrayList<ArrayList> listArray;
    private String TAG = MainActivity.class.getSimpleName();

    private String url;
    private Activity activity;

    public ListAdapter(Context context, int layout, ArrayList<ListId> listIds, ArrayList<ArrayList> listArray, String url) {
        this.context = context;
        this.layout = layout;
        this.listIds = listIds;
        this.listArray = listArray;
        this.url = url;
    }


    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.dong_list_recycler,parent,false);

        return new ListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {

        ListId subnavigation = listIds.get(position);
        final ArrayList<HinhAnh> arr;
        arr = listArray.get(position);
        holder.recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.txtView.setText(subnavigation.getName());
        holder.recyclerView.setLayoutManager(layoutManager);
//        Log.e(TAG,"Xem array coi" + arr.get(i).getTen());
        ShopAdapter shopAdapter = new ShopAdapter(context,arr);
        holder.recyclerView.setAdapter(shopAdapter);
        holder.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, holder.recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String data_list = arr.get(position).getData();
                        try {
                            JSONObject jsonObject = new JSONObject(data_list);
                            Intent intent = new Intent(context,InfoFilm.class);
                            intent.putExtra("url", url);
                            intent.putExtra("href","/"+jsonObject.getString("type")+"/"+jsonObject.getInt("id"));
                            activity = (Activity) context;
                            activity.startActivity(intent);
                        } catch (JSONException e) {
                            Toast.makeText(context,"Không tìm thấy dữ liệu ",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        String data_list = arr.get(position).getData();
                        try {
                            JSONObject jsonObject = new JSONObject(data_list);
                        } catch (JSONException e) {
                            Toast.makeText(context,"Không tìm thấy dữ liệu ",Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );

    }

    @Override
    public int getItemCount() {
        return listIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        TextView txtView;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycleview);
            txtView = (TextView) itemView.findViewById(R.id.textrecycle);
        }
    }

}
