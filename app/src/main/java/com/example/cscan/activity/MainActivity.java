package com.example.cscan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cscan.R;
import com.example.cscan.db.DBHelper;
import com.example.cscan.main_utils.Constant;

public class MainActivity extends AppCompatActivity {

    public DBHelper dbHelper;
    private EditText et_search;
    protected ImageView iv_drawer;
    protected ImageView iv_group_camera;
    protected ImageView iv_more;
    private ListView lv_drawer;
    public RecyclerView rv_group;
    public LinearLayout ly_empty;
    private ImageView iv_folder;
    private ImageView iv_close_search;
    private ImageView iv_clear_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFormWidgets();

    }



    private void getFormWidgets() {
//            drawer_ly = (DrawerLayout) findViewById(R.id.drawer_ly);
//            lv_drawer = (ListView) findViewById(R.id.lv_drawer);
        iv_folder = (ImageView) findViewById(R.id.iv_folder);
        iv_drawer = (ImageView) findViewById(R.id.iv_drawer);
        iv_more = (ImageView) findViewById(R.id.iv_more);
//            rl_search_bar = (RelativeLayout) findViewById(R.id.rl_search_bar);
        iv_close_search = (ImageView) findViewById(R.id.iv_close_search);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_clear_txt = (ImageView) findViewById(R.id.iv_clear_txt);
//            tag_tabs = (TabLayout) findViewById(R.id.tag_tabs);
        rv_group = (RecyclerView) findViewById(R.id.rv_group);
        ly_empty = (LinearLayout) findViewById(R.id.ly_empty);
//            tv_empty = (TextView) findViewById(R.id.tv_empty);
        iv_group_camera = (ImageView) findViewById(R.id.iv_group_camera);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_folder:
                openNewFolderDialog("");//mở folder mới
                return;
            case R.id.iv_clear_txt:
                et_search.setText("");
                iv_clear_txt.setVisibility(View.GONE);//ẩn text
                return;
//            case R.id.iv_drawer:
//                drawer_ly.openDrawer(GravityCompat.START);
//                return;
            case R.id.iv_group_camera:
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 2);
                return;
//            case R.id.iv_more:
//                PopupMenu popupMenu = new PopupMenu(this, view);
//                popupMenu.setOnMenuItemClickListener(this);
//                popupMenu.inflate(R.menu.group_more);
//                try {
//                    Field declaredField = PopupMenu.class.getDeclaredField("mPopup");
//                    declaredField.setAccessible(true);
//                    Object obj = declaredField.get(popupMenu);
//                    obj.getClass().getDeclaredMethod("setForceShowIcon", new Class[]{Boolean.TYPE}).invoke(obj, new Object[]{true});
//                    popupMenu.show();
//                    return;
//                } catch (Exception exception) {
//                    popupMenu.show();
//                    return;
//                }
//            case R.id.iv_search:
//                iv_search.setVisibility(View.GONE);
//                rl_search_bar.setVisibility(View.VISIBLE);
//                showSoftKeyboard(et_search);
//                return;
            default:
                return;
        }
    }

    private void openNewFolderDialog(String s) {

    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1) {
            if (i != 2) {
            } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) {
                Constant.inputType = "Group";
                Constant.IdentifyActivity = "ScannerActivity";
                startActivity(new Intent(MainActivity.this, ScannerActivity.class));
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 2);
            }
        } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) {
            Constant.inputType = "Group";
            Constant.IdentifyActivity = "MainGalleryActivity";

        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1);
        }
    }
}