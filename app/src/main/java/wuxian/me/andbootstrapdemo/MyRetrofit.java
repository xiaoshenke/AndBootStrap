package wuxian.me.andbootstrapdemo;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static wuxian.me.andbootstrapdemo.Constant.BASE_URL;

/**
 * Created by wuxian on 4/3/2017.
 */

public class MyRetrofit {
    private static Retrofit sretrofit = null;

    public static Retrofit getInstance() {

        if (sretrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            sretrofit = new Retrofit.Builder().client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

        }

        return sretrofit;
    }
}
