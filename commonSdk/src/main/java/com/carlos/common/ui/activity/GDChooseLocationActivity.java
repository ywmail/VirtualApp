/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.Intent
 *  android.location.Location
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.MenuItem$OnActionExpandListener
 *  android.view.View
 *  android.widget.ArrayAdapter
 *  android.widget.CheckBox
 *  android.widget.EditText
 *  android.widget.ListView
 *  android.widget.TextView
 *  android.widget.Toast
 *  androidx.annotation.Nullable
 *  androidx.appcompat.app.AlertDialog
 *  androidx.appcompat.app.AlertDialog$Builder
 *  androidx.appcompat.widget.SearchView
 *  androidx.appcompat.widget.SearchView$OnQueryTextListener
 *  androidx.appcompat.widget.Toolbar
 *  com.amap.api.maps2d.AMap
 *  com.amap.api.maps2d.AMap$OnCameraChangeListener
 *  com.amap.api.maps2d.CameraUpdateFactory
 *  com.amap.api.maps2d.MapView
 *  com.amap.api.maps2d.model.CameraPosition
 *  com.amap.api.maps2d.model.LatLng
 *  com.amap.api.services.core.LatLonPoint
 *  com.amap.api.services.geocoder.GeocodeResult
 *  com.amap.api.services.geocoder.GeocodeSearch
 *  com.amap.api.services.geocoder.GeocodeSearch$OnGeocodeSearchListener
 *  com.amap.api.services.geocoder.RegeocodeQuery
 *  com.amap.api.services.geocoder.RegeocodeResult
 *  com.amap.api.services.help.Inputtips
 *  com.amap.api.services.help.Inputtips$InputtipsListener
 *  com.amap.api.services.help.InputtipsQuery
 *  com.amap.api.services.help.Tip
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$menu
 *  com.kook.librelease.R$string
 *  com.kook.librelease.R$style
 */
