//package com.feifei.feature;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.cloud.CloudListener;
//import com.baidu.mapapi.cloud.CloudManager;
//import com.baidu.mapapi.cloud.CloudPoiInfo;
//import com.baidu.mapapi.cloud.CloudSearchResult;
//import com.baidu.mapapi.cloud.DetailSearchResult;
//import com.baidu.mapapi.cloud.NearbySearchInfo;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.InfoWindow;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.MyLocationConfiguration;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.model.LatLngBounds;
//import com.feifei.lifetools.BaseActivity;
//import com.feifei.lifetools.R;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Map;
//
//
//public class MapActivity extends BaseActivity implements View.OnClickListener,
//        CloudListener, SeekBar.OnSeekBarChangeListener {
//
//    MapView mMapView;
//    BaiduMap mBaiduMap;
//    // 标注弹窗
//    InfoWindow mInfoWindow;
//    // 位置回调接口
//    BDLocationListener locationListener = new MyLocationListener();
//    // 清单文件中注册服务
//    BDLocation location;
//    // 位置
//    LocationClient client;
//    Button button1, button;
//
//    SeekBar seekBar;
//    TextView progressText;
//
//    Marker[] markerx;
//    int p;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feature);
//        CloudManager.getInstance().init(this);
//
//        mMapView = (MapView) findViewById(R.id.bmapView);
//        mMapView.showScaleControl(false);
//        mMapView.showZoomControls(false);
//        mBaiduMap = mMapView.getMap();
////        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//
//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
//        mBaiduMap.setMyLocationEnabled(true);
//        client = new LocationClient(this);
//        client.registerLocationListener(locationListener);
//
//        LocationClientOption option2 = new LocationClientOption();
//        option2.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        // option2.setOpenGps(true);崩溃
//        option2.setCoorType("bd09ll");
//        option2.setScanSpan(360000);
//        // option2.setIsNeedAddress(true);
//        // option2.setNeedDeviceDirect(true);
//        client.setLocOption(option2);
//
//        button = (Button) findViewById(R.id.button);
//        button1 = (Button) findViewById(R.id.button1);
//        button.setOnClickListener(this);
//        button1.setOnClickListener(this);
//
//
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        seekBar.setOnSeekBarChangeListener(this);
//        seekBar.setFocusable(false);
//        progressText = (TextView) findViewById(R.id.progress);
////        button.setVisibility(View.INVISIBLE);
//        button1.setVisibility(View.INVISIBLE);
////        seekBar.setVisibility(View.INVISIBLE);
////        progressText.setVisibility(View.INVISIBLE);
//        // timer = new Timer(true);
//        // 点击标注弹出layout，点击layout的事件
//        mBaiduMap
//                .setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(final Marker marker2) {
//                        Button button = new Button(MapActivity.this);
//                        button.setBackgroundResource(R.drawable.popmap);
//                        InfoWindow.OnInfoWindowClickListener listener = null;
//                        for (int i = 0; i < markerx.length; i++) {
//                            if (marker2 == markerx[i]) {
//                                button.setText(enertyArrayList.get(i).getName());
//                                p = i;
//                                break;
//                            }
//                        }
//                        listener = new InfoWindow.OnInfoWindowClickListener() {
//                            public void onInfoWindowClick() {
//                                mBaiduMap.hideInfoWindow();
//                                Intent intent;
//                                //去详情页
//                                if (TextUtils.isEmpty(enertyArrayList.get(p).getUrl())) {
//                                    intent = new Intent(MapActivity.this,
//                                            DetailActivity.class);
////                                intent.putExtra("lat", 35.425289528306);
////                                intent.putExtra("lon", 116.60377473566);
//                                } else {
//                                    //去网页
//                                    intent = new Intent(MapActivity.this,
//                                            WebActivity.class);
//                                    intent.putExtra("url", enertyArrayList.get(p).getUrl());
//
//                                }
//                                intent.putExtra("p", enertyArrayList.get(p));
//                                intent.putExtra("lat",
//                                        location.getLatitude());
//                                intent.putExtra("lon",
//                                        location.getLongitude());
//                                MapActivity.this.startActivity(intent);
//
//                            }
//                        };
//                        LatLng ll = marker2.getPosition();
//                        mInfoWindow = new InfoWindow(BitmapDescriptorFactory
//                                .fromView(button), ll, -47, listener);
//                        mBaiduMap.showInfoWindow(mInfoWindow);
//                        return true;
//                    }
//                });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                client.start();
//            }
//        },1000);
//
//    }
//
//    ArrayList<FeaturePoint> enertyArrayList;
//    // private Timer timer;
//    // int time = 0;
//    NearbySearchInfo info;
//
//    private void search(int radius) {
//        info = new NearbySearchInfo();
//        info.ak = "KTx0CICY8ZbRTMhbdOGEpfUq";
//        info.geoTableId = 103205;
//        info.radius = radius;
//        info.location = "" + location.getLongitude() + ","
//                + location.getLatitude();
////        info.location = "" + 116.60377473566 + "," + 35.425289528306;
//        info.pageSize = 50;
//        // timer.schedule(new TimerTask() {
//        // @Override
//        // public void run() {
//        // if (time < 6) {
//        // info.pageIndex = time;
//        CloudManager.getInstance().nearbySearch(info);
//        // time++;
//        // }
//        // }
//        // }, 1000, 2000);
//    }
//
//    // 获取到搜索结果后添加标记
//    public void onGetSearchResult(CloudSearchResult result, int error) {
//
//        mBaiduMap.clear();
//        if (result != null && result.poiList != null
//                && result.poiList.size() > 0) {
//            // Log.e("map",
//            // "onGetSearchResult, result length: "
//            // + result.poiList.size());
//            BitmapDescriptor bd1 = BitmapDescriptorFactory
//                    .fromResource(R.drawable.icon_marka);
//            BitmapDescriptor bd2 = BitmapDescriptorFactory
//                    .fromResource(R.drawable.icon_markb);
//            BitmapDescriptor bd3 = BitmapDescriptorFactory
//                    .fromResource(R.drawable.icon_markb);
//            LatLng ll;
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            //arraylist的问题，需要和数组在这里一起new
//            markerx = new Marker[result.poiList.size()];
//            enertyArrayList = new ArrayList<FeaturePoint>();
//            for (int i = 0; i < result.poiList.size(); i++) {
//                CloudPoiInfo cloudPoiInfo = result.poiList.get(i);
//                FeaturePoint featurePoint = new FeaturePoint(cloudPoiInfo.title,
//                        cloudPoiInfo.address, cloudPoiInfo.latitude,
//                        cloudPoiInfo.longitude);
//                Map<String, Object> map = cloudPoiInfo.extras;
//                featurePoint.setUrl((String) map.get("url"));
//                featurePoint.setPrice((String) map.get("price"));
//                String typeString = (String) map.get("types");
//                featurePoint.setType(typeString);
//                enertyArrayList.add(featurePoint);
//                // for (CloudPoiInfo info : result.poiList) {
//                // ll = new LatLng(info.latitude, info.longitude);
//                ll = new LatLng(cloudPoiInfo.latitude, cloudPoiInfo.longitude);
////                Log.e("log", " lat: " + cloudPoiInfo.latitude + " lon: " + cloudPoiInfo.longitude + " name: " + cloudPoiInfo.title);
//                OverlayOptions oo;
//                if (typeString.equals("景点")) {
//                    oo = new MarkerOptions().icon(bd1).position(ll);
//                } else if (typeString.equals("交通")) {
//                    oo = new MarkerOptions().icon(bd2).position(ll);
//                } else {
//                    oo = new MarkerOptions().icon(bd3).position(ll);
//                }
//
//                markerx[i] = (Marker) mBaiduMap.addOverlay(oo);
//                builder.include(ll);
//
//            }
//            LatLngBounds bounds = builder.build();
//            MapStatusUpdate u =
//                    MapStatusUpdateFactory.newLatLngBounds(bounds);
//            mBaiduMap.animateMapStatus(u);
//
//        }
//
//    }
//
//    public void onGetDetailSearchResult(DetailSearchResult result, int error) {
//        if (result != null) {
//            if (result.poiInfo != null) {
//                Toast.makeText(MapActivity.this, result.poiInfo.title,
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(MapActivity.this, "status:" + error,
//                        Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(MapActivity.this, "status:" + result.status,
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        progressText.setText("范围" + i);
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        // time = 0;
//        // mBaiduMap.clear();
//        if (seekBar.getProgress() == 0) {
//            search(1);
//        } else {
//            search(seekBar.getProgress());
//        }
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.button1:
//                // 点击定位的时候只需重新设置一下参数就行了
//                mBaiduMap
//                        .setMyLocationConfigeration(new MyLocationConfiguration(
//                                MyLocationConfiguration.LocationMode.FOLLOWING,
//                                true, null));
//                break;
//            case R.id.button:
//                fav();
//                break;
//        }
//
//    }
//
//    class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            location = bdLocation;
//            // 构造定位数据
//            MyLocationData locData = new MyLocationData.Builder()
//                    // 定位经度
//                    .accuracy(location.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(location.getDirection())
//                    .latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            // 设置缩放
////            MapStatus mMapStatus = new MapStatus.Builder()
////                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
////                    .zoom(16).build();
//            //模拟地理位置测试
////            MyLocationData locData = new MyLocationData.Builder()
////                    // 定位经度
////                    .accuracy(location.getRadius())
////                            // 此处设置开发者获取到的方向信息，顺时针0-360
////                    .direction(location.getDirection())
////                    .latitude(35.425289528306).longitude(116.60377473566)
////                    .build();
//            // 设置定位数据
//            mBaiduMap.setMyLocationData(locData);
////            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
////                    .newMapStatus(mMapStatus);
////            // 改变地图状态
////            mBaiduMap.setMapStatus(mMapStatusUpdate);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    search(2000);
//                }
//            }, 1000);
//
//
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
////        client.start();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
//        client.stop();
//    }
//
//    String ss = "\\bimg+alt\\b";
//
//    public static void fav() {
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    String getURL = "http://www.baidu.com";
//                    URL getUrl = new URL(getURL);
//                    HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
//                    connection.connect();
//
//                    // 取得输入流，并使用Reader读取
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
//                    StringBuffer sb = new StringBuffer();
//                    String line = "";
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    reader.close();
//                    // 断开连接
//                    connection.disconnect();
//                    Log.e("log", sb.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute();
//
//
//    }
//}
