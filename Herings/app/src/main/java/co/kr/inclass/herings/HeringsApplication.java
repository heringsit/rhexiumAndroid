package co.kr.inclass.herings;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;

import co.kr.inclass.herings.model.UserInfo;
import co.kr.inclass.herings.pref.PrefMgr;

public class HeringsApplication extends Application {

    private static HeringsApplication instance = null;
    private PrefMgr mPrefMgr = null;

    public MainActivity gMainActivity = null;

    // FCM 토큰값
    public String strFCMToken = "";
    public String initialInstallTime ="";

    /**
     * singleton 애플리케이션 객체를 얻는다.
     *
     * @return singleton 애플리케이션 객체
     */
    public static HeringsApplication getGlobalApplicationContext( ) {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate( ) {
        super.onCreate();
        instance = this;
        mPrefMgr = new PrefMgr();

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Application", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        strFCMToken = task.getResult().getToken();
                        initialInstallTime = new Date().getTime()+"";
                        Log.w(" strFCMToken >>>>> ", strFCMToken  );
                        mPrefMgr.setDevToken(strFCMToken);
                        mPrefMgr.setInitialInstallTime(initialInstallTime);
                    }
                });
                if(mPrefMgr.getDevToken() == ""){
                    strFCMToken = FirebaseInstanceId.getInstance().getToken();
                    mPrefMgr.setDevToken(strFCMToken);
                }
                strFCMToken = mPrefMgr.getDevToken();
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate( ) {
        super.onTerminate();
        instance = null;
    }

    public static HeringsApplication getInstance() {
        return instance;
    }

    public PrefMgr getPreference() {
        return mPrefMgr;
    }

    public void setPushToken(String token) {
        strFCMToken = token;
        mPrefMgr.setDevToken(strFCMToken);
    }
}
