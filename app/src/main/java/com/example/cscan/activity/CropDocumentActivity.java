package com.example.cscan.activity;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cscan.R;
import com.example.cscan.main_utils.AdjustUtil;
import com.example.cscan.main_utils.BitmapUtils;
import com.example.cscan.main_utils.Constant;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.pqpo.smartcropperlib.view.CropImageView;

public class CropDocumentActivity extends AppCompatActivity {
    private CropImageView iv_preview_crop;
    protected ImageView iv_done;
    public String selected_group_name;
    protected TextView iv_retake;
    public Bitmap original;
    public String username = "HA";
    protected TextView iv_Rotate_Doc;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//          if (Constant.IdentifyActivity.equals("CurrentFilterActivity")) {
//                startActivity(new Intent(CropDocumentActivity.this, CurrentFilterActivity.class));
//                Constant.IdentifyActivity = "";
//                finish();
//            } else
            if (Constant.IdentifyActivity.equals("ScannerActivity_Retake")) {
                Constant.IdentifyActivity = "";
                finish();
            }
        }

    };
    private String group_name;
    private String group_date;
    private String URL_Insert_Group="http://192.168.56.241/android/insertGroup.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_document);
        //dbHelper = new DBHelper(this);

        init();

    }

    private void init() {
        iv_preview_crop = (CropImageView) findViewById(R.id.iv_preview_crop);
        iv_done = (ImageView) findViewById(R.id.iv_done);
        iv_retake = (TextView) findViewById(R.id.iv_retake);
        iv_Rotate_Doc = (TextView) findViewById(R.id.iv_Rotate_Doc);
        if (Constant.original != null) {
            iv_preview_crop.setImageToCrop(Constant.original);
            original = Constant.original;
            changeBrightness(20);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_done:
                if (iv_preview_crop.canRightCrop()) {
                    Constant.original = iv_preview_crop.crop();
                    insertImage();
                    Intent intent2 = new Intent(CropDocumentActivity.this, GroupDocumentActivity.class);
                    intent2.putExtra("current_group", group_name);
                    startActivity(intent2);
                    finish();
                    return;
                }
                return;
            case R.id.iv_retake:
                startActivity(new Intent(CropDocumentActivity.this, ScannerActivity.class));
                finish();
                return;
            case R.id.iv_Rotate_Doc:
                Bitmap bitmap = Constant.original;
                Matrix matrix = new Matrix();
                matrix.postRotate(90.0f);
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                Constant.original.recycle();
                System.gc();
                Constant.original = createBitmap;
                original = createBitmap;
                iv_preview_crop.setImageToCrop(Constant.original);
                iv_preview_crop.setFullImgCrop();
                Log.e(TAG, "onClick: Rotate");
                return;
        }
    }

    private void changeBrightness(float brightness) {
        iv_preview_crop.setImageBitmap(AdjustUtil.changeBitmapContrastBrightness(original, 1.0f, brightness));
    }

    public static final String URL_Insert_Image = "http://192.168.56.241/android/insertImage.php";

    private void insertImage() {
        Bitmap bitmap = Constant.original;
        byte[] bytes = BitmapUtils.getBytes(bitmap);
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(externalFilesDir, System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } if (Constant.inputType.equals("Group")) {
            group_name = "CamScanner" + Constant.getDateTime("_ddMMHHmmss");
            group_date = Constant.getDateTime("yyyy-MM-dd  hh:mm a");
            insertGroup(group_name, group_date);
        }else {
            group_name = GroupDocumentActivity.current_group;

        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Insert_Image,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(CropDocumentActivity.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CropDocumentActivity.this, "Lỗi thêm ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CropDocumentActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fileImage", file.getPath());
                params.put("groupName", group_name);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
    private void insertGroup(String group_name, String group_date) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Insert_Group,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CropDocumentActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("groupName", group_name);
                params.put("groupDate", group_date);
                params.put("username", "ha");

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


}