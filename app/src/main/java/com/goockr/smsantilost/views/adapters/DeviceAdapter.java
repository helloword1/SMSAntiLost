package com.goockr.smsantilost.views.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.smsantilost.R;

import java.util.List;

/**
 * Created by tanzhihao on 2017/11/20.
 */

public class DeviceAdapter extends BaseAdapter {

    private List<BluetoothDevice> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public DeviceAdapter(List<BluetoothDevice> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.item_device,parent,false);
            holder = new AntilostHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (AntilostHolder) convertView.getTag();
        }
        holder.mDeviceName.setText(mList.get(position).getName());
        return convertView;
    }

    class AntilostHolder {
        // 图标
        ImageView mDeviceIcon;
        // 钥匙
        TextView mDeviceName;
        // 型号强弱
        ImageView mDeviceSignal;

        public AntilostHolder(View view) {
            mDeviceIcon = view.findViewById(R.id.iv_DeviceIcon);
            mDeviceName = view.findViewById(R.id.tv_DeviceName);
            mDeviceSignal = view.findViewById(R.id.iv_DeviceSignal);
        }
    }
}
