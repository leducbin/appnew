package com.ldb.bin.newapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.R.attr.value;

public class SearchType extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = MainActivity.class.getSimpleName();
    NavigationView naviView;
    RecyclerView listMenu;
    Toolbar menuToolbar;
    DrawerLayout drawerLayout;
    ProgressDialog pDialog;
    TextView textViewdefault,textView,textView2,textViewmanhinh;
    ImageView imageView,imageView_search;
    Button buttonLogin;
    GridView gridView;
    SharedPreferences sharedPreferences;
    View foot_view;
    Boolean isLoading = false;
    Handler_m handler_m;
    ArrayList<Related> arrayList_list = new ArrayList<Related>();
    int page = 1;
    GridViewAdapter gridViewAdapter = new GridViewAdapter(SearchType.this,R.layout.dong_grid_view);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmanhinhchinh);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navi_menu);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView buttonLogin = (TextView) hView.findViewById(R.id.SignIn);


        AnhXa();

        Intent intent = getIntent();
        String offerings = intent.getStringExtra("offerings");
        String category = intent.getStringExtra("category");
        String genre = intent.getStringExtra("genre");
        String url_menu = intent.getStringExtra("url");
        try {
            final String url_search =
                    "http://api.danet.vn/products/search?types="+category + "&genres="+
                            URLEncoder.encode(genre, "utf-8").toString()+"&offerings=" +offerings +"&page=";
            handler_m.setUrl(url_search);
            Log.e(TAG,url_search+"");
            GetContacts search_type = new GetContacts();
            search_type.setURL(url_menu);
            search_type.setPages(page);
            Log.e(TAG,"data url" + url_menu);
            search_type.setUrl_search(url_search);
            search_type.setUrl_title(url_menu);
            search_type.execute();
        } catch (UnsupportedEncodingException e) {

        }



    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void AnhXa() {
        naviView = (NavigationView) findViewById(R.id.navi_menu);
        textViewdefault = (TextView) findViewById(R.id.railgo);
        textViewmanhinh = (TextView) findViewById(R.id.txtbar);
        buttonLogin = (Button) findViewById(R.id.login);
        textView = (TextView) findViewById(R.id.railcine);
        textView2 = (TextView) findViewById(R.id.railbuffet);
        imageView_search = (ImageView) findViewById(R.id.search_info_film);
        gridView = (GridView) findViewById(R.id.gridview_search);
        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        foot_view = inflater.inflate(R.layout.footer_view,null);
        listMenu = (RecyclerView) findViewById(R.id.listView_nav);
        handler_m = new Handler_m();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.railgo) {
            Toast.makeText(SearchType.this,"aaaaaaaaaaaaaaaaaaa",Toast.LENGTH_LONG).show();
        } else if (id == R.id.railbuffet) {

        } else if (id == R.id.railcine) {

        } else if (id == R.id.listview_menu) {

        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void ActionBar()
//    {
//        imageView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view)
//            {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
//    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        private String url,url_search;
        private String reponse,reponse_search;
        private String url_title;
        private Integer pages;

        public Integer getPages() {
            return pages;
        }

        public void setPages(Integer pages) {
            this.pages = pages;
        }

        public String getUrl_search() {
            return url_search;
        }

        public void setUrl_search(String url_search) {
            this.url_search = url_search;
        }

        public String getUrl_title() {
            return url_title;
        }

        public void setUrl_title(String url_title) {
            this.url_title = url_title;
        }

        public void setURL(String url){
            this.url = url;
        }
        public String getURL(){
            return this.url;
        }


        @Override
        protected void onPreExecute() {
            if (pages == 1)
            {
            pDialog = new ProgressDialog(SearchType.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (pages == 1)
            {

            HttpHandler sh = new HttpHandler(SearchType.this);
            // Making a request to url and getting response
            this.reponse = sh.makeServiceCall(this.url);
            }
            Log.e(TAG,"data " +reponse );
            HttpHandler sh_search = new HttpHandler(SearchType.this);
            // Making a request to url and getting response
            this.url_search = this.url_search + pages +"&limit=12";
            this.reponse_search = sh_search.makeServiceCall(this.url_search);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (pages == 1)
                {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    JSONObject jsonReponse = new JSONObject(this.reponse);
                    JSONArray data = jsonReponse.getJSONArray("data");
                    Log.e(TAG,"data reponse " + data);
                    final ArrayList<Subnavigation> listNavigation =  new ArrayList<Subnavigation>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        String type = item.getString("type");
                        if(type.equals("subnavigation")){
                            // the same process with carousel
                            JSONArray subnavigations = item.getJSONArray("items");
                            for (int k = 0; k < subnavigations.length(); k++) {
                                JSONObject subnavigation  = subnavigations.getJSONObject(k);
                                String title = subnavigation.getString("title");
                                Subnavigation tmp = new Subnavigation();
                                tmp.setName(title);
                                tmp.setData(subnavigation.toString());
                                listNavigation.add(tmp);
                            }
                            SubnavigationAdapter subadapter = new SubnavigationAdapter(SearchType.this,R.layout.dong_menu,listNavigation);
                            listMenu.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchType.this,LinearLayoutManager.VERTICAL,false);
                            listMenu.setLayoutManager(layoutManager);
                            listMenu.setAdapter(subadapter);
                            listMenu.addOnItemTouchListener(
                                    new RecyclerItemClickListener(SearchType.this, listMenu ,new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override public void onItemClick(View view, int position) {
                                            String data_list = listNavigation.get(position).getData();
                                            try {
                                                Intent intent = new Intent(SearchType.this,SearchType.class);
                                                JSONObject jsonObject_sub = new JSONObject(listNavigation.get(position).getData());
                                                intent.putExtra("offerings",jsonObject_sub.getString("offering"));
                                                intent.putExtra("category",jsonObject_sub.getString("category"));
                                                intent.putExtra("genre",jsonObject_sub.getString("genre"));
                                                intent.putExtra("url",url);
                                                Log.e(TAG,"data qq "+  url);
                                                overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                                                SearchType.this.startActivity(intent);
//                                                    Toast.makeText(MainActivity.this,"Đang ở đây" + url_title,Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                Toast.makeText(SearchType.this,"Không tìm thấy dữ liệu ",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override public void onLongItemClick(View view, int position) {

                                        }
                                    })
                            );
//                            listMenu.setAdapter(subadapter);
//                            listMenu.addOnItemTouchListener(
//                                    new RecyclerItemClickListener(SearchType.this, listMenu ,new RecyclerItemClickListener.OnItemClickListener() {
//                                        @Override public void onItemClick(View view, int position) {
//                                            String data_list = listNavigation.get(position).getData();
//                                            try {
//                                                JSONObject jsonObject_sub = new JSONObject(listNavigation.get(position).getData());
//                                                Intent intent_searchtype = new Intent(SearchType.this,SearchType.class);
//                                                intent_searchtype.putExtra("offerings",jsonObject_sub.getString("offering"));
//                                                intent_searchtype.putExtra("category",jsonObject_sub.getString("category"));
//                                                intent_searchtype.putExtra("genre",jsonObject_sub.getString("genre"));
//                                                intent_searchtype.putExtra("url",url_title);
//                                                SearchType.this.startActivity(intent_searchtype);
//                                                overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
//                                            } catch (JSONException e) {
//                                                Toast.makeText(SearchType.this,"Không tìm thấy dữ liệu ",Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//
//                                        @Override public void onLongItemClick(View view, int position) {
//
//                                        }
//                                    })
//                            );
//

                        }
                    }

                }

                if (reponse_search != null && reponse_search.length() !=0 && pages == 1)
                {
                    JSONObject jsonObject_list = new JSONObject(reponse_search);
                    JSONArray jsonArray_list = jsonObject_list.getJSONArray("data");
                    for (int k =0;k<jsonArray_list.length();k++)
                    {
                        JSONObject data_list = jsonArray_list.getJSONObject(k);
                        Related tmp = new Related();
                        tmp.setHinhanh(data_list.getJSONObject("image").getString("base_uri"));
                        tmp.setData(data_list.toString());
                        arrayList_list.add(tmp);
                    }
                    gridViewAdapter.setData(arrayList_list);
                    gridView.setAdapter(gridViewAdapter);

                }else
                {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    JSONObject jsonObject_list = new JSONObject(reponse_search);
                    JSONArray jsonArray_list = jsonObject_list.getJSONArray("data");
                    for (int k =0;k<jsonArray_list.length();k++)
                    {
                        JSONObject data_list = jsonArray_list.getJSONObject(k);
                        Related tmp = new Related();
                        tmp.setHinhanh(data_list.getJSONObject("image").getString("base_uri"));
                        tmp.setData(data_list.toString());
                        arrayList_list.add(tmp);
                    }

                    gridViewAdapter.notifyDataSetChanged();
                }
                gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (view.getLastVisiblePosition() == totalItemCount - 1 && totalItemCount != 0 && isLoading == false)
                        {
                            isLoading = true;
                            ThreadData threadData = new ThreadData();
                            threadData.start();
                        }
                    }
                });


            } catch (JSONException e) {
                Toast.makeText(SearchType.this,"Lỗi rồi 5000 anh em ey ", Toast.LENGTH_LONG).show();
            }



        }
    }

    public class Handler_m extends Handler {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    pDialog = new ProgressDialog(SearchType.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    break;
                case 1:
                    GetContacts getContacts = new GetContacts();
                    getContacts.setPages(++page);
                    getContacts.setUrl_search(url);
                    getContacts.execute();
                    isLoading = false;
                    break;
            }
        }


    }
    public class ThreadData extends Thread {
        @Override
        public void run() {
            super.run();
            handler_m.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = handler_m.obtainMessage(1);
            handler_m.sendMessage(message);
        }
    }

}
