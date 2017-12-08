package com.goockr.smsantilost.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.smsantilost.R;
import com.goockr.smsantilost.entries.SleepTimeBean;
import com.goockr.smsantilost.utils.FileCache;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyouming on 2017/11/29.
 */

public class SleepTimeAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SleepTimeBean> mList;
    private LayoutInflater mInflater;
    private FileCache mCache;

    public SleepTimeAdapter(Context mContext, ArrayList<SleepTimeBean> mList, FileCache cache) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
        this.mCache = cache;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SleepTimeHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_sleep_time, parent, false);
            holder = new SleepTimeHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SleepTimeHolder) convertView.getTag();
        }
        SleepTimeBean bean = mList.get(position);
        holder.name.setText(bean.getSleepTimeName());
        holder.duration.setText(bean.getSleepTimeDuration());
        holder.repeat.setText(bean.getSleepTimeRepeat());
        final SleepTimeHolder finalHolder = holder;
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                mCache.put("sleep", mList);
                finalHolder.sml.smoothClose();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class SleepTimeHolder {
        TextView name;
        TextView duration;
        TextView repeat;
        Button del;
        SwipeMenuLayout sml;

        public SleepTimeHolder(View view) {
            this.name = view.findViewById(R.id.tv_time_name);
            this.duration = view.findViewById(R.id.tv_duration);
            this.repeat = view.findViewById(R.id.tv_repeat);
            this.del = view.findViewById(R.id.btn_Del);
            this.sml = view.findViewById(R.id.sml_sleep_time);
        }
    }
}
