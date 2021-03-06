package com.goockr.smsantilost.views.activities.more

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import com.bumptech.glide.Glide
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.graphics.GlideCircleTransform
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.graphics.PhotoAlertDialog
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.IMAGE_CHIOCE
import com.goockr.smsantilost.utils.Constant.IMAGE_CROP
import com.goockr.smsantilost.utils.Constant.TAKE_PHOTO
import com.goockr.smsantilost.utils.Constant.TOKEN
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.login.LoginActivity
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_user_setting.*
import okhttp3.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Exception


/**
 * 用户设置页面，头像，密码，手机登
 */
class UserSettingActivity(override val contentView: Int = R.layout.activity_user_setting) : BaseActivity(), View.OnClickListener {

    private var bottomDialog: PhotoAlertDialog? = null
    private var myAlertDialog: MyAlertDialog? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initBottomDialog()
        initDialog()
        initClickEvent()
    }

    /**
     * 初始化title
     */
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        title?.text = getString(R.string.accountSetting)
        titleBack?.setOnClickListener {
            setResult(Activity.RESULT_OK,Intent().putExtra("imageUri",imageUri.toString()))
            finish()
        }
        ll?.addView(titleLayout)
        val extras = intent.extras
        val image = extras.getString("IMAGE_URL")
        if (NotNull.isNotNull(image)) {
            Glide.with(this).load(image).transform(GlideCircleTransform(this)).placeholder(R.mipmap.icon_head_portrait).into(userIcon)
        }
        val phone = preferences?.getStringValue(Constant.LOGIN_PHONE)
        if (NotNull.isNotNull(phone)){
            phoneBindText.text=getString(R.string.hadBind)
        }else{
            phoneBindText.text=getString(R.string.hadNotBind)
        }

    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        ll_ChangeProfile.setOnClickListener(this)
        ll_UserName.setOnClickListener(this)
        ll_PhoneBind.setOnClickListener(this)
        ll_WeChatBind.setOnClickListener(this)
        ll_ChangePWD.setOnClickListener(this)
        tv_SignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_ChangeProfile -> {
                bottomDialog?.show()
            }
            R.id.ll_UserName -> {
                //用户名称
                showActivity(SetUserNameActivity::class.java)
            }
            R.id.ll_PhoneBind -> {
                //绑定手机
                showActivity(BindPhoneNumActivity::class.java)
            }
            R.id.ll_WeChatBind -> {
                //绑定微信
                MyToast.showToastCustomerStyleText(this, getString(R.string.deviceDeveloping))
            }
            R.id.ll_ChangePWD -> {
                //设置密码
                showActivity(SetPwdActivity::class.java)
            }
            R.id.tv_SignUp -> {
                //退出登陆
                myAlertDialog?.show()
            }
        }
    }

    private fun initBottomDialog() {
        bottomDialog = PhotoAlertDialog(this)
        bottomDialog?.setOnDialogListener(object : PhotoAlertDialog.OnDialogListener {
            override fun onTakePhotoListener() {
                //拍照
                val imageFile = File(Environment
                        .getExternalStorageDirectory(), "tempImage.jpg")
                try {
                    if (imageFile.exists()) {
                        imageFile.delete()
                    }
                    imageFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                imageUri = Uri.fromFile(imageFile)
                val intent = Intent("android.media.action.IMAGE_CAPTURE")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, TAKE_PHOTO)
            }

            override fun onFromPicListener() {
                //照片
                val imageFile = File(Environment
                        .getExternalStorageDirectory(), "outputImage.jpg")
                if (imageFile.exists()) {
                    imageFile.delete()
                }
                try {
                    imageFile.createNewFile()
                } catch (e: IOException) {
                }

                imageUri = Uri.fromFile(imageFile)
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.putExtra("scale", true)
                intent.putExtra("crop", true)
                intent.type = "image/*"
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, IMAGE_CHIOCE)
            }

            override fun onPhotoCancelListener() {
                bottomDialog?.hide()
            }

        })
    }

    private fun initDialog() {
        myAlertDialog = MyAlertDialog(this).setTitle(getString(R.string.exitLogin)).setContent("")
                .setConfirm(getString(R.string.signOut))
        myAlertDialog?.setOnDialogListener(object : MyAlertDialog.OnDialogListener {
            override fun onConfirmListener() {
                preferences?.clearPreferences()
                val goockrApplication = application as GoockrApplication
                goockrApplication.exit()//清空preference
                goockrApplication.mDaoSession?.clear()
                showActivity(LoginActivity::class.java)
            }

            override fun onCancelListener() {
                myAlertDialog?.hide()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            TAKE_PHOTO -> {
                if (NotNull.isNotNull(imageUri)) {
                    photoCrop(imageUri!!)
                }
            }
            IMAGE_CHIOCE -> {
                if (NotNull.isNotNull(data)) {
                    handleImageOnKitkat(data!!)
                }
            }
            IMAGE_CROP -> {
                //TODO 处理上传
                if (NotNull.isNotNull(imageUri)) {
                    val file = File(imageUri?.path)
                    showProgressDialog(getString(R.string.upLoading))
                    val token = preferences?.getStringValue(TOKEN)
                    val url = Constant.BASE_URL + NetApi.UP_LOADHEAD_IMG
                    val filename = file.name
                    OkHttpUtils.post().addFile("file", filename, file)?.
                            url(url)?.
                            addParams("token", token)?.
                            build()?.execute(object : MyStringCallback(this) {
                        override fun onResponse(response: String?, id: Int) {
                            dismissDialog()
                            LogUtils.i("", "$response")
                            val bitmap = BitmapFactory.decodeStream(contentResolver
                                    .openInputStream(imageUri))
                            LogUtils.i("", "" + bitmap)
                            runOnUiThread {
                                if (NotNull.isNotNull(bitmap)) {
                                    val bao = ByteArrayOutputStream()
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao)
                                    val bytes=bao.toByteArray()
                                    Glide.with(this@UserSettingActivity).load(bytes).transform(GlideCircleTransform(this@UserSettingActivity)).placeholder(R.mipmap.icon_head_portrait).into(userIcon)
//                                    userIcon.setImageBitmap(bitmap)
                                    MyToast.showToastCustomerStyleText(this@UserSettingActivity, getString(R.string.uploadSucceed))
                                }
                                bottomDialog?.hide()
                            }
                        }

                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            dismissDialog()
                            LogUtils.i("", "$e")
                            bottomDialog?.hide()
                            MyToast.showToastCustomerStyleText(this@UserSettingActivity, getString(R.string.uploadError))
                        }
                    })


                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun handleImageOnKitkat(data: Intent) {
        var fileImagePath: String? = null
        var uri = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cr = this.contentResolver
        val cursor = cr.query(uri, filePathColumn, null, null, null)
        //首先获取路径
        if (null != cursor) {
            cursor.moveToFirst()
            if (cursor.columnCount > 0) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                if (cursor.count > 0) {
                    fileImagePath = cursor.getString(columnIndex)
                    LogUtils.i("", "--->baseActivity" + fileImagePath)
                }
            }
            cursor.close()
        }

        if (fileImagePath == null || "" == fileImagePath) {
            fileImagePath = uri.path
        }

        //路径没有获取到
        if (fileImagePath == null || "" == fileImagePath) {
            //再去获取图片
            val extras = data.extras
            val isGetBitmap = if (null != extras) {
                val bitmap = extras.getParcelable<Parcelable>("data")
                if (null != bitmap) {
                    LogUtils.i("", "" + bitmap)
                    true
                } else {
                    false
                }
            } else {
                false
            }
            //图片也没有获取到，直接调用系统裁剪
            if (!isGetBitmap) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(cr, uri)
                    LogUtils.i("", "" + bitmap)
                } catch (e: FileNotFoundException) {
                } catch (e: IOException) {
                }

            }
        } else {
            if (fileImagePath.indexOf("/document/image") > -1) {
                MyToast.showToastCustomerStyleText(this, "图片路径不对请重新选择")
                return
            } else {
                fileImagePath = getCorrectFilePath(fileImagePath)
                uri = Uri.fromFile(File(fileImagePath))
                imageUri = uri
                photoCrop(uri)
            }

        }
    }


    private fun getCorrectFilePath(photoPath: String): String {
        var filePath = photoPath
        if (!filePath.isEmpty()) {
            if (filePath.indexOf("/storage/emulated/0") > -1) {
                filePath = filePath.replace("/storage/emulated/0", "/sdcard")
            }
        }
        return filePath

    }

    private fun photoCrop(uri: Uri) {
        // 设置intent为启动裁剪程序
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("scale", true)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, IMAGE_CROP)
    }
}
