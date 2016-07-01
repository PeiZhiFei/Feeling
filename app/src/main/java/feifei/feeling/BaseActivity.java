package feifei.feeling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.baiiu.tsnackbar.LUtils;
import com.baiiu.tsnackbar.TSnackbar;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //跟布局为CoordinatorLayout,并且添加了fitsSystemWindows属性,需要调用该方法
        //建议将该属性添加在Toolbar上或者AppBarLayout上
        TSnackbar.setCoordinatorLayoutFitsSystemWindows(true);

        //若将fitsSystemWindows添加在AppBarLayout或者Toolbar上,则不用调用此方法
        if (LUtils.hasKitKat()) {
            LUtils.instance(this).setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LUtils.clear();
    }
}
