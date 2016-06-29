package feifei.feeling;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"a980d5022a5f7fc57231a29bd55b6f0c");
        Fresco.initialize(this);
    }
}
