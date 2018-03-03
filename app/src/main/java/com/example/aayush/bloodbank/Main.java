package com.example.aayush.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Exchanger;

import static android.R.attr.inputType;
import static android.R.attr.start;
import static com.example.aayush.bloodbank.MainActivity.url;

/**
 * Created by Aayush on 7/26/2017.
 */

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ProgressDialog progressDialog;
    RequestQueue requestQueue1;
    ListView listView1;
    List<String> list;
    int i, j;
    EditText inputSearch;
    ArrayList<String> arrayList;
    public static String str12[];
    String[] h_name;
    String[] address;
    String[] district;
    String[] city;
    String[] state;
    String[] phno;
    String[] str1,str2;
    public final static String url = "https://data.gov.in/node/356981/datastore/export/json";
    String stateRe;


    ArrayAdapter<String> ar1;
   // EditText inputSearch;
   // ArrayList<HashMap<String, String>> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView1 = (ListView) this.findViewById(R.id.listview1);
        listView1.setOnItemClickListener(this);
      /*  inputSearch = (EditText) findViewById(R.id.inputSearch);
        final ImageView crossImageView=(ImageView) findViewById(R.id.crossImageView);*/

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        final ImageView crossImageView=(ImageView) findViewById(R.id.crossImageView);


        final Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        stateRe = bundle.getString("value");

        Log.e("Received Data", stateRe);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data");
        progressDialog.show();

        ///

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest request1 = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        try {
                            JSONArray array2 = response.getJSONArray("data");
                            String str[] = new String[array2.length()];
                            h_name = new String[array2.length()];
                            address = new String[array2.length()];
                            district = new String[array2.length()];
                            state = new String[array2.length()];
                            phno = new String[array2.length()];
                            city =new String[array2.length()];

                            arrayList = new ArrayList<String>();
                            //array list clear for new information strt from 0
                            arrayList.clear();

                            //TODO : Loop for all 2000+ data arrays

                            for (i = 0, j = 0; i < array2.length(); i++) {
                                JSONArray array1 = array2.getJSONArray(i);
                                // TODO: 7/26/2017 for loop if want to access all data of particular bank
                                if (array1.getString(1).contentEquals(stateRe)) {
                                    str[j] = "STATE: " + array1.getString(1) + "\n" +
                                            "CITY: " + array1.getString(2) + "\n" +
                                            "DISTRICT: " + array1.getString(3) + "\n" +
                                            "HOSPITAL: " + array1.getString(4) + "\n" +
                                            "ADDRESS: " + array1.getString(5) + "\n" +
                                            "PINCODE: " + array1.getString(6) + "\n" +
                                            "PHONE NO: " + array1.getString(7) + "\n" +
                                            "WEBSITE: " + array1.getString(11) + "\n";
                                    h_name[j] = array1.getString(4);
                                    address[j] = array1.getString(5);
                                    district[j] = array1.getString(3);
                                    state[j] = array1.getString(1);
                                    city[j] = array1.getString(2);
                                    //// TODO: 29-Jul-17  ADD the phno is array to proivide in on longitem click listener
                                    if (array1.getString(7) != "NA") {

                                    }

                                    arrayList.add(j, str[j]);
                                    j++;


                                    //  Log.e("State Choosed", str[j]);
                                }
                            }


                            str12 = new String[j];
                            Log.e("Value of j:  ", String.valueOf(j));

                            //TODO SEARCH
                            str1 = new String[j];
                            str2 = new String[j];//TODO for city

                            for (int i = 0; i < j; i++) {
                                str1[i] = district[i];
                            }

                            //TODO for city

                            for (int i = 0; i < j; i++) {
                                str2[i] = city[i];
                            }

                            for (int i = 0; i < j; i++) {
                                Log.e("district", String.valueOf(str1[i]));
                            }

                            //TODO for city

                            for (int i = 0; i < j; i++) {
                                Log.e("city", String.valueOf(str2[i]));
                            }

                            Toast.makeText(Main.this, "ArrayList Size: " + arrayList.size(), Toast.LENGTH_SHORT).show();
                            ar1 = new ArrayAdapter<String>(Main.this, R.layout.textview, R.id.textViewList, arrayList);
                            listView1.setAdapter(ar1);

                            Log.e("DATA array adapter", String.valueOf(j));


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

                                    Main.this.ar1.getFilter().filter(cs);

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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });

                requestQueue1.add(request1);
            }


        });
        t.run();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        arrayList.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Hospital: "+ h_name[position]+"\n"
                +"Address: "+ address[position]+"\n"
                +"District: "+ district[position]+"\n"
                +"STATE: "+ state[position],
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Main.this, MapsActivity.class);
        intent.putExtra("value",h_name[position]+","+district[position]+","+state[position]+",India");
        startActivity(intent);


    }
}

