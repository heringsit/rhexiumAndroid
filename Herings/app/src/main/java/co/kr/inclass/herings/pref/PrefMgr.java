package co.kr.inclass.herings.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import co.kr.inclass.herings.HeringsApplication;
import co.kr.inclass.herings.model.UserInfo;

public class PrefMgr {
    private static final String SP_LOGIN_INFO = "sp_login_info";
    private static final String SP_DEV_TOKEN = "sp_dev_token";
    private static final String SP_SPLASH_IDX = "sp_splash_idx";

    // 알람 정보
    private static final String SP_ALARM_NOTI = "sp_alarm_noti";
    private static final String SP_ALARM_NOTI_EXCLUDED = "sp_alarm_noti_excluded";
    private static final String SP_ALARM_NOTI_FIXED = "sp_alarm_noti_fixed";
    private static final String SP_GO_WORK_TIME = "sp_go_work_time";
    private static final String SP_OFF_WORK_TIME = "sp_off_work_time";

    // 출퇴근정보
    private static final String FLAG_GO_WORK = "flag_go_work"; //
    private static final String FLAG_OFF_WORK = "flag_go_work"; //
    private static final String CHECK_GO_WORK_DATE = "check_go_work_date"; //
    private static final String CHECK_OFF_WORK_DATE = "check_off_work_date"; //
    private static final String CHECK_GO_WORK_TIME = "check_go_work_time"; // 출근시간분

    private static final String SP_EXCLUDED_START_TIME = "sp_excluded_start_time"; //
    private static final String SP_EXCLUDED_END_TIME = "sp_excluded_end_time"; //

    private static final String SP_FIXED_NOTI_HOUR = "sp_fixed_noti_hour"; //
    private static final String SP_FIXED_NOTI_MINUTE = "sp_fixed_noti_minute"; //

    // 출퇴근 알람을 놀출했댔는지 날짜 저장
    private static final String CHECK_MSG_GO_WORK_DATE = "check_msg_go_work_date"; //
    private static final String CHECK_MSG_OFF_WORK_DATE = "check_msg_off_work_date"; //

    // 메시지 노출시 령역체크상태
    private static final String IS_ABLE_CHECK = "is_able_check"; //

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    public PrefMgr() {
        String name = HeringsApplication.getInstance().getPackageName() + "-preference";
        sharedPreferences = HeringsApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginInfo(UserInfo user) {
        String strUser = "";
        if (user == null) {
            editor.putString(SP_LOGIN_INFO, strUser).apply();
            return;
        }

        strUser = new Gson().toJson(user, UserInfo.class);
        editor.putString(SP_LOGIN_INFO, strUser).apply();
    }

    public UserInfo getLoginUser() {
        String strUser = sharedPreferences.getString(SP_LOGIN_INFO, "");

        if (strUser.isEmpty() == true) {
            return null;
        }

        UserInfo user = new Gson().fromJson(strUser, UserInfo.class);

        return user;
    }

    public void setDevToken(String token) {
        editor.putString(SP_DEV_TOKEN, token).apply();
    }

    public String getDevToken() {
        return sharedPreferences.getString(SP_DEV_TOKEN, "");
    }

    public void setSpAlarmNoti(int value) {
        editor.putInt(SP_ALARM_NOTI, value).apply();
    }

    public int getSpAlarmNoti() {
        return sharedPreferences.getInt(SP_ALARM_NOTI, 0);
    }

    public void setSpAlarmNotiExcluded(int value) {
        editor.putInt(SP_ALARM_NOTI_EXCLUDED, value).apply();
    }

    public int getSpAlarmNotiExcluded() {
        return sharedPreferences.getInt(SP_ALARM_NOTI_EXCLUDED, 0);
    }

    public void setSpAlarmNotiFixed(int value) {
        editor.putInt(SP_ALARM_NOTI_FIXED, value).apply();
    }

}
