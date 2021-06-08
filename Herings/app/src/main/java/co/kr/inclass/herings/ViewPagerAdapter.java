package co.kr.inclass.herings;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewHolderPager> {

    private ArrayList<DataPage> listData;
    private Activity parentActivity;
    ViewPagerAdapter(ArrayList<DataPage> data, Activity activity) {
        this.listData = data;
        this.parentActivity = activity;
    }

    @Override
    public ViewHolderPager onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPager(view, this.parentActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolderPager holder, int position) {
        if(holder instanceof ViewHolderPager){
            ViewHolderPager viewHolder = (ViewHolderPager) holder;
            viewHolder.onBind(listData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
