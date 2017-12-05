package com.goockr.smsantilost.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.smsantilost.R;
import com.goockr.smsantilost.entries.AreaBean;
import com.goockr.smsantilost.views.activities.more.AntiDisturbActivity;
import com.goockr.smsantilost.views.activities.more.AreaMapActivity;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * Created by yuyouming on 2017/11/27.
 */

public class AreaAdapter extends BaseAdapter {

    private Context mContext;
    private List<AreaBean> mLists;
    private LayoutInflater mInflater;

    public void setmLists(List<AreaBean> mLists) {
        this.mLists = mLists;
    }

    public AreaAdapter(Context context, List<AreaBean> list) {
        mContext = context;
        mLists = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mLists != null && mLists.size() > 0){
            return mLists.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AreaHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_area,parent,false);
            holder = new AreaHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (AreaHolder) convertView.getTag();
        }
        holder.areaName.setText(mLists.get(position).getAreaName());
        holder.areaRemark.setText(mLists.get(position).getAreaRemark());
        if (position == mLists.size() - 1) {
            holder.swipeMenuLayout.setSwipeEnable(false);
            holder.areaRemark.setVisibility(View.GONE);
        }
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(((AntiDisturbActivity) mContext), AreaMapActivity.class);
                mContext.startActivity(i);
            }
        });
        return convertView;
    }

    class AreaHolder {
        TextView areaName;
        TextView areaRemark;
        SwipeMenuLayout swipeMenuLayout;
        LinearLayout content;

        public AreaHolder(View itemView) {
            areaName = itemView.findViewById(R.id.tv_area_name);
            areaRemark = itemView.findViewById(R.id.tv_area_remark);
            swipeMenuLayout = itemView.findViewById(R.id.sml_content);
            content = itemView.findViewById(R.id.ll_content);
        }
    }
}
