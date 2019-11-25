package com.example.myapplication2;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class OpenShopActivity extends AppCompatActivity implements View.OnClickListener {

    private Button new_shop;
    private Button picture;
    private EditText shop_name;
    private EditText shop_owner;
    private EditText shop_introduce;
    private String account_id;
    private String aByte = null;
    public static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_shop);

        Intent intent = getIntent();
        account_id = intent.getStringExtra("id");

        initUI();

        new_shop.setOnClickListener(this);
        picture.setOnClickListener(this);

    }

    private void initUI() {
        shop_name = findViewById(R.id.et_newshop_shopname);
        shop_owner = findViewById(R.id.et_newshop_owner);
        new_shop = findViewById(R.id.bt_newshop_openshop);
        shop_introduce = findViewById(R.id.et_newshop_introduce);
        picture = findViewById(R.id.bt_newshop_picture);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_newshop_openshop: {

                String name = shop_name.getText().toString();
                String owner = shop_owner.getText().toString();
                String introduce = shop_introduce.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(OpenShopActivity.this, "请输入商店名", Toast.LENGTH_SHORT).show();return;
                } else if (owner.length() == 0) {
                    Toast.makeText(OpenShopActivity.this, "请输入所有者", Toast.LENGTH_SHORT).show();return;
                } else if (introduce.length() == 0) {
                    Toast.makeText(OpenShopActivity.this, "请输入店铺简介", Toast.LENGTH_SHORT).show();return;
                }
                else if (aByte == null)
                {Toast.makeText(OpenShopActivity.this, "请插入图片", Toast.LENGTH_SHORT).show();return;}
                else {
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(OpenShopActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

                    database.execSQL("insert into shop(shop_name,shop_owner,shop_introduce,account_id,shop_picture) values('" + name + "','" + owner + "','" + introduce + "','" + account_id + "','" + aByte + "');");
                    database.close();
                    Toast.makeText(OpenShopActivity.this, name + "开店成功", Toast.LENGTH_LONG).show();
                    break;
                }
            }
            case R.id.bt_newshop_picture: {
                if (ContextCompat.checkSelfPermission(OpenShopActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OpenShopActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                Toast.makeText(OpenShopActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // Toast.makeText(OpenShopActivity.this,"onActivityResult1",Toast.LENGTH_SHORT).show();
//        Toast.makeText(OpenShopActivity.this,String.valueOf(requestCode),Toast.LENGTH_SHORT).show();
        Log.e("TAG", String.valueOf(requestCode));
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT>= 19){
                        handleImageOnKitKat(data);
//                        Toast.makeText(OpenShopActivity.this,"onActivityResult2",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        handleImageBoforeKitKat(data);
//                        Toast.makeText(OpenShopActivity.this,"onActivityResult3",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                default:
                    break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBoforeKitKat(Intent data) {
        Uri uri = data.getData();
        String iamgePath = getImagePath(uri, null);
        displayImage(iamgePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
//        Toast.makeText(OpenShopActivity.this," display",Toast.LENGTH_SHORT).show();
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            aByte = bitmapToString(bitmap);

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }

    private byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();
//        Toast.makeText(OpenShopActivity.this,"2111111111111",Toast.LENGTH_SHORT).show();
//        if (imgBytes == null)
//        {Toast.makeText(OpenShopActivity.this, "请插入图片", Toast.LENGTH_SHORT).show();}
        return imgBytes;
    }

    public String bitmapToString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string= Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }
}




