package com.example.admin.restcalls;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.admin.restcalls.model.MyResponse;
import com.example.admin.restcalls.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //public static final String BASE_URL = "http://www.mocky.io/v2/59de725b100000fc12a85150";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url( Constants.URLS.BASE_URL )
                .build();

        switch( view.getId() ) {
            case R.id.btnNative:

                MyHttpThread myHttpThread = new MyHttpThread( Constants.URLS.BASE_URL );
                Thread thread = new Thread( myHttpThread );
                thread.start();

                break;
            case R.id.btnOkhttpSync:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = null;

                        try {
                            response = client.newCall( request )
                                    .execute()
                                    .body()
                                    .string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "buttonClicked: "  + response);

                        //serialize the response to objects with GSON
                        Gson gson = new Gson();
                        MyResponse myResponse = gson.fromJson( response, MyResponse.class );
                        Log.d(TAG, "run: " + myResponse.getName() );
                        Log.d(TAG, "run: " + myResponse.getCars().get(0));

                        //JSON is a format for converting objects into strings to be communicated
                        //between processes. Gson parses the string back into an object.

                        //native way
                        try {
                            JSONObject mainObject = new JSONObject( response );
                            Log.d(TAG, "MyParser: " + mainObject.get( "name" ));

                            JSONArray carArray = (JSONArray) mainObject.get( "cars" );
                            Log.d(TAG, "MyParser: " + carArray.get( 0 ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btnOkhttpAsync:

                client.newCall( request ).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: Thread is " + Thread.currentThread().getName());
                        Log.d(TAG, "onResponse: " + response.body().string());
                    }
                });

                break;
            case R.id.btnRetrofitSync:

                break;
            case R.id.btnRetrofitAsync:

                //retrofit2 is a library, like okhttp3 in the imports above.
                retrofit2.Call<MyResponse> myResponseCall = RetrofitHelper.getCall();

                myResponseCall.enqueue( new retrofit2.Callback<MyResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<MyResponse> call,
                                           retrofit2.Response<MyResponse> response) {
                        Log.d(TAG, "onResponse: " + response.body().getName());
                    }

                    @Override
                    public void onFailure(retrofit2.Call<MyResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.toString());
                    }
                });

                retrofit2.Call<MyResponse> myResponseCallV3 = RetrofitHelper.getCallV3( "v3" );
                myResponseCallV3.enqueue(new retrofit2.Callback<MyResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<MyResponse> call,
                                           retrofit2.Response<MyResponse> response) {

                    }

                    @Override
                    public void onFailure(retrofit2.Call<MyResponse> call, Throwable t) {

                    }
                });

                break;
        }
    }
}