package com.carlos.common.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.carlos.common.ui.activity.base.VActivity;
import com.carlos.common.utils.StringUtils;
import com.carlos.common.utils.location.CoordinateBean;
import com.carlos.common.utils.location.PositionConvertUtil;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GDChooseLocationActivity
extends VActivity
implements GeocodeSearch.OnGeocodeSearchListener,
Inputtips.InputtipsListener {
    private AMap mMap;
    private MapView mapView;
    private MenuItem mSearchMenuItem;
    private GeocodeSearch geocoderSearch;
    private View mSearchLayout;
    private TextView mLatText;
    private TextView mLngText;
    private TextView mAddressText;
    private ArrayAdapter<MapSearchResult> mSearchAdapter;
    private View mMockImg;
    private View mMockingView;
    private View mMockBtn;
    private View mSearchTip;
    private TextView mMockText;
    private String mCity;
    private String mCurPkg;
    private int mCurUserId;
    private VLocation mLocation;
    private boolean isFindLocation;
    private boolean mMocking;
    private String mAddress;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        VLocation vLocation;
        super.onCreate(savedInstanceState);
        this.setResult(0);
        this.setContentView(R.layout.activity_mock_location);
        Toolbar toolbar = (Toolbar)this.bind(R.id.top_toolbar);
        toolbar.setTitle(R.string.activity_choose_location);
        toolbar.setTitleTextColor(-1);
        this.setSupportActionBar(toolbar);
        this.enableBackHome();
        ListView mSearchResult = (ListView)this.bind(R.id.search_results);
        this.mapView = (MapView)this.findViewById(R.id.map);
        this.mLatText = (TextView)this.bind(R.id.tv_lat);
        this.mLngText = (TextView)this.bind(R.id.tv_lng);
        this.mMockImg = this.bind(R.id.img_mock);
        this.mMockText = (TextView)this.bind(R.id.tv_mock);
        this.mSearchLayout = this.bind(R.id.search_layout);
        this.mAddressText = (TextView)this.bind(R.id.tv_address);
        this.mMockingView = this.bind(R.id.img_stop);
        this.mMockBtn = this.findViewById(R.id.img_go_mock);
        this.mSearchTip = this.findViewById(R.id.tv_tip_search);
        this.mapView.onCreate(savedInstanceState);
        this.mMap = this.mapView.getMap();
        this.mSearchAdapter = new ArrayAdapter((Context)this, 17367043, new ArrayList());
        mSearchResult.setAdapter(this.mSearchAdapter);
        this.geocoderSearch = new GeocodeSearch((Context)this);
        this.geocoderSearch.setOnGeocodeSearchListener((GeocodeSearch.OnGeocodeSearchListener)this);
        MapSearchResult.NULL.setAddress(this.getString(R.string.tip_no_find_points));
        mSearchResult.setOnItemClickListener((adapterView, view, position, id2) -> {
            MapSearchResult searchResult = (MapSearchResult)this.mSearchAdapter.getItem(position);
            if (searchResult != null && searchResult != MapSearchResult.NULL) {
                this.mSearchMenuItem.collapseActionView();
                this.gotoLocation(searchResult.address, searchResult.lat, searchResult.lng, true);
            }
        });
        this.mMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener(){

            public void onCameraChange(CameraPosition cameraPosition) {
            }

            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                GDChooseLocationActivity.this.gotoLocation(null, cameraPosition.target.latitude, cameraPosition.target.longitude, false);
            }
        });
        this.findViewById(R.id.img_stop_mock).setOnClickListener(v -> {
            VirtualLocationManager.get().setMode(this.mCurUserId, this.mCurPkg, 0);
            this.updateMock(false);
            Intent intent = this.getIntent();
            this.mLocation.latitude = 0.0;
            this.mLocation.longitude = 0.0;
            intent.putExtra("virtual_location", (Parcelable)this.mLocation);
            intent.putExtra("virtual.extras.package", this.mCurPkg);
            intent.putExtra("virtual.extras.userid", this.mCurUserId);
            intent.putExtra("virtual.extras.address", this.mAddress);
            this.setResult(-1, intent);
        });
        this.mMockBtn.setOnClickListener(v -> {
            if ("com.alibaba.android.rimet".equals(this.mCurPkg)) {
                CoordinateBean coordinateBean = PositionConvertUtil.gcj02ToWgs84(this.mLocation.getLatitude(), this.mLocation.getLongitude());
                this.mLocation.latitude = coordinateBean.getLatitude();
                this.mLocation.longitude = coordinateBean.getLongitude();
            }
            VLog.e("VA-", " mLocation " + this.mLocation.getLatitude() + "  " + this.mLocation.getLongitude());
            VirtualCore.get().killApp(this.mCurPkg, this.mCurUserId);
            VirtualLocationManager.get().setMode(this.mCurUserId, this.mCurPkg, 2);
            VirtualLocationManager.get().setLocation(this.mCurUserId, this.mCurPkg, this.mLocation);
            this.updateMock(true);
            Intent intent = this.getIntent();
            intent.putExtra("virtual_location", (Parcelable)this.mLocation);
            intent.putExtra("virtual.extras.package", this.mCurPkg);
            intent.putExtra("virtual.extras.userid", this.mCurUserId);
            intent.putExtra("virtual.extras.address", this.mAddress);
            this.setResult(-1, intent);
        });
        this.findViewById(R.id.img_loc).setOnClickListener(v -> this.startLocation());
        ((CheckBox)this.findViewById(R.id.checkbox)).setOnCheckedChangeListener((v, b) -> this.showInputWindow());
        this.mMockingView.setOnClickListener(v -> {});
        this.mCurPkg = this.getIntent().getStringExtra("virtual.extras.package");
        this.mCurUserId = this.getIntent().getIntExtra("virtual.extras.userid", 0);
        VLocation vLocation2 = vLocation = this.getIntent().hasExtra("virtual_location") ? (VLocation)this.getIntent().getParcelableExtra("virtual_location") : null;
        if (vLocation != null) {
            CoordinateBean coordinateBean = PositionConvertUtil.wgs84ToGcj02(vLocation.latitude, vLocation.longitude);
            VLocation mLatLng = new VLocation(coordinateBean.getLatitude(), coordinateBean.getLongitude());
            this.mLocation = "com.alibaba.android.rimet".equals(this.mCurPkg) ? mLatLng : vLocation;
            this.updateMock(VirtualLocationManager.get().isUseVirtualLocation(this.mCurUserId, this.mCurPkg));
            this.gotoLocation(null, vLocation.getLatitude(), vLocation.getLongitude(), true);
        } else {
            this.mLocation = new VLocation();
            Location location = this.mMap.getMyLocation();
            if (location != null) {
                this.gotoLocation(null, location.getLatitude(), location.getLongitude(), false);
            }
            this.startLocation();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        MenuItem menuItem;
        this.getMenuInflater().inflate(R.menu.map_menu, menu2);
        this.mSearchMenuItem = menuItem = menu2.findItem(R.id.action_search);
        this.mSearchMenuItem.setEnabled(!this.mMocking);
        SearchView mSearchView = (SearchView)menuItem.getActionView();
        mSearchView.setImeOptions(3);
        mSearchView.setQueryHint((CharSequence)this.getString(R.string.tip_input_keywords));
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){

            public boolean onMenuItemActionExpand(MenuItem item) {
                GDChooseLocationActivity.this.mSearchLayout.setVisibility(View.VISIBLE);
                return true;
            }

            public boolean onMenuItemActionCollapse(MenuItem item) {
                GDChooseLocationActivity.this.mSearchLayout.setVisibility(View.GONE);
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            public boolean onQueryTextSubmit(String newText) {
                if (!TextUtils.isEmpty((CharSequence)newText)) {
                    HVLog.d("newText:" + newText);
                    InputtipsQuery inputquery = new InputtipsQuery(newText, newText);
                    Inputtips inputTips = new Inputtips((Context)GDChooseLocationActivity.this, inputquery);
                    inputTips.setInputtipsListener((Inputtips.InputtipsListener)GDChooseLocationActivity.this);
                    inputTips.requestInputtipsAsyn();
                } else {
                    GDChooseLocationActivity.this.mSearchAdapter.clear();
                    GDChooseLocationActivity.this.mSearchAdapter.notifyDataSetChanged();
                }
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332: {
                this.finish();
                break;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
        return true;
    }

    private void gotoLocation(String address, double lat, double lng, boolean move) {
        if (move) {
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((LatLng)new LatLng(lat, lng), (float)18.0f));
        } else {
            this.mLocation.latitude = StringUtils.doubleFor8(lat);
            this.mLocation.longitude = StringUtils.doubleFor8(lng);
            this.mLatText.setText((CharSequence)String.valueOf(this.mLocation.latitude));
            this.mLngText.setText((CharSequence)String.valueOf(this.mLocation.longitude));
        }
        if (TextUtils.isEmpty((CharSequence)address)) {
            LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200.0f, "autonavi");
            this.geocoderSearch.getFromLocationAsyn(query);
            HVLog.d("异步逆地理编码请求 lat：" + lat + "    lng :" + lng);
        } else {
            this.setAddress(address);
        }
    }

    private void startLocation() {
        if (this.isFindLocation) {
            Toast.makeText((Context)this, (int)R.string.tip_find_location, (int)0).show();
            return;
        }
        this.isFindLocation = true;
    }

    private void updateMock(boolean mock) {
        this.mMocking = mock;
        this.mMockImg.setSelected(mock);
        if (mock) {
            this.mMockText.setText(R.string.mocking);
            this.mMockingView.setVisibility(View.VISIBLE);
            this.mMockBtn.setVisibility(View.GONE);
            if (this.mSearchMenuItem != null) {
                this.mSearchMenuItem.setEnabled(false);
            }
        } else {
            this.mMockText.setText(R.string.no_mock);
            this.mMockingView.setVisibility(View.GONE);
            this.mMockBtn.setVisibility(View.VISIBLE);
            if (this.mSearchMenuItem != null) {
                this.mSearchMenuItem.setEnabled(true);
            }
        }
        this.mMockText.setSelected(mock);
    }

    private void setAddress(String text) {
        this.runOnUiThread(() -> {
            this.mAddress = text;
            this.mAddressText.setText((CharSequence)text);
        });
    }

    protected void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    private void showInputWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.VACustomTheme);
        View view1 = this.getLayoutInflater().inflate(R.layout.dialog_change_loc, null);
        builder.setView(view1);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        EditText editText1 = (EditText)view1.findViewById(R.id.edt_lat);
        editText1.setText((CharSequence)StringUtils.doubleFor8String(this.mLocation.getLatitude()));
        EditText editText2 = (EditText)view1.findViewById(R.id.edt_lon);
        editText2.setText((CharSequence)StringUtils.doubleFor8String(this.mLocation.getLongitude()));
        dialog.setCancelable(false);
        view1.findViewById(R.id.btn_cancel).setOnClickListener(arg_0 -> GDChooseLocationActivity.lambda$showInputWindow$7((Dialog)dialog, arg_0));
        view1.findViewById(R.id.btn_ok).setOnClickListener(arg_0 -> this.lambda$showInputWindow$8((Dialog)dialog, editText1, editText2, arg_0));
    }

    public void onGeocodeSearched(GeocodeResult result, int rCode) {
    }

    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        HVLog.d("result :" + result + "    rCode:" + rCode);
        if (rCode == 1000 && result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
            String addressName = result.getRegeocodeAddress().getFormatAddress() + "附近";
            HVLog.d("addressName :" + addressName);
            this.mCity = result.getRegeocodeAddress().getFormatAddress();
            this.setAddress(addressName);
        }
    }


    public void onGetInputtips(List<Tip> list, int rCode) {
        HVLog.d("onGetInputtips :" + list + "    rCode:" + rCode);
        if (rCode == 1000) {
            if (this.mSearchTip.getVisibility() != View.GONE) {
                this.runOnUiThread(() -> {
                    this.mSearchTip.setVisibility(View.GONE);
                });
            }

            this.mSearchAdapter.clear();
            if (list.size() == 0) {
                this.mSearchAdapter.add(GDChooseLocationActivity.MapSearchResult.NULL);
            } else {
                Iterator var3 = list.iterator();

                while(var3.hasNext()) {
                    Tip item = (Tip)var3.next();
                    MapSearchResult result = new MapSearchResult(item.getAddress(), item.getPoint().getLatitude(), item.getPoint().getLongitude());
                    result.setCity(item.getAddress());
                    this.mSearchAdapter.add(result);
                }
            }

            this.mSearchAdapter.notifyDataSetChanged();
        }

    }

    private /* synthetic */ void lambda$showInputWindow$8(Dialog dialog, EditText editText1, EditText editText2, View v2) {
        dialog.dismiss();
        try {
            double lat = Double.parseDouble(editText1.getText().toString());
            double lon = Double.parseDouble(editText2.getText().toString());
            this.gotoLocation(null, lat, lon, true);
        }
        catch (Exception e) {
            Toast.makeText((Context)this, (int)R.string.input_loc_error, (int)0).show();
        }
    }

    private static /* synthetic */ void lambda$showInputWindow$7(Dialog dialog, View v2) {
        dialog.dismiss();
    }

    private static class MapSearchResult {
        private static final MapSearchResult NULL = new MapSearchResult();
        private String address;
        private double lat;
        private double lng;
        private String city;

        private MapSearchResult() {
        }

        public MapSearchResult(String address) {
            this.address = address;
        }

        private MapSearchResult(String address, double lat, double lng) {
            this.address = address;
            this.lat = lat;
            this.lng = lng;
        }

        private void setAddress(String address) {
            this.address = address;
        }

        private void setCity(String city) {
            this.city = city;
        }

        public String toString() {
            return this.address;
        }
    }
}

