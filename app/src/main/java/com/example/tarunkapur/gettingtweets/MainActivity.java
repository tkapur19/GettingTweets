package com.example.tarunkapur.gettingtweets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar Settings
        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.main_head);
        toolbar.setLogo(R.drawable.twitter_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Welcome to GetTweetsApp");

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }



        // Its the url for api hitting hosted as a django server on my mac
        final String serverUrl="http://192.168.0.102:8000/tweets/";


        //Button for getting tweets
        Button button=(Button) findViewById(R.id.button1);

        //ImagView for loading DP of the userName
        final CircleImageView profilepic=(CircleImageView) findViewById(R.id.profile_pic);

        //EditText for getting twitter username from user
        final EditText editText=(EditText) findViewById(R.id.username);
        editText.setHint("@Username");

        // Main textView for showing text msg of tweet by the twitter username
        final TextView textView=(TextView) findViewById(R.id.txt1);

        //textView for showing name and screenName of the twitter user
        final TextView name=(TextView) findViewById(R.id.name);
        final TextView screenName=(TextView) findViewById(R.id.screen_name);


       // Handling button call i.e Get Tweets
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Making request by api calling to get the latest tweet by twitter user
                String username=editText.getText().toString();
                RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, serverUrl+username, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("myMessage", String.valueOf(response));
                        try {
                            textView.setText(response.getString("text"));
                            Picasso.with(getApplicationContext()).load(response.getJSONObject("user").getString("profile_image_url")).into(profilepic);
                            name.setText(response.getJSONObject("user").getString("name"));
                            screenName.setText("@"+response.getJSONObject("user").getString("screen_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(jsonObjectRequest);




            }
        });
    }



}
