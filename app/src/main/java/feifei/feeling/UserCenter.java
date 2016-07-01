package feifei.feeling;

import android.os.Bundle;
import android.support.annotation.Nullable;

import feifei.feeling.user.DynamicHeaderListFragment;

public class UserCenter extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_con);
        getFragmentManager().beginTransaction().replace(R.id.container, new DynamicHeaderListFragment()).commit();
    }
}
