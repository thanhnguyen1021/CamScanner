package com.example.cscan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.cscan.R;
import com.example.cscan.main_utils.Constant;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GroupDocumentActivity extends AppCompatActivity {
    public static String current_group;
    //protected GroupDocAdapter groupDocAdapter;
    protected ImageView iv_back;
    protected ImageView iv_create_pdf;
    protected ImageView iv_doc_camera;
    protected ImageView iv_doc_more;
    private LinearLayout ly_doc_camera;
    public Uri pdfUri;

    public RecyclerView rv_group_doc;

    public String selected_group_name;

    public int selected_position;

    public ArrayList<Bitmap> singleBitmap = new ArrayList<>();

    public String singleDoc;

    public TextView tv_title;
    @Override
    protected void onResume() {
        tv_title.setText(current_group);
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_document);

        current_group = getIntent().getStringExtra("current_group");
        init();
    }
    private void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_create_pdf = (ImageView) findViewById(R.id.iv_create_pdf);
        iv_doc_more = (ImageView) findViewById(R.id.iv_doc_more);
        rv_group_doc = (RecyclerView) findViewById(R.id.rv_group_doc);
        iv_doc_camera = (ImageView) findViewById(R.id.iv_doc_camera);
        ly_doc_camera = (LinearLayout) findViewById(R.id.ly_doc_camera);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                return;
            case R.id.iv_create_pdf:
               // new createAndOpenPDF(current_group).execute(new String[0]);
                return;
            case R.id.iv_doc_camera:
                Constant.inputType = "GroupItem";
                startActivity(new Intent(GroupDocumentActivity.this, ScannerActivity.class));
                finish();
                return;
            case R.id.iv_doc_more:
//                PopupMenu popupMenu = new PopupMenu(this, view);
//                popupMenu.setOnMenuItemClickListener(this);
//                popupMenu.inflate(R.menu.group_doc_more);
//                try {
//                    Field declaredField = PopupMenu.class.getDeclaredField("mPopup");
//                    declaredField.setAccessible(true);
//                    Object obj = declaredField.get(popupMenu);
//                    obj.getClass().getDeclaredMethod("setForceShowIcon", new Class[]{Boolean.TYPE}).invoke(obj, new Object[]{true});
//                    popupMenu.show();
//                    return;
//                } catch (Exception e) {
//                    popupMenu.show();
//                    e.printStackTrace();
//                    return;
//                }
            default:
                return;
        }
    }
}