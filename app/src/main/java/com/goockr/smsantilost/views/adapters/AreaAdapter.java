package com.goockr.smsantilost.views.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.smsantilost.R;
import com.goockr.smsantilost.entries.AntiAddressBean;
import com.goockr.smsantilost.utils.Constant;
import com.goockr.smsantilost.views.activities.antilost.DeviceMapActivity;
import com.goockr.smsantilost.views.activities.more.AddAntiAreaMapActivity;
import com.goockr.smsantilost.views.activities.more.AntiDisturbActivity;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * Created by yuyouming on 2017/11/27.
 */

public class AreaAdapter extends BaseAdapter {
    private final int END_TYPE = -1;
    private final int TYPE = 0;
    private Context mContext;
    private List<AntiAddressBean> mLists;
    private LayoutInflater mInflater;
    private OnSwipeDeleteListener listener;

    public void setmLists(List<AntiAddressBean> mLists) {
        this.mLists = mLists;
    }

    public AreaAdapter(Context context, List<AntiAddressBean> list) {
        mContext = context;
        mLists = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mLists.size() - 1) {
            return END_TYPE;
        } else {
            return TYPE;
        }
    }

    @Override
    public AntiAddressBean getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AntiAddressBean addressBean = mLists.get(position);
        int itemViewType = getItemViewType(position);
        if (itemViewType == END_TYPE) {
            convertView = mInflater.inflate(R.layout.item_area_add, parent, false);
            ((TextView) convertView.findViewById(R.id.tv_area_name_add)).setText(addressBean.getName());
            convertView.findViewById(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_NAME(), "");
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_ID(), "0");
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_LATITUDE(), "0");
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_LONGITUDE(), "0");
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_ADDRESS(), "");
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_RADUIS(), "100");
                    ((AntiDisturbActivity) mContext).showActivity(AddAntiAreaMapActivity.class, bundle);
                }
            });
        } else {
            AreaHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_area, parent, false);
                holder = new AreaHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (AreaHolder) convertView.getTag();
            }

            holder.areaName.setText(addressBean.getName());
            holder.areaRemark.setText(addressBean.getRemark());
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.INSTANCE.getLONGITUDE(), addressBean.getLongitude());
                    bundle.putString(Constant.INSTANCE.getLATITUDE(), addressBean.getLatitude());
                    bundle.putString(Constant.INSTANCE.getADDRESS(), addressBean.getRemark());
                    bundle.putInt(Constant.INSTANCE.getADDRESS_TYPE(), 5);
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_ID(), addressBean.getId().toString());
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_RADUIS(), addressBean.getRadius());
                    bundle.putString(Constant.INSTANCE.getCURRENT_AREA_NAME(), addressBean.getName());
                    ((AntiDisturbActivity) mContext).showActivity(DeviceMapActivity.class, bundle);
                }
            });
            final AreaHolder finalHolder = holder;
            holder.btn_Del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.swipeDeleteListener(position, finalHolder.swipeMenuLayout);
                }
            });
        }


        return convertView;
    }

    class AreaHolder {
        TextView areaName;
        TextView areaRemark;
        SwipeMenuLayout swipeMenuLayout;
        LinearLayout content;
        Button btn_Del;

        AreaHolder(View itemView) {
            areaName = itemView.findViewById(R.id.tv_area_name);
            areaRemark = itemView.findViewById(R.id.tv_area_remark);
            swipeMenuLayout = itemView.findViewById(R.id.sml_content);
            content = itemView.findViewById(R.id.ll_content);
            btn_Del = itemView.findViewById(R.id.btn_Del);
        }
    }
    public interface  OnSwipeDeleteListener{
        void swipeDeleteListener(int position, SwipeMenuLayout swipeMenuLayout);
    }
    public void setOnSwipeDeleteListener(OnSwipeDeleteListener listener){
        this.listener=listener;
    }

}
