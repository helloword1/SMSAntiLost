package com.goockr.smsantilost.views.activities.more

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.View
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.graphics.PhotoAlertDialog
import com.goockr.smsantilost.utils.Constant.IMAGE_CHIOCE
import com.goockr.smsantilost.utils.Constant.IMAGE_CROP
import com.goockr.smsantilost.utils.Constant.TAKE_PHOTO
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.login.LoginActivity
import cxx.utils.FileUtils.isHaveSdcard
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_user_setting.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


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
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
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
        val intent = Intent()
        when (v?.id) {
            R.id.ll_ChangeProfile -> {
                bottomDialog?.show()
            }
            R.id.ll_UserName -> {
                showActivity(SetUserNameActivity::class.java)
            }
            R.id.ll_PhoneBind -> {
                showActivity(BindPhoneNumActivity::class.java)
            }
            R.id.ll_WeChatBind -> {

            }
            R.id.ll_ChangePWD -> {
                showActivity(SetPwdActivity::class.java)
            }
            R.id.tv_SignUp -> {
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
                showActivity(LoginActivity::class.java)
            }

            override fun onCancelListener() {
                myAlertDialog?.hide()
            }

        })
    }

    private fun getCamaraPath(): String {
        if (!isHaveSdcard()) {
            return ""
        }
        var imageFilePath = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/"
        val imageName = SystemClock.currentThreadTimeMillis().toString() + "$.jpg"
        val out = File(imageFilePath)
        if (!out.exists()) {
            out.mkdirs()
        }
        imageFilePath += imageName
        return imageFilePath
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
                    val bitmap = BitmapFactory.decodeStream(contentResolver
                            .openInputStream(imageUri))
                    LogUtils.i("", "" + bitmap)
                    runOnUiThread {
                        userIcon.setImageBitmap(bitmap)
                        bottomDialog?.hide()
                    }
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
            var isGetBitmap = false
            isGetBitmap = if (null != extras) {
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
                //					size = new Size(170,170);
                uri = Uri.fromFile(File(fileImagePath))
                val bitmap = BitmapFactory.decodeStream(contentResolver
                        .openInputStream(uri))
                LogUtils.i("", "" + bitmap)
                runOnUiThread {
                    userIcon.setImageBitmap(bitmap)
                    bottomDialog?.hide()
                }
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
