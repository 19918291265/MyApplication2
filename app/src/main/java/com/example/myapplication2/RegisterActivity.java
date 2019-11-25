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
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication2.Util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_register_register;
    private Button bt_register_picture;
    private EditText account;
    private EditText cipher;
    private EditText name;
    private EditText address;
    private EditText introduce;
    private Button photo;
    private String aByte = null;
    public static final int CHOOSE_PHOTO = 2;
    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        initUI();

        bt_register_picture.setOnClickListener(this);
        bt_register_register.setOnClickListener(this);
        photo.setOnClickListener(this);
    }

    private void initUI() {
        bt_register_register=findViewById(R.id.bt_register_register);
        bt_register_picture=findViewById(R.id.bt_register_picture);
        introduce = findViewById(R.id.ev_register_introduce);
        account = findViewById(R.id.ev_register_account);
        cipher = findViewById(R.id.ev_register_cipher);
        name = findViewById(R.id.ev_register_name);
        address = findViewById(R.id.ev_register_address);
        photo = findViewById(R.id.take_photo);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_register_register:
            {
                String str_account = account.getText().toString();
                String str_cipher = cipher.getText().toString();
                String str_name = name.getText().toString();
                String str_address = address.getText().toString();
                String str_introduce = introduce.getText().toString();
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(RegisterActivity.this);//实例化一个对象

                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();//打开数据库
                Cursor cursor = database.query("user",new String[]{"account"},null, null,null,null,null);

                if (cursor.moveToFirst()){
                    String account_exist = cursor.getString(cursor.getColumnIndex("account"));
                    if (str_account.equals(account_exist))
                    {{ Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show(); return;}}

                    while (cursor.moveToNext()) {
                        String account_exist1  = cursor.getString(cursor.getColumnIndex("account"));
                        if (str_account.equals(account_exist1))
                        {{ Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show(); return;}}
                    }
                }


                if (str_account.length()<4)
                { Toast.makeText(RegisterActivity.this, "用户名至少4个数字！", Toast.LENGTH_SHORT).show(); return;}
                else if (str_cipher.length()<4)
                { Toast.makeText(RegisterActivity.this, "密码至少4个字符！", Toast.LENGTH_SHORT).show(); return;}
                else if (!Util.isEmpty(str_cipher))
                {Toast.makeText(RegisterActivity.this, "密码中输入了非法字符！", Toast.LENGTH_SHORT).show(); return;}
                else if (str_name.length()==0)
                { Toast.makeText(RegisterActivity.this, "昵称不能为空！", Toast.LENGTH_SHORT).show(); return;}
                else if (str_address.length()==0)
                { Toast.makeText(RegisterActivity.this, "地址不能为空！", Toast.LENGTH_SHORT).show(); return;}
                else if (str_introduce.length()==0)
                { Toast.makeText(RegisterActivity.this, "简介不能为空！", Toast.LENGTH_SHORT).show(); return;}
                else if (aByte == null)
                {Toast.makeText(RegisterActivity.this, "请先上传图片！", Toast.LENGTH_SHORT).show(); return;}
                    else{


                database.execSQL("insert into user(account,cipher,name,address,introduce,picture) values('" + str_account
                        + "','" + str_cipher + "','" + str_name + "','" + str_address + "','" + str_introduce + "','" + aByte + "');");

                database.close();
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                Intent intent_load = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent_load);
                finish();
                }
                break;
            }
            case R.id.bt_register_picture:
            {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                Toast.makeText(RegisterActivity.this, "插入成功！", Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.take_photo:
            {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try {
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(RegisterActivity.this,"com.example.cameraalbumtest.fileprovider",outputImage);
                }
                else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Toast.makeText(OpenShopActivity.this,"onActivityResult1",Toast.LENGTH_SHORT).show();
//        Toast.makeText(RegisterActivity.this,String.valueOf(requestCode),Toast.LENGTH_SHORT).show();
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
            case TAKE_PHOTO:{
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        aByte = bitmapToString(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
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
        Toast.makeText(RegisterActivity.this," display",Toast.LENGTH_SHORT).show();
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
