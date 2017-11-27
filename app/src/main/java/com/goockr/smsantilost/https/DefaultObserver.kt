package com.goockr.smsantilost.https

import android.content.Context
import com.goockr.smsantilost.views.activities.BaseActivity
import rx.Observer

/**
 * Created by Administrator on 2017/10/15.
 */

abstract class DefaultObserver<T>(private val context: Context) : Observer<T> {
    private val baseBctivity: BaseActivity

    init {
        baseBctivity = this.context as BaseActivity
    }

    override fun onNext(response: T) {
        //        if (((BaseEntity) response).getErrorcode() == 401) {
        //            baseBctivity.preferences.putValue(IS_LOGIN, false);
        //            baseBctivity.showActivityByFlags(LoginActivity.class, Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //            MyToast.showToastCustomerStyleText(baseBctivity, "登录过期");
        //        }
        baseBctivity.dismissDialog()
    }

    override fun onError(e: Throwable) {
        baseBctivity.dismissDialog()
    }

    override fun onCompleted() {

    }
}
