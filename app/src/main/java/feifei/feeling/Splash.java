package feifei.feeling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;


public class Splash extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserProxy.getCurrentUser(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class).putExtra("from", "splash"));
            finish();
        }
    }
}
