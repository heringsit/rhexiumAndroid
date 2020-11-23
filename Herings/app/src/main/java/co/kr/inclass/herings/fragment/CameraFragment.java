package co.kr.inclass.herings.fragment;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import co.kr.inclass.herings.CameraActivity;
import co.kr.inclass.herings.R;
import co.kr.inclass.herings.util.Util;


public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    SurfaceView ui_sfvCamera;

    Camera camera;
    SurfaceHolder surfaceHolder;
    boolean isPreviewRunning;

    CameraActivity parentActivity;

    public CameraFragment() {

    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentActivity = (CameraActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        ui_sfvCamera = (SurfaceView) view.findViewById(R.id.sfv_Camera);
        view.findViewById(R.id.imv_shutter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnShutter();
            }
        });

        view.findViewById(R.id.tv_cancel_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.doCameraCancel();
            }
        });

        initLayout();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initLayout() {
        surfaceHolder = ui_sfvCamera.getHolder();
        surfaceHolder.setSizeFromLayout();
        surfaceHolder.addCallback(this);
    }

    void OnShutter() {
        if (camera != null) {
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    // 파일로 저장
                    new SaveImageTask().execute(data);
                }
            });
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // 카메라 객체를 사용할 수 있게 연결한다.
            camera = Camera.open();

            Camera.Parameters params = camera.getParameters();
//            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // 카메라의 회전이 가로/세로일때 화면을 설정한다.
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                params.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                params.setRotation(90);
            } else {
                params.set("orientation", "landscape");
                camera.setDisplayOrientation(0);
                params.setRotation(0);
            }
            camera.setParameters(params);

//            camera.setDisplayOrientation(Camera.CameraInfo.CAMERA_FACING_BACK);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            // 자동포커스 설정
            camera.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {

                    }
                }
            });
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 카메라 화면을 회전 할 때의 처리
        if (holder.getSurface() == null) {
            // 프리뷰가 존재하지 않을때
            return;
        }

        if (isPreviewRunning) {
            camera.stopPreview();
        }

        Camera.Parameters parameters = camera.getParameters();

        // 화면 회전시 사진 회전 속성을 맞추기 위해 설정한다.
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_0) {
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);
        } else if (rotation == Surface.ROTATION_90) {
            camera.setDisplayOrientation(0);
            parameters.setRotation(0);
        } else if (rotation == Surface.ROTATION_180) {
            camera.setDisplayOrientation(270);
            parameters.setRotation(270);
        } else {
            camera.setDisplayOrientation(180);
            parameters.setRotation(180);
        }

        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        Camera.Size size = getBestPreviewSize(width, height, parameters);
        // 변경된 화면 넓이를 설정한다.
        parameters.setPreviewSize(size.width, size.height);
        //camera.setParameters(parameters);

        // 새로 변경된 설정으로 프리뷰를 시작한다
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (Throwable t) {
            Log.e("SurfaceCallback", "Exception in setPreviewDisplay()", t);
            Toast.makeText(getContext(), t.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        camera.startPreview();

        isPreviewRunning = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        isPreviewRunning = false;
        camera.release();
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return (result);
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(byte[]... bytes) {
            File file = Util.createFile();
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes[0]);
                fos.flush();
                fos.close();

                parentActivity.doRegCallOrder(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
