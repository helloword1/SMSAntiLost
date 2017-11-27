package com.goockr.smsantilost.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri


/**
 * Created by LJN on 2017/11/23.
 */
class ContactUtils(private var context: Context) {
    fun updataContact(rawContactId: Long, name: String, number: String) {
        val uri = Uri.parse("content://com.android.contacts/data")//对data表的所有数据操作
        val values = ContentValues()
        //更新电话号码
        if (number.contains(",")) {
            val split = number.split(",")
            for (s in split) {
                values.put("data1", s)
            }
        } else {
            values.put("data1", number)
        }
        context.contentResolver.update(uri, values, "mimetype_id=? and raw_contact_id=?", arrayOf("5", rawContactId.toString()))
        //更新联系人姓名
        values.clear()
        values.put("data1", name)
        context.contentResolver.update(uri, values, "mimetype_id=? and raw_contact_id=?", arrayOf("7", rawContactId.toString()))
    }

    //增加联系人
    fun addContact(name: String, number: String) {
        /* 往 raw_contacts 中添加数据，并获取添加的id号*/
        var uri = Uri.parse("content://com.android.contacts/raw_contacts")
        val values = ContentValues()
        val rawContactId = ContentUris.parseId(context.contentResolver.insert(uri, values))
        //插入data表
        uri = Uri.parse("content://com.android.contacts/data")
        // 向data表插入数据
        if (name !== "") {
            values.put("raw_contact_id", rawContactId)
            values.put("mimetype", "vnd.android.cursor.item/name")
            values.put("data2", name)
            context.contentResolver.insert(uri, values)
        }
        // 向data表插入电话号码
        if (number !== "") {
            values.clear()
            values.put("raw_contact_id", rawContactId)
            values.put("mimetype", "vnd.android.cursor.item/phone_v2")
            values.put("data2", "2")
            if (number.contains(",")) {
                val split = number.split(",")
                for (s in split) {
                    values.put("data1", s)
                }
            } else {
                values.put("data1", number)
            }
            context.contentResolver.insert(uri, values)
        }
    }
}