package com.libit.wingspayroll.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient{

        private static final int DEFAULT_TIMEOUT = 3000;
        private static Retrofit mRetrofit = null;

        public static Retrofit getClient()
        {
            HttpLoggingInterceptor logging=new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder=new OkHttpClient.Builder();
            builder.addInterceptor(logging);

            builder.cookieJar(new CookieJar() {
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies=cookieStore.get(url);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });

            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            if(mRetrofit==null){
                Gson gson=new GsonBuilder().setLenient().create();

                mRetrofit=new Retrofit.Builder()
                        .baseUrl(Urls.BASE_URL)
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

            }
            return mRetrofit;
        }
}