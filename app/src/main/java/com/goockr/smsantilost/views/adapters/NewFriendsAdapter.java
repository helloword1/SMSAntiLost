package com.goockr.smsantilost.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goockr.smsantilost.R;
import com.goockr.smsantilost.entries.FriendsListBean;
import com.goockr.smsantilost.utils.StringToInt;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by yuyouming on 2017/11/27.
 */

public class NewFriendsAdapter extends BaseAdapter implements StickyListHeadersAdapter{

    private Context mContext;
    private List<FriendsListBean> mLists;
    private LayoutInflater mInflater;

    public void setmLists(List<FriendsListBean> mLists) {
        this.mLists = mLists;
    }

    public NewFriendsAdapter(Context context, List<FriendsListBean> list) {
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
        FriendsHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_new_friends,parent,false);
            holder = new FriendsHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (FriendsHolder) convertView.getTag();
        }
        FriendsListBean friendsListBean = mLists.get(position);
        Glide.with(mContext).load(friendsListBean.getIcon()).into(holder.icon);
        holder.name.setText(friendsListBean.getName());
        holder.num.setText(friendsListBean.getNum());
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.header_friends_list,parent,false);
            holder = new HeaderHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (HeaderHolder) convertView.getTag();
        }
        holder.header.setText(mLists.get(position).getFirstChar());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return StringToInt.getHeaderId(mLists.get(position).getFirstChar());
    }


    class FriendsHolder {
        CircleImageView icon;
        TextView name;
        TextView num;

        public FriendsHolder(View itemView) {
            icon = itemView.findViewById(R.id.iv_friends_icon);
            name = itemView.findViewById(R.id.tv_friends_name);
            num = itemView.findViewById(R.id.tv_friends_num);
        }
    }

    class HeaderHolder {
        TextView header;

        public HeaderHolder(View itemView) {
            header = itemView.findViewById(R.id.tv_header);
        }
    }
}
