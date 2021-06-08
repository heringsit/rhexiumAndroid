package co.kr.inclass.herings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class ViewHolderPager extends RecyclerView.ViewHolder {
    private ImageView imgView, imgStartView;
    private Button closeBtn;
    private CheckBox notToday;
    private RelativeLayout notTodayArea;
    private RelativeLayout wholeLayout;
    DataPage data;

    private Activity activity;
    ViewHolderPager(View itemView, final Activity activity) {
        super(itemView);
        wholeLayout = itemView.findViewById(R.id.wholeLayout);
        imgView = itemView.findViewById(R.id.imgView);
        imgStartView = itemView.findViewById(R.id.imgStartArea);
        closeBtn = itemView.findViewById(R.id.button);
        notToday = itemView.findViewById(R.id.checkNotToday);
        notTodayArea = itemView.findViewById(R.id.notTodayArea);
        this.activity = activity;
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        imgStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        notToday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Calendar today = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SharedPreferences prefs = activity.getSharedPreferences("shared", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if(isChecked){
                    editor.putString("notToday", sdf.format(today.getTime()));  // 오늘 저장
                    editor.commit();
                    Log.d("kkk", "notToday => " + prefs.getString("notToday", ""));

                }else{
                    editor.putString("notToday", "");  // 삭제
                    editor.commit();
                    Log.d("kkk", "notToday non check => " + prefs.getString("notToday", ""));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBind(DataPage data){
        this.data = data;
//        imgView.setImageResource(data.getImgResource());
        wholeLayout.setBackground(this.activity.getDrawable(data.getImgResource()));
        if(!data.notToday){
            notTodayArea.setVisibility(View.GONE);
        }else{
            notTodayArea.setVisibility(View.VISIBLE);
        }
        if(this.data.number == 0){
            closeBtn.setBackground(ContextCompat.getDrawable(this.activity, R.drawable.app_close_g));
        }else{
            closeBtn.setBackground(ContextCompat.getDrawable(this.activity, R.drawable.app_close));
        }

        if(this.data.number == 9){
            imgStartView.setVisibility(View.VISIBLE);
        }else{
            imgStartView.setVisibility(View.GONE);
        }
    }
}
