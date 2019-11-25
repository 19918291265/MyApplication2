package com.example.myapplication2;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
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

import com.example.myapplication2.Util.Util;

import java.io.ByteArrayOutputStream;

public class OpenGoodActivity extends AppCompatActivity implements View.OnClickListener {

    private Button good_add;
    private Button add_picture;
    private EditText good_name;
    private EditText good_price;
    private EditText good_introduce;
    public static final int CHOOSE_PHOTO = 2;
    private String shop_id;
    private String aByte = null;
    private String n0 = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_good);

        Intent intent_2=getIntent();
        shop_id = intent_2.getStringExtra("id");

        initUI();

        add_picture.setOnClickListener(this);
        good_add.setOnClickListener(this);
    }

    private void initUI() {
        good_add = findViewById(R.id.addgood);
        add_picture = findViewById(R.id.addpicture);
        good_name = findViewById(R.id.name);
        good_price = findViewById(R.id.price);
        good_introduce = findViewById(R.id.introduce);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addgood:

                String name = good_name.getText().toString();
                String price = good_price.getText().toString();
                String introduce = good_introduce.getText().toString();

                if (name.length() == 0) {
                    Toast.makeText(OpenGoodActivity.this, "商品名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (price.length() == 0) {
                    Toast.makeText(OpenGoodActivity.this, "请确定商品价格！", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!Util.isPrice_1(price))
                {
                    Toast.makeText(OpenGoodActivity.this, "请输入合法的商品价格！", Toast.LENGTH_SHORT).show();return;
                }

                else if (introduce.length() == 0) {
                    Toast.makeText(OpenGoodActivity.this, "请介绍你的商品！", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (aByte == null)
                {Toast.makeText(OpenGoodActivity.this, "请插入图片", Toast.LENGTH_SHORT).show();return;}
                else {

                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(OpenGoodActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();


                    database.execSQL("insert into good(good_name,good_price,good_introduce,shop_id,good_picture) values('" + name + "','" + price + "','" + introduce + "','" + shop_id + "','" + aByte + "');");
                    database.close();
                    Toast.makeText(OpenGoodActivity.this, name + "上架商品成功", Toast.LENGTH_LONG).show();
                    break;
                }
                    case R.id.addpicture:
                        if (ContextCompat.checkSelfPermission(OpenGoodActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(OpenGoodActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }
                        Toast.makeText(OpenGoodActivity.this, "插入成功", Toast.LENGTH_LONG).show();
                        break;
                }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", String.valueOf(requestCode));
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT>= 19){
                        handleImageOnKitKat(data);
                    }
                    else {
                        handleImageBoforeKitKat(data);
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