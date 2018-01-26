package com.goockr.smsantilost.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.graphics.GlideCircleTransform
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.USER_NAME
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.more.*
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.fragment_more.*
import okhttp3.Call
import org.json.JSONObject
import java.lang.Exception


/**
 * Created by ning.wen on 2016/11/1.
 */

class MoreFragment : BaseFragment(), View.OnClickListener {
    private var imageUrl = ""
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return setContentView(R.layout.fragment_more)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickEvent()
        getIcon()
    }

    private fun initView() {

    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        llUserIcon.setOnClickListener(this)
        ll_MyFriends.setOnClickListener(this)
        ll_AntiDisturb.setOnClickListener(this)
        ll_SleepTime.setOnClickListener(this)
        ll_UserSetting.setOnClickListener(this)
        ll_AboutUs.setOnClickListener(this)
        llMultilingualLanguage.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        val baseActivity = activity as BaseActivity
        val name = baseActivity.preferences?.getStringValue(USER_NAME)
        if (NotNull.isNotNull(name)) {
            tv_UserName.text = name
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llUserIcon -> {
                //我的头像
                val bundle = Bundle()
                bundle.putString("IMAGE_URL", imageUrl)
                val intent = Intent(activity, UserSettingActivity::class.java)
                intent.putExtras(bundle)
                startActivityForResult(intent, 112)

            }
            R.id.ll_MyFriends -> {
                //我的好友
                showActivity(MyFriendsActivity::class.java)
            }
            R.id.ll_AntiDisturb -> {
                //防打扰区域
                showActivity(AntiDisturbActivity::class.java)
            }
            R.id.ll_SleepTime -> {
                //休眠时间
                showActivity(SleepTimeActivity::class.java)
            }
            R.id.llMultilingualLanguage -> {
                //多国语言
                showActivity(MultilingualLanguageActivity::class.java)
            }
            R.id.ll_UserSetting -> {
                //设置
                showActivity(MoreSettingActivity::class.java)
            }
            R.id.ll_AboutUs -> {
                //关于我们
                showActivity(AboutUsActivity::class.java)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 112) {
            val extra = data?.getStringExtra("imageUri")
            val imageUri = Uri.parse(extra)
            if (!TextUtils.equals(extra, "null")) {
                Glide.with(activity.applicationContext).load(imageUri).transform(GlideCircleTransform(activity)).placeholder(R.mipmap.icon_head_portrait).into(iv_ProfilePic1)
            }
//            getIcon()
        }
    }

    private fun getIcon() {
        val value = activity as BaseActivity
        val token = value.preferences?.getStringValue(Constant.TOKEN)
        val url = Constant.BASE_URL + NetApi.LOADHEAD_IMG
        if (!NotNull.isNotNull(token)) return
        OkHttpUtils.post()?.url(url)?.
                addParams("token", token)?.
                build()?.execute(object : MyStringCallback(activity) {
            override fun onResponse(response: String?, id: Int) {
                LogUtils.mi(response!!)
                if (!NotNull.isNotNull(response)) return
                val jsonObject = JSONObject(response)
                val imgUrl = jsonObject.getString("imgUrl")
                if (NotNull.isNotNull(imgUrl)&&NotNull.isNotNull(activity)) {
                    imageUrl = imgUrl
                    activity.runOnUiThread {
                        Glide.with(activity.applicationContext).load(imgUrl).transform(GlideCircleTransform(activity)).placeholder(R.mipmap.icon_head_portrait).into(iv_ProfilePic1)
                    }
                }
            }

            override fun onError(call: Call?, e: Exception?, id: Int) {
                LogUtils.mi(e.toString())
            }
        })
    }
}
