package com.example.aayush.bloodbank;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView tv, tv2;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    public ListView listView;
    List<String> list;
    AlertDialog alertDialog;
    EditText inputSearch;

    ArrayAdapter<String> ar;
    String str1[];
    public final static String url = "https://data.gov.in/node/356981/datastore/export/json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean connect =  isNetworkAvailable();
        Log.e("Internet ",String.valueOf(connect));

        listView = (ListView) this.findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data");
        progressDialog.show();

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        final ImageView crossImageView=(ImageView) findViewById(R.id.crossImageView);






        ///
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            final String str[] = new String[array.length()];
                            String[] state = new String[array.length()];

                            for (int i = 0; i < array.length(); i++) {
                                JSONArray array1 = array.getJSONArray(i);
                                // TODO: 7/26/2017 for loop if want to access all data of particular bank
                                str[i] = array1.getString(1); //+ ":" + array1.getString(17);
                            }

                            state = getUniqueState(str);

                            int j = getStateNo(str);

                            Log.e("State", String.valueOf(j));

                            str1 = new String[j];

                            for (int i = 0; i < j; i++) {
                                str1[i] = state[i];
                            }
                            // STate and city Extraction


                            // Toast.makeText(MainActivity.this, "Str1"+str1.length, Toast.LENGTH_SHORT).show();


                            ar = new ArrayAdapter<String>(MainActivity.this, R.layout.textview, R.id.textViewList, str1);
                            ar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            listView.setAdapter(ar);
                            for (int i = 0; i < j; i++)
                            Log.e("Hello", String.valueOf(str1[i]));



                            //TODO 8/20/17 for search bar

                            crossImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    inputSearch.setText("");
                                }
                            });


                            inputSearch.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    // When user changed the Text
                                    MainActivity.this.ar.getFilter().filter(cs);

                                }

                                @Override
                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                              int arg3) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void afterTextChanged(Editable arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });

                            //TODO 8/20/17 serachbar over

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


                ///
                requestQueue.add(request);
            }
        });
        t.run();


    }


    private String[] getUniqueState(String str[]) {
        String state[] = new String[40];
        int j = 0;
        for (int i = 0; i < str.length; i++) {
            if (i == 0) {
                state[j] = str[i];
                j++;
                continue;
            }

            if (!Objects.equals(str[i], str[i - 1])) {
                state[j] = str[i];
                j++;
            }
        }
        return state;
    }

    private int getStateNo(String str[]) {
        String state[] = new String[40];
        int j = 0;
        for (int i = 0; i < str.length; i++) {
            if (i == 0) {
                state[j] = str[i];
                j++;
                continue;
            }

            if (!Objects.equals(str[i], str[i - 1])) {
                state[j] = str[i];
                j++;
                continue;
            }
        }
        return j;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(MainActivity.this,Main.class);
        intent.putExtra("value", str1[position]);
        startActivity(intent);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
