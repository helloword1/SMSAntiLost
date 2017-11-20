package com.goockr.smsantilost.https;

import android.content.Context;

import com.goockr.smsantilost.views.activities.BaseActivity;

import rx.Observer;

/**
 * Created by Administrator on 2017/10/15.
 */

public abstract class DefaultObserver<T> implements Observer<T> {
    private Context context;
    private final BaseActivity baseBctivity;

    public DefaultObserver(Context context) {
        this.context = context;
        baseBctivity = (BaseActivity) this.context;
    }

    @Override
    public void onNext(T response) {
//        if (((BaseEntity) response).getErrorcode() == 401) {
//            baseBctivity.preferences.putValue(IS_LOGIN, false);
//            baseBctivity.showActivityByFlags(LoginActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            MyToast.showToastCustomerStyleText(baseBctivity, "登录过期");
//        }
        baseBctivity.dismissDialog();
    }

    @Override
    public void onError(Throwable e) {
        baseBctivity.dismissDialog();
    }

    @Override
    public void onCompleted() {

    }
}
