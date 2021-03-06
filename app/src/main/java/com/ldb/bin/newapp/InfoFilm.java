package com.ldb.bin.newapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InfoFilm extends AppCompatActivity {
    public static final String railURL = "http://api.danet.vn/data/rails/go";
    public static final String railCineURL = "http://api.danet.vn/data/rails/cineplex";
    public static final String railBuffURL = "http://api.danet.vn/data/rails/buffet";
    private String TAG = MainActivity.class.getSimpleName();
    ImageView videoView,bginfo;
    RelativeLayout listView;
    Toolbar toolbar;
    ImageView imageView,image_video;
    TextView textView;
    ProgressDialog pDialog;
    int REQUEST_CODE_EDIT = 123;
    int REQUEST_LOGOUT = 234;
    CoordinatorLayout drawerLayout;
    RelativeLayout relativeLayout;
    TextView txttitle,txtclassification,txtdescription,txtgenres,txtactors,txtlanguage,textView_epi;
    RecyclerView recyclerView,recyclerView_ep,recyclerView_related;
    LinearLayout linearLayout;
    ScrollView scrollView;
    private PopupWindow popWindow;
    FloatingActionButton commentBtn;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_film);
        AnhXa();
        Intent intent = getIntent();
        String href = intent.getStringExtra("href");
        Log.e(TAG,"href " +href);
        String url = intent.getStringExtra("url");
        Log.e(TAG,"url " + url);
        switch (url){
            case railURL:
                textView.setText("MIỄN PHÍ");
                break;
            case railBuffURL:
                textView.setText("PHIM GÓI");
                break;
            case railCineURL:
                textView.setText("THUÊ PHIM");
                break;
            case "AVOD":
                textView.setText("MIỄN PHÍ");
                break;
            case "SVOD":
                textView.setText("PHIM GÓI");
                break;
            case "TVOD":
                textView.setText("THUÊ PHIM");
                break;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // inflate the custom popup layout
                View inflatedView = layoutInflater.inflate(R.layout.popup_layout, null,false);
                // find the ListView in the popup layout
                ListView listView = (ListView)inflatedView.findViewById(R.id.commentsListView);
                LinearLayout headerView = (LinearLayout)inflatedView.findViewById(R.id.headerLayout);

                // get device size
                Display display = getWindowManager().getDefaultDisplay();
                final Point size = new Point();
                display.getSize(size);
//        mDeviceHeight = size.y;
                DisplayMetrics displayMetrics = InfoFilm.this.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;


                // fill the data to the list items
                setSimpleList(listView);


                // set height depends on the device size
                popWindow = new PopupWindow(inflatedView, width,height-50, true );
                // set a background drawable with rounders corners
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.fb_popup_bg));

                popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

                popWindow.setAnimationStyle(R.style.PopupAnimation);

                // show the popup at bottom of the screen and set some margin at bottom ie,
                popWindow.showAtLocation(view, Gravity.BOTTOM, 0,100);
                ImageView close = (ImageView) inflatedView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popWindow.dismiss();
                    }
                });
            }
        });


        String[] separated = href.split("/");
        Log.e(TAG,separated[1].equals("series")+"kiem tra");
        if (separated[1].equals("series"))
        {
            GetInfo getInfo = new GetInfo();
            getInfo.setUrl(separated[2]);
            getInfo.setType(separated[1]);
            getInfo.execute();
        }
        else if(separated[1].equals("movie"))
        {
            GetInfo getInfo = new GetInfo();
            getInfo.setUrl(separated[2]);
            getInfo.setType(separated[1]);
            getInfo.execute();

        }

    }
    void setSimpleList(ListView listView){

//        ArrayList<String> contactsList = new ArrayList<String>();
//
//        for (int index = 0; index < 10; index++) {
//            contactsList.add("I am @ index " + index + " today " + Calendar.getInstance().getTime().toString());
//        }
//
//        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
//                R.layout.fb_comments_list_item, android.R.id.text1,contactsList));
        ArrayList<String> contactsList = new ArrayList<String>();

        for (int index = 0; index < 10; index++) {
            contactsList.add("I am @ index " + index + " today " + Calendar.getInstance().getTime().toString());
        }

        listView.setAdapter(new ArrayAdapter<String>(InfoFilm.this,
                R.layout.fb_comments_list_item, android.R.id.text1, contactsList));
    }



    private class GetInfo extends AsyncTask<Void, Void, Void>
    {

        private String url_id,type;
        private String reponse,eps_reponse,rela_reponse;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url_id;
        }

        public void setUrl(String url) {
            this.url_id = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InfoFilm.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler(InfoFilm.this);

            // Making a request to url and getting response
            this.reponse = sh.makeServiceCall("http://api.danet.vn/products/"+this.url_id);
            Log.e(TAG,"data " + reponse);
            HttpHandler rela = new HttpHandler(InfoFilm.this);
            this.rela_reponse = rela.makeServiceCall("http://api.danet.vn/products/"+this.url_id+"/related");
            Log.e(TAG,"data 2 " + rela_reponse);
            if(type.equals("series"))
            {
                HttpHandler eps = new HttpHandler(InfoFilm.this);
                this.eps_reponse = eps.makeServiceCall("http://api.danet.vn/products/"+this.url_id+"/episodes");
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing() && reponse != null)
                pDialog.dismiss();
            try {
                JSONObject json_reponse = new JSONObject(reponse);

                JSONObject image = json_reponse.getJSONObject("image");
                JSONObject profile = image.getJSONObject("profile");
                String package_type = json_reponse.getString("package_type");
                Log.e(TAG,"package type "+ package_type);
                JSONArray poster = profile.getJSONArray("poster");
                for (int z=0;z<poster.length();z++)
                {
                    Log.e(TAG,"url " + poster.getJSONObject(z).toString());
                    String bg_info = poster.getJSONObject(z).getString("url");
                    if(poster.getJSONObject(z).getInt("width") == 600)
                    {
                        Picasso.with(InfoFilm.this).load(bg_info).into(videoView);
                        Picasso.with(InfoFilm.this).load(bg_info).into(bginfo);

                    }
                    else{

                    }
                }


                txttitle.setText( json_reponse.getString("title"));
                txttitle.setTextSize(26);
                txtclassification.setText(json_reponse.getString("released")+"("+json_reponse.getString("classification")+")");
                txtdescription.setText(json_reponse.getString("description"));
                JSONArray argenres = json_reponse.getJSONArray("genres");

                String genres = "";
                for (int i=0;i<argenres.length();i++ ){
                    genres = genres + argenres.get(i) +", ";
                    Log.e(TAG,genres);
                }
                txtgenres.setText(genres);
                JSONArray jsonArray_2 = json_reponse.getJSONArray("actors");
                String actors = "";
                for (int k = 0; k < jsonArray_2.length();k++)
                {
                    actors = actors + jsonArray_2.get(k)+ ", ";
                }


                JSONArray jsonArray_3 = json_reponse.getJSONArray("directors");
                String directors = "";
                for (int l=0; l < jsonArray_3.length();l++)
                {
                    directors = directors + jsonArray_3.get(l)+ ", ";
                }
                txtactors.setText("Diễn viên " +actors +"\n" + "Đạo diễn " + directors);
                JSONObject jsonObject_2 = json_reponse.getJSONObject("language");
                JSONArray jsonArray_4 = jsonObject_2.getJSONArray("subtitles");
                String subtitles = "";
                for (int k=0;k < jsonArray_4.length();k++)
                {
                    subtitles = subtitles + jsonArray_4.get(k).toString()+ ", ";
                }
                JSONArray jsonArray_5 = jsonObject_2.getJSONArray("audios");
                String audio = "";
                for (int l=0;l< jsonArray_5.length();l++)
                {
                    if (jsonArray_5.get(l) == null)
                    {
                        audio = "null";
                    }
                    else if(l == jsonArray_5.length() - 1)
                    {
                        audio = audio + jsonArray_5.get(l).toString();
                    }
                    else
                    {
                        audio = audio + jsonArray_5.get(l).toString()+ ", ";
                    }
                }
                txtlanguage.setText("Phụ đề " +subtitles +"\n" + "Lồng tiếng " +  audio);
                if(type.equals("series") && package_type.equals("AVOD"))
                {
                    JSONObject json_eps = new JSONObject(eps_reponse);
                    final JSONArray data = json_eps.getJSONArray("data");
                    final ArrayList<Episodes> arrayList_ep = new ArrayList<Episodes>();
                    for (int z=0;z<data.length();z++)
                    {
                        JSONObject eps_num = data.getJSONObject(z);
                        Episodes episodes = new Episodes();
                        episodes.setNumber(eps_num.getInt("episode_number"));
                        episodes.setArrayList(eps_num.toString());
                        arrayList_ep.add(episodes);
                    }
                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e(TAG, "series videoview click");
                            Intent intent = new Intent(InfoFilm.this,VideoPlay.class);
                            intent.putExtra("url",url_id);
                            try {
                                Log.e(TAG, "Start activity");
                                intent.putExtra("id",data.getJSONObject(0).getString("id"));
                                InfoFilm.this.startActivity(intent);
                                overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                            } catch (JSONException e) {
                            }

                        }
                    });
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(InfoFilm.this, LinearLayoutManager.HORIZONTAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(InfoFilm.this,arrayList_ep,0);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(InfoFilm.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(InfoFilm.this,arrayList_ep,position);
                                    recyclerView.setAdapter(recyclerViewAdapter);
                                    try {
                                        JSONObject jsonObject = new JSONObject(arrayList_ep.get(position).getArrayList());
                                        String id_video_ep = jsonObject.getString("id");
                                        Intent intent = new Intent(InfoFilm.this,VideoPlay.class);
                                        intent.putExtra("url",url_id);
                                        intent.putExtra("id",id_video_ep);
                                        Log.e(TAG,"data info "+ id_video_ep + url_id);
                                        InfoFilm.this.startActivity(intent);
                                        overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                                    } catch (JSONException e) {

                                    }

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    Toast.makeText(InfoFilm.this,position + "long length", Toast.LENGTH_LONG).show();
                                }
                            })
                    );
                }
                else if (type.equals("movie") && package_type.equals("SVOD"))
                {
//                    JSONObject json_eps = new JSONObject(eps_reponse);
//                    final JSONArray data = json_eps.getJSONArray("data");
                    textView_epi.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(sharedPreferences.getString("accessToken","").equals(""))
                            {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(InfoFilm.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(InfoFilm.this);
                                }
                                builder.setTitle("Hãy đăng nhập để xem hoặc mua phim!!!")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(InfoFilm.this, Login.class);
                                                startActivityForResult(intent,REQUEST_CODE_EDIT);
                                                overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }
                            else if(sharedPreferences.getString("accessToken","") != null)
                            {
                                final String token_user = sharedPreferences.getString("accessToken","");
                                String url_user = "http://api.danet.vn/products/"+url_id;
                                RequestQueue requestQueue = Volley.newRequestQueue(InfoFilm.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url_user, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response != null){
                                            try {
                                                JSONObject jsonObject_user = new JSONObject(response);
                                                JSONArray array_offerings = jsonObject_user.getJSONArray("offerings");
                                                int i = 0;
                                                for (int x = 0; x<array_offerings.length();x++) {
                                                    JSONObject ob_offerings = array_offerings.getJSONObject(x);
                                                    if(ob_offerings.getBoolean("entitled") == true)
                                                    {
                                                        i = 1;
                                                    }
                                                }
                                                if (i == 1)
                                                {
                                                    Toast.makeText(InfoFilm.this,"Oke men", Toast.LENGTH_LONG).show();
                                                }
                                                else if(i==0)
                                                {
                                                    AlertDialog.Builder builder;
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        builder = new AlertDialog.Builder(InfoFilm.this, android.R.style.Theme_Material_Dialog_Alert);
                                                    } else {
                                                        builder = new AlertDialog.Builder(InfoFilm.this);
                                                    }
                                                    builder.setTitle("Bạn phải mua phim! YES! Để mua...")
                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    AlertDialog.Builder builder;
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                        builder = new AlertDialog.Builder(InfoFilm.this, android.R.style.Theme_Material_Dialog_Alert);
                                                                    } else {
                                                                        builder = new AlertDialog.Builder(InfoFilm.this);
                                                                    }
                                                                    builder.setTitle("Đăng ký phim gói")
                                                                            .setPositiveButton("Bằng Điểm", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    Intent intent = new Intent(InfoFilm.this, SubscriptionsList.class);
                                                                                    startActivityForResult(intent,REQUEST_CODE_EDIT);
                                                                                    overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                                                                                }
                                                                            })
                                                                            .setNeutralButton("Bằng Tiền",new DialogInterface.OnClickListener(){

                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    Toast.makeText(InfoFilm.this,"Bằng Tiền", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            })
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            .show();
                                                                }
                                                            })
                                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // do nothing
                                                                }
                                                            })
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }
                                ){
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Movideo-Auth", token_user);
                                        return params;
                                    }
                                }
                                        ;

                                requestQueue.add(stringRequest);
                            }


                        }
                    });

                }else if (type.equals("movie") && package_type.equals("TVOD"))
                {
//                    JSONObject json_eps = new JSONObject(eps_reponse);
//                    final JSONArray data = json_eps.getJSONArray("data");
                    textView_epi.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(sharedPreferences.getString("accessToken","").equals(""))
                            {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(InfoFilm.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(InfoFilm.this);
                                }
                                builder.setTitle("Hãy đăng nhập để xem hoặc mua phim!!!")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(InfoFilm.this, Login.class);
                                                startActivityForResult(intent,REQUEST_CODE_EDIT);
                                                overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
//
                            }
                            else if(sharedPreferences.getString("accessToken","") != null)
                            {
                                final String token_user = sharedPreferences.getString("accessToken","");
                                String url_user = "http://api.danet.vn/products/"+ url_id;
                                RequestQueue requestQueue = Volley.newRequestQueue(InfoFilm.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url_user, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response != null){
                                            try {
                                                JSONObject jsonObject_user = new JSONObject(response);
                                                JSONArray array_offerings = jsonObject_user.getJSONArray("offerings");
                                                int i = 0;
                                                for (int x = 0; x<array_offerings.length();x++) {
                                                    JSONObject ob_offerings = array_offerings.getJSONObject(x);
                                                    if(ob_offerings.getBoolean("entitled") == true)
                                                    {
                                                        i = 1;
                                                    }
                                                }
                                                if (i == 1)
                                                {
                                                    Toast.makeText(InfoFilm.this,"Oke men", Toast.LENGTH_LONG).show();
                                                }
                                                else if(i==0 )
                                                {


                                                    if( Float.parseFloat(sharedPreferences.getString("credits","0")) >= Float.valueOf(array_offerings.getJSONObject(0).getString("price")) )
                                                    {
                                                        Toast.makeText(InfoFilm.this,"Đủ tiền thanh toán r!", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        AlertDialog.Builder builder;
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                            builder = new AlertDialog.Builder(InfoFilm.this, android.R.style.Theme_Material_Dialog_Alert);
                                                        } else {
                                                            builder = new AlertDialog.Builder(InfoFilm.this);
                                                        }
                                                        builder.setTitle("Bạn không đủ điểm.").setMessage("Bạn không đủ " + array_offerings.getJSONObject(0).getString("price") + " điểm để thuê phim này!")
                                                                .setPositiveButton("Nạp Điểm", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        AlertDialog.Builder builder;
                                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                            builder = new AlertDialog.Builder(InfoFilm.this, android.R.style.Theme_Material_Dialog_Alert);
                                                                        } else {
                                                                            builder = new AlertDialog.Builder(InfoFilm.this);
                                                                        }
                                                                        builder.setTitle("Nạp điểm DANET")
                                                                                .setPositiveButton("Bằng Tiền", new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        Toast.makeText(InfoFilm.this,"Bằng Tiền", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                })
                                                                                .setNeutralButton("Nạp Thẻ ĐT",new DialogInterface.OnClickListener(){

                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        Toast.makeText(InfoFilm.this,"Bằng THẻ", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                })
                                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                                .show();
                                                                    }
                                                                })
                                                                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // do nothing
                                                                    }
                                                                })
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .show();
                                                    }

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }
                                ){
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Movideo-Auth", token_user);
                                        return params;
                                    }
                                }
                                        ;

                                requestQueue.add(stringRequest);
                            }


                        }
                    });

                }

                JSONObject json_rela = new JSONObject(rela_reponse);
                JSONArray data_rela = json_rela.getJSONArray("data");
                final ArrayList<Related> arrayList_re = new ArrayList<Related>();
                for (int f=0;f<data_rela.length();f++)
                {
                    JSONObject ob_rela = data_rela.getJSONObject(f);
                    JSONObject image_rela = ob_rela.getJSONObject("image");
                    String image_url = image_rela.getString("base_uri");
                    Related tmp = new Related();
                    tmp.setData(ob_rela.toString());
                    tmp.setHinhanh(image_url);
                    arrayList_re.add(tmp);
                }
                recyclerView_related.setHasFixedSize(true);
                LinearLayoutManager layoutManager_related = new LinearLayoutManager(InfoFilm.this, LinearLayoutManager.HORIZONTAL,false);
                recyclerView_related.setLayoutManager(layoutManager_related);
                RecyclerRelatedAdapter recyclerViewAdapter_related = new RecyclerRelatedAdapter(InfoFilm.this,arrayList_re);
                recyclerView_related.setAdapter(recyclerViewAdapter_related);
                recyclerView_related.addOnItemTouchListener(
                        new RecyclerItemClickListener(InfoFilm.this, recyclerView_related ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                String data_list = arrayList_re.get(position).getData();
                                try {
                                    Intent intent = new Intent(InfoFilm.this,InfoFilm.class);
                                    JSONObject jsonObject_sub = new JSONObject(arrayList_re.get(position).getData());
                                    intent.putExtra("url", jsonObject_sub.getString("package_type"));
                                    intent.putExtra("href","/"+jsonObject_sub.getString("type")+"/"+jsonObject_sub.getInt("id"));
                                    overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                                    InfoFilm.this.startActivity(intent);
//                                                    Toast.makeText(MainActivity.this,"Đang ở đây" + url_title,Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    Toast.makeText(InfoFilm.this,"Không tìm thấy dữ liệu ",Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override public void onLongItemClick(View view, int position) {

                            }
                        })
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void AnhXa() {
        commentBtn =(FloatingActionButton) findViewById(R.id.fab);
        bginfo = (ImageView) findViewById(R.id.bg_info);
        videoView = (ImageView) findViewById(R.id.videoview);
        listView = (RelativeLayout) findViewById(R.id.listview_video);
        imageView = (ImageView) findViewById(R.id.cencelview);
        drawerLayout = (CoordinatorLayout) findViewById(R.id.layout_video);
        relativeLayout = (RelativeLayout) findViewById(R.id.info_play);
        textView = (TextView) findViewById(R.id.txtbar_video);
        image_video = (ImageView) findViewById(R.id.image_video);
        txttitle = (TextView) findViewById(R.id.txttitle);
        txtclassification = (TextView) findViewById(R.id.classification);
        txtdescription = (TextView) findViewById(R.id.description);
        txtgenres = (TextView) findViewById(R.id.genres);
        txtactors = (TextView) findViewById(R.id.actors);
        txtlanguage = (TextView) findViewById(R.id.language);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_ep);
        recyclerView_related = (RecyclerView) findViewById(R.id.recycleview_related);
        textView_epi = (TextView) findViewById(R.id.epi);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        scrollView = (ScrollView) findViewById(R.id.scrollView_info);
        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT && resultCode==RESULT_OK && data !=null )
        {
            String data_new = data.getStringExtra("data");
            try {
                JSONObject jsonObject_new = new JSONObject(data_new);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("identifier",jsonObject_new.getString("identifier"));
                editor.putString("accessToken",jsonObject_new.getString("accessToken"));
                editor.putString("phone",jsonObject_new.getString("phone"));
                editor.putString("currency",jsonObject_new.getString("currency"));
                editor.putString("object",jsonObject_new.getString("object"));
                editor.putString("credits",jsonObject_new.getString("credits"));
                editor.putString("given_name",jsonObject_new.getString("given_name"));
                editor.putString("family_name",jsonObject_new.getString("family_name"));
                editor.putString("date_of_birth",jsonObject_new.getString("date_of_birth"));
                editor.putString("devices",jsonObject_new.getString("family_name"));
                editor.putString("subscription",jsonObject_new.getString("subscription"));
                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(requestCode == REQUEST_LOGOUT && resultCode==RESULT_OK)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private static class BlurBuilder {
        private static final float BITMAP_SCALE = 0.4f;
        private static final float BLUR_RADIUS = 7.5f;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public static Bitmap blur(Context context, Bitmap image) {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }
    }
}
