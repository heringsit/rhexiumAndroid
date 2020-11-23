package co.kr.inclass.herings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;

import co.kr.inclass.herings.util.MediaManager;
import co.kr.inclass.herings.util.Util;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 0x11;
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;


    private static final int MSG_ANI_STEP1 = 100;
    private static final int MSG_ANI_STEP2 = 200;
    private static final int MSG_ANI_STEP3 = 300;
    private static final int MSG_ANI_STEP4 = 400;
    private static final int MSG_ANI_STEP5 = 500;
    private static final int MSG_ANI_STEP6 = 600;
    private static final int MSG_ANI_STEP7 = 700;
    private static final int MSG_ANI_STEP8 = 800;
    private static final int MSG_ANI_STEP9 = 900;
    private static final int MSG_ANI_STEP10 = 1000;
    private static final int MSG_ANI_STEP11 = 1100;
    private static final int MSG_ANI_STEP12 = 1200;
    private static final int MSG_ANI_STEP13 = 1300;
    private static final int MSG_ANI_STEP14 = 1400;
    private static final int MSG_ANI_STEP15 = 1500;


    private WebView mWebView;

    // Splash 관련
    private RelativeLayout mSplash;
    private ImageView mSplashBg;
    private ImageView mSplashIcon1;
    private ImageView mSplashIcon2;
    private ImageView mSplashIcon3;
    private ImageView mSplashTxtWhite;
    private ImageView mSplashTxtGreen;

    private RelativeLayout mSplashBlack;
    private RelativeLayout mSplashWhite;
    private ImageView mSplashIcon;
    private ImageView mSplashMark;
    private ImageView mSplashDes;

    private GestureDetector gestureDetector;
    // 사진선택팝업
    RelativeLayout mRlSelectPhoto;

    private MediaManager mMediaManager;
    public ArrayList<String> selectedImages = new ArrayList<String>();

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> uploadMessage;

