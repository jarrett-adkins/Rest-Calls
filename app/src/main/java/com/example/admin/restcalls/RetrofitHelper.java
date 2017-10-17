package com.example.admin.restcalls;

import com.example.admin.restcalls.model.MyResponse;
import com.example.admin.restcalls.utils.Constants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Admin on 10/11/2017.
 */

public class RetrofitHelper {

    public static final String BASE_URL = "http://www.mocky.io/";

    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory(GsonConverterFactory.create() )
                .build();

        return retrofit;
    }

    public static Call<MyResponse> getCall() {
        Retrofit retrofit = create();
        RetrofitService service = retrofit.create( RetrofitService.class );

        return service.getResponse();
    }

    public static Call<MyResponse> getCallV3( String version ) {
        Retrofit retrofit = create();
        RetrofitService service = retrofit.create( RetrofitService.class );

        return service.getResponseV3( version );
    }

    public interface RetrofitService {
        @GET("v2/59de8b64100000620042a9b6")
        Call<MyResponse> getResponse();

        @GET("{version}/59de8b64100000620042a9b6")
        Call<MyResponse> getResponseV3( @Path("version") String version);
    }
}

/*
{
"name":"John",
"age":30,
"cars":[ "Ford", "BMW", "Fiat" ]
}
 */
