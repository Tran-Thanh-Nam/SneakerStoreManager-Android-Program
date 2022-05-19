package com.hiu.vn.bangiayonline.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hiu.vn.bangiayonline.R;
import com.hiu.vn.bangiayonline.adapter.AdidasAdapter;
import com.hiu.vn.bangiayonline.adapter.NikeAdapter;
import com.hiu.vn.bangiayonline.model.Sanpham;
import com.hiu.vn.bangiayonline.util.CheckConnection;
import com.hiu.vn.bangiayonline.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdidasActivity extends AppCompatActivity {

    Toolbar toolbaradidas;
    ListView lvadidas;
    AdidasAdapter adidasAdapter;
    ArrayList<Sanpham> mangadidas;
    int idadidas = 0;
    int page = 1;
    View footerview;
    boolean isLoading= false;
    boolean limitdata =false;
    mHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adidas);
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            Anhxa();
            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại internet");
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.hiu.vn.bangiayonline.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoreData() {

        lvadidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangadidas.get(i));
                startActivity(intent);
            }
        });
        lvadidas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && isLoading == false && limitdata == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void Anhxa() {
        toolbaradidas = (Toolbar) findViewById(R.id.toolbaradidas);
        lvadidas = (ListView) findViewById(R.id.listviewadidas);
        mangadidas = new ArrayList<>();
        adidasAdapter = new AdidasAdapter(getApplicationContext(),mangadidas);
        lvadidas.setAdapter(adidasAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();
    }

    private void GetIdloaisp() {
        idadidas = getIntent().getIntExtra("idloaisanpham",-1);

    }

    private void ActionToolbar() {
        setSupportActionBar(toolbaradidas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbaradidas.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan= Server.Duongdannike+String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tenadidas = "";
                int Giaadidas = 0;
                String Hinhanhadidas = "";
                String Motaadidas = "";
                int Idspadidas = 0;
                if (response != null && response.length() != 2){
                    lvadidas.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenadidas = jsonObject.getString("tensp");
                            Giaadidas = jsonObject.getInt("giasp");
                            Hinhanhadidas = jsonObject.getString("hinhanhsp");
                            Motaadidas = jsonObject.getString("motasp");
                            Idspadidas = jsonObject.getInt("idsanpham");
                            mangadidas.add(new Sanpham(id,Tenadidas,Giaadidas,Hinhanhadidas,Motaadidas,Idspadidas));
                            adidasAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham",String.valueOf(idadidas));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lvadidas.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