//    private final static int FILECHOOSER_RESULTCODE = 1;

    private String pushLink = "";

    String[] permissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CAMERA",
            "android.permission.READ_PHONE_STATE",
    };

    long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getStringExtra("link") != null)
            pushLink = getIntent().getStringExtra("link");

        mWebView = (WebView) findViewById(R.id.webview);

        mSplash = (RelativeLayout) findViewById(R.id.rl_splash);
        mSplashBg = (ImageView) findViewById(R.id.imv_splash_bg);
        mSplashIcon1 = (ImageView) findViewById(R.id.imv_splash_01);
        mSplashIcon2 = (ImageView) findViewById(R.id.imv_splash_02);
        mSplashIcon3 = (ImageView) findViewById(R.id.imv_splash_03);
        mSplashTxtWhite = (ImageView) findViewById(R.id.imv_splash_txt_white);
        mSplashTxtGreen = (ImageView) findViewById(R.id.imv_splash_txt_green);

        mSplashBlack = (RelativeLayout) findViewById(R.id.rl_layout_black);
        mSplashWhite = (RelativeLayout) findViewById(R.id.rl_layout_white);
        mSplashWhite.setAlpha(0.0f);
        mSplashIcon = (ImageView) findViewById(R.id.imv_splash_icon);
        mSplashMark = (ImageView) findViewById(R.id.imv_splash_mark);
        mSplashDes = (ImageView) findViewById(R.id.imv_splash_des);

        if (!pushLink.isEmpty())
            mSplash.setVisibility(View.GONE);
        else
            mSplash.setVisibility(View.VISIBLE);

        mRlSelectPhoto = (RelativeLayout) findViewById(R.id.rl_select_photo);
        findViewById(R.id.tv_select_camera).setOnClickListener(this);
        findViewById(R.id.tv_select_gallery).setOnClickListener(this);
        findViewById(R.id.tv_select_cancel).setOnClickListener(this);

        if (mMediaManager == null) {
            mMediaManager = new MediaManager(this);
            mMediaManager.setMediaCallback(new MediaManager.MediaCallback() {
                @Override
                public void onSelected(Boolean isVideo, File file, Bitmap bitmap, String videoPath, String thumbPath) {
                    if (file != null) {
                        selectedImages = new ArrayList<>();
                        selectedImages.add(file.getPath());
                        addImage();
                    }
                }

                @Override
                public void onFailed(int code, String err) {

                }

                @Override
                public void onDelete() {

                }
            });
        }

        requestPermission(this, permissions, REQUEST_CODE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("onTouchEvent", ">>>>>>> "+ev);
        return gestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClient(this, mWebView));

        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setAppCacheEnabled(true);

        gestureDetector = new GestureDetector(new CustomeGestureDetector());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(true);

        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        mWebView.getSettings().setTextZoom(100);    // 디바이스 폰트 사이즈 설정 무시하고 웹 폰트 사이즈 그대로 이용

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebSettings webViewSettings = mWebView.getSettings();
            webViewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        int screenHeight = mWebView.getRootView().getHeight();

                        int keypadHeight = screenHeight - r.bottom;
                        if (keypadHeight > screenHeight * 0.15) {
                            // keyboard is opened
                        } else {
                            // keyboard is closed
                            mWebView.postInvalidateDelayed(50);
                        }
                    }
                });

        mWebView.setWebChromeClient(new WebChromeClient() {
            // javaScript Alert 출력
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if (message == null || message.trim().length() == 0) {

                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(message)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();
                }
                return true;
            }

            // javaScript Confirm 출력
            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                if (message == null || message.trim().length() == 0) {

                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(message)
                            .setPositiveButton("확인",
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                            result.confirm();

                                        }
                                    })
                            .setNegativeButton("취소",
                                    new AlertDialog.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            result.cancel();
                                        }
                                    }).create().show();
                    //                           .setCancelable(false).create().show();
                }
                return true;
            }

            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
                // make sure there is no existing message
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePath;

                mRlSelectPhoto.setVisibility(View.VISIBLE);
                return true;
            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                mRlSelectPhoto.setVisibility(View.VISIBLE);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                mRlSelectPhoto.setVisibility(View.VISIBLE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                mRlSelectPhoto.setVisibility(View.VISIBLE);
            }

        });
    }


    private void onNext() {
        initWebView();
        //mWebView.addJavascriptInterface(new ContentManager(getApplicationContext()), "jobfriendapp");
        HeringsApplication application = (HeringsApplication) getApplicationContext();
        String url = Constants.WEB_URL + "?m_token=" + application.strFCMToken + "&m_no=";
        if (!pushLink.isEmpty()) {
            if (pushLink.contains("?")) {
                url = Constants.WEB_URL + pushLink + "&m_token=" + application.strFCMToken + "&m_no=";
            } else {
                url = Constants.WEB_URL + pushLink + "?m_token=" + application.strFCMToken + "&m_no=";
            }
        }
        if (!Util.hasUsim(application)) {
            mWebView.loadUrl(url);
        } else {
            mWebView.loadUrl(url + Util.getPhoneNumber(application));
        }

        if (!pushLink.isEmpty()) {
            mSplash.setVisibility(View.GONE);
        } else {
            startSplashAni();
        }
    }

    private void startSplashAni() {
        mSplash.setVisibility(View.VISIBLE);
        mSplashBg.setVisibility(View.GONE);
        mSplashIcon1.setVisibility(View.GONE);
        mSplashIcon2.setVisibility(View.GONE);
        mSplashIcon3.setVisibility(View.GONE);
        mSplashTxtWhite.setVisibility(View.GONE);
        mSplashTxtGreen.setVisibility(View.GONE);

        mHandler.sendEmptyMessage(MSG_ANI_STEP10);  // 실행이 끝난후 알림
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 500);  // 1 초 후에 실행
    }

    private void startSplashAniStep1() {
        Log.e("Herings", "start animation step 1");
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        mSplashIcon1.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_ANI_STEP2);  // 실행이 끝난후 알림
                    }
                }, 500);  // 1 초 후에 실행
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep2() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        mSplashIcon2.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_ANI_STEP3);  // 실행이 끝난후 알림
                    }
                }, 500); // 0.5초후
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep3() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        mSplashIcon3.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_ANI_STEP4);  // 실행이 끝난후 알림
                    }
                }, 500); // 0.5초후
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep4() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_anim);
        mSplashTxtWhite.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSplashTxtWhite.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_ANI_STEP5);  // 실행이 끝난후 알림
                    }
                }, 500); // 0.5초후
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep5() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        mSplashBg.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSplashBg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.sendEmptyMessage(MSG_ANI_STEP6);
                mSplashTxtWhite.setVisibility(View.GONE);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                          // 실행이 끝난후 알림
