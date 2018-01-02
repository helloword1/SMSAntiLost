package com.goockr.smsantilost.views.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.smsantilost.R;
import com.goockr.smsantilost.entries.AntilostBean;

import java.util.List;

/**
 * Created by tanzhihao on 2017/11/20.
 */

public class AntilostAdapter extends BaseAdapter {

    private List<AntilostBean> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public AntilostAdapter(List<AntilostBean> list, Context context) {
        this.mList = list;
        mContext = context;
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
        AntilostHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_antilost, parent, false);
            holder = new AntilostHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AntilostHolder) convertView.getTag();
        }
        AntilostBean antilostBean = mList.get(position);
        holder.mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, getDeviceID(antilostBean.getDrawableId())));
        holder.mName.setText(antilostBean.getName());
        holder.mLastDate.setText(antilostBean.getLastDate());
        if (antilostBean.isConnectState()) {
            holder.mConnnect.setText(R.string.hadConnect);
        } else {
            holder.mConnnect.setText(R.string.disConnect);
        }
        String distance = antilostBean.getDistance();
        if (Double.valueOf(distance) > -70) {
            distance = "距离近";
        } else {
            distance = "距离远";
        }
        holder.tvDistance.setText(distance);
        return convertView;
    }

    class AntilostHolder {
        // 图标
        ImageView mImageView;
        // 钥匙
        TextView mName;
        // 距离最近（日期）
        TextView mLastDate;
        // 已连接
        TextView mConnnect;
        TextView tvDistance;

        public AntilostHolder(View view) {
            mImageView = view.findViewById(R.id.antilost_item_icon);
            mName = view.findViewById(R.id.antilost_item_name);
            mLastDate = view.findViewById(R.id.antilost_item_lastDate);
            mConnnect = view.findViewById(R.id.antilost_item_connect);
            tvDistance = view.findViewById(R.id.tvDistance);
        }
    }

    private int getDeviceID(int type) {
        int mType = 0;
        switch (type) {
            case 0:
                mType = R.mipmap.icon_key;
                break;
            case 1:
                mType = R.mipmap.icon_wallet;
                break;
            case 2:
                mType = R.mipmap.icon_portable_computer;
                break;
            case 3:
                mType = R.mipmap.icon_vice_card_phone;
                break;
            case 4:
                mType = R.mipmap.icon_other;
                break;
            default:
                break;
        }
        return mType;
    }
}
