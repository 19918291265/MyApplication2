package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_load_register;
    private Button bt_load_load;
    private EditText et_load_account;
    private EditText et_load_cipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load);
        bt_load_register = findViewById(R.id.bt_load_register);
        bt_load_load = findViewById(R.id.bt_load_load);
        et_load_account = findViewById(R.id.et_load_account);
        et_load_cipher = findViewById(R.id.et_load_cipher);
        bt_load_register.setOnClickListener(this);
        bt_load_load.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_load_register:
                Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
                finish();
                break;
            case R.id.bt_load_load:
                String account_query = et_load_account.getText().toString();
                String cipher_compare = et_load_cipher.getText().toString();
                if (account_query.length()==0)
                { Toast.makeText(LoginActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show(); return; }
                else if (cipher_compare.length()==0)
                { Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show(); return; }
                else {
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(LoginActivity.this);
                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();


                Cursor cursor = database.query("user", new String[]{"account", "cipher"}, "account=?", new String[]{account_query}, null, null, null);

                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();

                if (cursor.moveToFirst()) {


                    String cipher = cursor.getString(cursor.getColumnIndex("cipher"));

                    cursor.close();//游标关闭
                    database.close();

                    if (cipher.equals(cipher_compare)) {

                        MyDataBaseHelper dataBaseHelper_1 = new MyDataBaseHelper(LoginActivity.this);
                        SQLiteDatabase database_1 = dataBaseHelper_1.getReadableDatabase();


                        Cursor cursor_1 = database_1.query("user", new String[]{"account_id", "account"}, "account=?", new String[]{account_query}, null, null, null);
                        if (cursor_1.moveToFirst()) {
                            Intent intent_load = new Intent(LoginActivity.this, PublicShopActivity.class);

                            String account_id = cursor_1.getString(cursor_1.getColumnIndex("account_id"));
                            intent_load.putExtra("id", account_id);
                            cursor_1.close();//游标关闭
                            database_1.close();

                            startActivity(intent_load);
                            finish();
                        }


                    } else {
                        Toast.makeText(this, "你输入的手机号或密码不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                }
        }
    }
}