//                    }
//                }, 500); // 0.5초후
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep6() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_anim);
        mSplashTxtGreen.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSplashTxtGreen.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessage(MSG_ANI_STEP8);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep7() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        mSplashBg.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSplashBg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.sendEmptyMessage(MSG_ANI_STEP8);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep8() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.trans_anim);
        mSplashTxtGreen.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSplashTxtGreen.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessage(MSG_ANI_STEP9);  // 실행이 끝난후 알림
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep9() {
        mSplash.setVisibility(View.GONE);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        animation1.setDuration(1000);
        mSplash.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_anim);
        animation2.setDuration(2000);
        mWebView.startAnimation(animation2);
    }

    private void startSplashAniStep10() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_ANI_STEP11);  // 실행이 끝난후 알림
            }
        }, 500); // 0.5초후
    }

    private void startSplashAniStep11() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        animation.setDuration(2000);
        mSplashBlack.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSplashIcon.setVisibility(View.VISIBLE);
                mSplashDes.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSplashBlack.setVisibility(View.GONE);
                mHandler.sendEmptyMessage(MSG_ANI_STEP12);  // 실행이 끝난후 알림
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void startSplashAniStep12() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_anim);
        animation.setDuration(1500);
        mSplashWhite.startAnimation(animation);
        mSplashWhite.setAlpha(1.0f);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSplashWhite.setAlpha(1.0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSplashWhite.setAlpha(1.0f);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        animation2.setDuration(1500);
        mSplashDes.startAnimation(animation2);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSplashDes.setVisibility(View.GONE);
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
                animation1.setDuration(800);
                mSplashIcon.startAnimation(animation1);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mSplashIcon.setVisibility(View.GONE);
                        mHandler.sendEmptyMessage(MSG_ANI_STEP15);  // 실행이 끝난후 알림
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startSplashAniStep13() {
        mSplashIcon.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_ANI_STEP14);  // 실행이 끝난후 알림
            }
        }, 2000); // 0.5초후
    }

    private void startSplashAniStep14() {
        mSplashMark.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_ANI_STEP15);  // 실행이 끝난후 알림
            }
        }, 2000); // 0.5초후
    }

    private void startSplashAniStep15() {
        mSplash.setVisibility(View.GONE);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_anim);
        animation1.setDuration(1000);
        mSplash.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_anim);
        animation2.setDuration(2000);
        mWebView.startAnimation(animation2);
    }



    public void addImage() {
        if (selectedImages != null) {

        }
    }

    // Image 선택 팝업 관련
    void onClickGallery() {
        selectedImages = new ArrayList<>();
        mMediaManager.getMediaFromGallery(false);
        mRlSelectPhoto.setVisibility(View.GONE);
    }


    void onClickCamera() {
        selectedImages = new ArrayList<>();
        mMediaManager.getImageFromCamera();
        mRlSelectPhoto.setVisibility(View.GONE);

    }

    void onClickCancel() {
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (uploadMessage == null) return;
                    uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(0, null));
                } else {
                    Uri result = null;
                    mUploadMessage.onReceiveValue(result);
                }
                uploadMessage = null;
                selectedImages = null;
                mRlSelectPhoto.setVisibility(View.GONE);
            }
        }, 10);
    }


    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) { // 실행이 끝난후 확인 가능
            if (msg.what == MSG_ANI_STEP1) {
                startSplashAniStep1();
            } else if (msg.what == MSG_ANI_STEP2) {
                Log.e("Herings", "start animation step 2");
                startSplashAniStep2();
            } else if (msg.what == MSG_ANI_STEP3) {
                Log.e("Herings", "start animation step 3");
                startSplashAniStep3();
            } else if (msg.what == MSG_ANI_STEP4) {
                Log.e("Herings", "start animation step 4");
                startSplashAniStep4();
            } else if (msg.what == MSG_ANI_STEP5) {
                Log.e("Herings", "start animation step 5");
                startSplashAniStep5();
            } else if (msg.what == MSG_ANI_STEP6) {
                startSplashAniStep6();
            } else if (msg.what == MSG_ANI_STEP7) {
                startSplashAniStep7();
            } else if (msg.what == MSG_ANI_STEP8) {
                startSplashAniStep8();
            } else if (msg.what == MSG_ANI_STEP9) {
                startSplashAniStep9();
            } else if (msg.what == MSG_ANI_STEP10) {
                startSplashAniStep10();
            } else if (msg.what == MSG_ANI_STEP11) {
                startSplashAniStep11();
            } else if (msg.what == MSG_ANI_STEP12) {
                startSplashAniStep12();
            } else if (msg.what == MSG_ANI_STEP13) {
                startSplashAniStep13();
            } else if (msg.what == MSG_ANI_STEP14) {
                startSplashAniStep14();
            } else if (msg.what == MSG_ANI_STEP15) {
                startSplashAniStep15();
            }
        }

    };

    private boolean getPermissionCheckAllStatus(int[] grantResults) {
        boolean w_result = true;
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    w_result = false;
                    break;
                }
            }
        } else {
            w_result = false;
        }

        return w_result;
    }

    public boolean hasPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void requestPermission(Activity p_context, String[] p_requiredPermissions, int requestCode) {
        ActivityCompat.requestPermissions(p_context, p_requiredPermissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (getPermissionCheckAllStatus(grantResults)) {
                onNext();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST || requestCode == CAMERA_REQUEST) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (uploadMessage == null) return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessage = null;
            } else {
                if (null == mUploadMessage) return;
                Uri result = data == null || resultCode != RESULT_OK ? null
                        : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mRlSelectPhoto.getVisibility() == View.VISIBLE) {
            mWebView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if (uploadMessage == null) return;
                        uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(0, null));
                    } else {
                        Uri result = null;
                        mUploadMessage.onReceiveValue(result);
                    }
                    uploadMessage = null;
                    selectedImages = null;
                    mRlSelectPhoto.setVisibility(View.GONE);
                }
            }, 10);
            return;
        }

        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;
        if (intervalTime >= 0 && intervalTime <= 2000) {
            finish();
        } else {
            backPressedTime = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, "한번 더 누르면 앱을 종료합니다.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_camera:
                onClickCamera();
                break;
            case R.id.tv_select_gallery:
                onClickGallery();
                break;
            case R.id.tv_select_cancel:
                onClickCancel();
                break;
        }
    }
}
