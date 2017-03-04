package wuxian.me.andbootstrapdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by wuxian on 4/3/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
