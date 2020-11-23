package co.kr.inclass.herings;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

import co.kr.inclass.herings.fragment.CameraFragment;

public class CameraActivity extends AppCompatActivity implements SensorEventListener {

    CameraFragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initUI();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void initUI() {
        cameraFragment = CameraFragment.newInstance();
        FragmentTransaction w_ft = getSupportFragmentManager().beginTransaction();
        w_ft.replace(R.id.flyCamera, cameraFragment);
        w_ft.addToBackStack(null);
        w_ft.commit();
    }

    public void doRegCallOrder(File file) {

        Intent intent = new Intent();
        intent.setData(Uri.fromFile(file));
        setResult(RESULT_OK, intent);
        finish();
    }

    public void doCameraCancel() {
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}