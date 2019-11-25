package com.example.myapplication2;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.AccessControlContext;

import static java.security.AccessController.getContext;

public class BeforePublicGoodActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_good;
    private TextView shop_name;
    private TextView shop_owner;
    private TextView shop_introduce;
    private ImageView picture_show;
    String shop_id ;
    private byte[] aByte;
    private String account_id;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_beforepublicgood);

        Intent intent = getIntent();
        shop_id = intent.getStringExtra("id");
        account_id = intent.getStringExtra("his");
    }

    @Override
    protected void onStart() {
        super.onStart();


    initUI();
        bt_good.setOnClickListener(this);

        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(BeforePublicGoodActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("shop",new String[]{"shop_owner","shop_introduce","shop_picture"},"shop_id=?",new String[]{shop_id},null,null,null);
        if (cursor.moveToFirst()){
            String owner = cursor.getString(cursor.getColumnIndex("shop_owner"));
            String introduce = cursor.getString(cursor.getColumnIndex("shop_introduce"));
            String picture = cursor.getString(cursor.getColumnIndex("shop_picture"));
             bitmap = stringToBitmap(picture);
//            byte[] bs = picture.getBytes();//以下都是我尝试过但失败的方法
            Matrix matrix = new Matrix();
            matrix.setScale(0.7f, 0.7f);
            bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, false);

//            Matrix matrix = new Matrix();
//            matrix.setScale(0.7f, 0.7f);
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeResource(getResources(), R.id.picture, options);
//            int imageHeight = options.outHeight;
//            int imageWidth = options.outWidth;
//            String imageType = options.outMimeType;
//         bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, false);
//            picture_show.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.picture, 100, 100));

//            if (picture != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(picture);
//                    Bitmap bitmap = BitmapFactory.decodeFile(picture);



//
//                    aByte = bitmapToByte(bitmap);
//                BitmapFactory.Options op = new BitmapFactory.Options();
//            op.inSampleSize = 2;
//           op.inJustDecodeBounds = true; //它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了。
//          op.inPreferredConfig = Bitmap.Config.ARGB_4444;
//            Bitmap bitmap1 = createBitmapFromByteData(bs,op);





//            picture_show.setImageBitmap(bitmap1);}
//            byte[]   bytelist = picture;
//            MemoryStream   ms1   =   new   MemoryStream(bytelist);
//            Bitmap   bm   =   (Bitmap)Image.FromStream(ms1);
//            ms1.Close()
//            ByteArrayInputStream bais = null;
//            if (picture != null) {
//                bais = new ByteArrayInputStream(picture);
//                appIcon.setImageDrawable(Drawable.createFromStream(bais, "photo"));//把图片设置到ImageView对象中
//            }


                //aByte = bitmapToByte(bitmap);
           picture_show.setImageBitmap(bitmap);
            shop_owner.setText(owner + "的店铺");
            shop_introduce.setText("店铺简介：" + introduce);
            cursor.close();
            database.close();
        }

    }
    private void initUI() {
        shop_owner = findViewById(R.id.owner);
        shop_introduce = findViewById(R.id.introduce);
        bt_good = findViewById(R.id.enter);
        picture_show = findViewById(R.id.picture);
    }

    @Override
    public void onClick(View v) {
    Intent intent = new Intent(BeforePublicGoodActivity.this,PublicGoodActivity.class);
    intent.putExtra("id",shop_id);
    intent.putExtra("his",account_id);
    startActivity(intent);

    }

  //  以下都是我尝试过但失败的方法

//    private Bitmap createBitmapFromByteData(byte[] data , BitmapFactory.Options options){
//
//
//
//           Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
//
//        return bitmap;
//    }
////
//    private byte[] bitmapToByte(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] imgBytes = baos.toByteArray();
//        return imgBytes;
//    }
//
//
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }
//
//    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) >= reqHeight&& (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }

    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
