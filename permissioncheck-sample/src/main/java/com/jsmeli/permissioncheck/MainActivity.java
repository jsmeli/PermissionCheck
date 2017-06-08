package com.jsmeli.permissioncheck;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jsmeli.permissioncheck.sjml.PermissionCallBack;
import com.jsmeli.permissioncheck.sjml.PermissionCheck;

public class MainActivity extends Activity implements PermissionCallBack, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView permissionTest1 = (TextView) findViewById(R.id.permission_test_1);
        TextView permissionTest2 = (TextView) findViewById(R.id.permission_test_2);
        TextView permissionTest3 = (TextView) findViewById(R.id.permission_test_3);
        permissionTest1.setOnClickListener(this);
        permissionTest2.setOnClickListener(this);
        permissionTest3.setOnClickListener(this);
    }

    @Override
    public void applyResult(int requestCode, int resultCode) {
        switch (requestCode) {
            case 1:
                Toast.makeText(MainActivity.this, resultCode + "",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(MainActivity.this, resultCode + "",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(MainActivity.this, resultCode + "",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionCheck.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permission_test_1:
                PermissionCheck
                        .with(MainActivity.this)
                        .setRequestCodeAndisCue(1, true)
                        .showText(getString(R.string.title), getString(R.string.content1), getString(R.string.content2), getString(R.string.btnText1), getString(R.string.btnText2))
                        .needPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(MainActivity.this)
                        .check();
                break;
            case R.id.permission_test_2:
                PermissionCheck
                        .with(MainActivity.this)
                        .setRequestCodeAndisCue(2, false)
                        .needPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .callback(MainActivity.this)
                        .check();
                break;
            case R.id.permission_test_3:
                PermissionCheck
                        .with(MainActivity.this)
                        .setRequestCodeAndisCue(3, false)
                        .needPermission(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE)
                        .callback(MainActivity.this)
                        .check();
                break;
        }
    }
}
