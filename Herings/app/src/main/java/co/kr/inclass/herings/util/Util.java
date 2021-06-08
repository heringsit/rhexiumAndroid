package co.kr.inclass.herings.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.kr.inclass.herings.Constants;
import co.kr.inclass.herings.MainActivity;

public class Util {
    public static ProgressDialog m_dlgProgress = null;
    private static int m_nProgressCnt = 0;

    public static int dpToPixel(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return (int) px;
    }

    public static int pixelToDp(Context context, int pixel) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = pixel / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, String MESSAGE) {
        Toast.makeText(context, MESSAGE, Toast.LENGTH_SHORT).show();
    }

    // apply diffrent styles to text
    public static SpannableStringBuilder setTextStyle(String szTxt, int nStyle,
                                                      Object nColor, int nSize) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.clear();

        if (szTxt == null || szTxt.equals("")) {
            return ssb;
        }

        ssb.append(szTxt); // attention : 본문에 "."이 들어가면 행바꾸기됩니다.
        try {
            ssb.setSpan(nStyle /*new StyleSpan(nStyle)*/, 0, szTxt.length(),
                    Spannable.SPAN_COMPOSING); // nStyle : Typeface.BOLD_ITALIC
            ssb.setSpan(nColor, 0, szTxt.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new AbsoluteSizeSpan(nSize), 0, szTxt.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            Log.d("", "setTextStyle() -->  " + e.getMessage());
        }

        return ssb;
    }

    // apply diffrent styles to text
    public static SpannableStringBuilder setTextStyle(String szTxt, int nStyle,
                                                      Object nColor) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.clear();

        if (szTxt == null || szTxt.equals("")) {
            return ssb;
        }

        ssb.append(szTxt); // attention : 본문에 "."이 들어가면 행바꾸기됩니다.
        try {
            ssb.setSpan(nStyle /*new StyleSpan(nStyle)*/, 0, szTxt.length(),
                    Spannable.SPAN_COMPOSING); // nStyle : Typeface.BOLD_ITALIC
            ssb.setSpan(nColor, 0, szTxt.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            Log.d("", "setTextStyle() -->  " + e.getMessage());
        }

        return ssb;
        // textView.append(setTextStyle("테스트.스타일", Typeface.BOLD_ITALIC,
        // Color.RED, 22));
    }

    // get current date-time string name
    public static String getDateTimeString( ) {
        return String.format("%d%d%d%d%d%d", Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND));
    }

    public static String getStringTimeFormat_00_00_00(int second) {
        int h = second / 3600;
        int m = (second % 3600) / 60;
        int s = second % 60;

        if (h == 0) {
            return String.format(Locale.getDefault(), "%02d : %02d", m, s);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
        }
    }

    // get appversion
    public static String getVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


        public static String getDeviceModel(Context context) {
        String deviceModel = Build.MODEL;
        return deviceModel;
    }

    public static void goToAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void hideProgress( ) {
        m_nProgressCnt--;
        if (m_nProgressCnt > 0)
            return;
        if (m_dlgProgress != null && m_dlgProgress.isShowing()) {
            m_dlgProgress.dismiss();
            m_dlgProgress.hide();
        }
        m_dlgProgress = null;
    }

    public static int getScreenWidth(@NonNull Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static void callPhone(Context context, String phone) {
        Uri number = Uri.parse("tel:" + phone);
        Intent i = new Intent(Intent.ACTION_DIAL, number);
        context.startActivity(i);
    }

    public static double getDiffTime(String start) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
            Date date = simpleDateFormat.parse(start);
            return ((new Date().getTime()) - date.getTime()) / 1000;

        } catch (ParseException e) {
            return 0;
        }
    }

    public static int getDiffMinute(String start) {
        try {

            String current_time = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(new Date());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
            Date date = simpleDateFormat.parse(start);

            return (int)((((simpleDateFormat.parse(current_time).getTime()) - date.getTime()) / 1000)/60);

        } catch (ParseException e) {
            return 0;
        }
    }

//    @SuppressLint("HardwareIds")
//    public static String getPhoneNumber(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return "";
//        }
//        String phoneNumber = telephonyManager == null || TextUtils.isEmpty(telephonyManager.getLine1Number()) ?
//                "" :
//                telephonyManager.getLine1Number();
//        if (phoneNumber.startsWith("+82")) {
//            phoneNumber = "0" + phoneNumber.substring(3);
//        }
//
//        phoneNumber = phoneNumber.replace("-", "");
//        phoneNumber = phoneNumber.replace(" ", "");
//        // Test
//        phoneNumber = "01000000000";
//        return phoneNumber;
//    }

    public static boolean hasUsim(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager != null ? telephonyManager.getSimState() : TelephonyManager.SIM_STATE_UNKNOWN;
        return simState != TelephonyManager.SIM_STATE_ABSENT && simState != TelephonyManager.SIM_STATE_UNKNOWN;
    }


    public static void saveBitmapToFile(MainActivity activity, Bitmap bitmap, String filepath) {
        if (bitmap == null || filepath.isEmpty()) {
            return;
        }

//        if (activity.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        OutputStream fOut = null;
        File file = new File(filepath);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        } else {
//            activity.requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x11);
//        }
    }

    public static File createFile() {
        File folder = new File(getFolderPath());
        if (!folder.exists())
            folder.mkdirs();

        Long tsLong = System.currentTimeMillis() / 1000;
        String ext = ".png";
        String filename = tsLong.toString() + ext;
        return new File(folder.toString(), filename);
    }

    private static String getFolderPath() {
        return Environment.getExternalStorageDirectory() + "/" + Constants.SDCARD_FOLDER;
    }
}
