package com.goockr.smsantilost.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.smsantilost.R;
import com.goockr.smsantilost.entries.RepeatBean;

import java.util.List;

/**
 * Created by yuyouming on 2017/11/28.
 */

public class RepeatAdapter extends BaseAdapter {

    private List<RepeatBean> mDate;
    private Context mContext;
    private LayoutInflater mInflater;

    public RepeatAdapter(List<RepeatBean> mDate, Context mContext) {
        this.mDate = mDate;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mDate != null && mDate.size() > 0) {
            return mDate.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RepeatHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_repeat,parent,false);
            holder = new RepeatHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (RepeatHolder) convertView.getTag();
        }
        holder.date.setText(mDate.get(position).getDate());
        if (mDate.get(position).isSelected()) {
            holder.selected.setVisibility(View.VISIBLE);
        }else {
            holder.selected.setVisibility(View.GONE);
        }
        return convertView;
    }

    class RepeatHolder {
        TextView date;
        ImageView selected;

        public RepeatHolder(View view) {
            this.date = view.findViewById(R.id.tv_repeat_date);
            this.selected = view.findViewById(R.id.iv_repeat_selected);
        }
    }
}
