package feifei.feeling;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.yalantis.euclid.library.EuclidActivity;
import com.yalantis.euclid.library.EuclidListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.util.TS;

public class TeamActivity extends EuclidActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        super.onCreate(savedInstanceState);

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TS.s(TeamActivity.this, "点赞");
//                Toast.makeText(TeamActivity.this, "点赞", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected BaseAdapter getAdapter() {
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();

        int[] avatars = {
                R.drawable.d,
                R.drawable.df,
                R.drawable.z,
                R.drawable.f,
                R.drawable.p,
                R.drawable.w,
                R.drawable.h
        };
        String[] names = getResources().getStringArray(R.array.array_names);
        String[] shorts = getResources().getStringArray(R.array.lorem_ipsum_short);
        String[] longs = getResources().getStringArray(R.array.lorem_ipsum_long);

        for (int i = 0; i < avatars.length; i++) {
            profileMap = new HashMap<>();
            profileMap.put(EuclidListAdapter.KEY_AVATAR, avatars[i]);
            profileMap.put(EuclidListAdapter.KEY_NAME, names[i]);
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, shorts[i]);
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, longs[i]);
            profilesList.add(profileMap);
        }

        return new EuclidListAdapter(this, R.layout.list_item, profilesList);
    }

}
