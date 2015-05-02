package com.example.smallc.mymap;

import android.app.Activity;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


public class MainActivity extends Activity {
    BaiduMap mBaiduMap;
    MapView mMapview;
    LocationClient mLocationClient;
    Button mButton;
    private LocationMode locationMode;
    boolean isFirstLoc=true;
    BitmapDescriptor bitmapDescriptor;
    MyLocationListener myLocationListener=new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mButton=(Button)findViewById(R.id.btn1);
        locationMode= LocationMode.NORMAL;

        OnClickListener onClickListener=new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (locationMode){
                   case NORMAL:
                       mButton.setText("跟随");
                       locationMode=LocationMode.FOLLOWING;
                       mBaiduMap.setMyLocationConfigeration(
                               new MyLocationConfiguration(locationMode,true,bitmapDescriptor));
                       break;
                   case FOLLOWING:
                       mButton.setText("罗盘");
                       locationMode=LocationMode.COMPASS;
                       mBaiduMap.setMyLocationConfigeration(
                               new MyLocationConfiguration(locationMode,true,bitmapDescriptor));
                       break;
                   case COMPASS:
                       mButton.setText("普通");
                       locationMode=LocationMode.NORMAL;
                       mBaiduMap.setMyLocationConfigeration(
                               new MyLocationConfiguration(locationMode,true,bitmapDescriptor));
                       break;
                }
            }
        };
        mButton.setOnClickListener(onClickListener);

        mMapview=(MapView)findViewById(R.id.bmapView);
        mBaiduMap=mMapview.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient=new LocationClient(this);
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onResume() {
        mMapview.onResume();
        super.onResume();
    }


    protected void onPause() {
        mMapview.onPause();
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mMapview.onDestroy();
        mMapview=null;
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

     class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location==null||mMapview==null){
                return;
            }

            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locationData);

            if(isFirstLoc){
                isFirstLoc=false;
                Log.d("MainActivity","aaaaa");
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
            }
        }

    }
}
