package com.goockr.smsantilost.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.smsantilost.R;
import com.goockr.smsantilost.entries.PhoneSoundBean;

import java.util.List;

/**
 * Created by tanzhihao on 2017/11/21.
 */

public class PhoneSoundAdapter extends BaseAdapter {

    private List<PhoneSoundBean> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mCurrentPosition = 0;

    public PhoneSoundAdapter(Context context, List<PhoneSoundBean> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        SoundHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_phone_sound, parent, false);
            holder = new SoundHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SoundHolder) convertView.getTag();
        }
        holder.name.setText(mList.get(position).getName());
        if (position == mCurrentPosition) {
            holder.selected.setVisibility(View.VISIBLE);
        }else {
            holder.selected.setVisibility(View.GONE);
        }
        return convertView;
    }

    class SoundHolder {
        TextView name;
        ImageView selected;

        public SoundHolder(View view) {
            name = view.findViewById(R.id.SoundName);
            selected = view.findViewById(R.id.SoundSelected);
        }
    }


    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
    }
}
