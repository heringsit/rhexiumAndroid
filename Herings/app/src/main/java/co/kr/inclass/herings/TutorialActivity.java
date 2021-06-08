package co.kr.inclass.herings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
//import android.view.GestureDetector;

public class TutorialActivity extends AppCompatActivity {

//    private GestureDetector gestureDetector;
//    private CustomeGestureDetector customeGestureDetector;
    ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        viewPager2 = findViewById(R.id.viewPager);
        ArrayList<DataPage> list = new ArrayList<>();
        list.add(new DataPage(R.drawable.app_tu_01,true,0));
        list.add(new DataPage(R.drawable.app_tu_02, false, 1));
        list.add(new DataPage(R.drawable.app_tu_03,false,2));
        list.add(new DataPage(R.drawable.app_tu_04, false,3));
        list.add(new DataPage(R.drawable.app_tu_05,false,4));
        list.add(new DataPage(R.drawable.app_tu_06, false,5));
        list.add(new DataPage(R.drawable.app_tu_07,false,6));
        list.add(new DataPage(R.drawable.app_tu_08, false,7));
        list.add(new DataPage(R.drawable.app_tu_09,false,8));
        list.add(new DataPage(R.drawable.app_tu_10, true,9));

        viewPager2.setAdapter(new ViewPagerAdapter(list, this));

//        ImageView imgView = (ImageView)findViewById(R.id.tutorialImg);
//        imgView.setImageResource(R.drawable.app_tu_01);
//        customeGestureDetector = new CustomeGestureDetector();
//        gestureDetector = new GestureDetector(customeGestureDetector);
//        imgView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("kkk", "direction => " + customeGestureDetector.direction);
//                return gestureDetector.onTouchEvent(event) || true;
//            }
//        });

    }
}
