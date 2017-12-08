package com.goockr.smsantilost.utils

import android.content.*
import android.net.Uri
import android.os.RemoteException
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.RawContacts
import android.provider.ContactsContract.RawContacts.Data
import com.goockr.smsantilost.entries.ContactsBean
import com.goockr.smsantilost.entries.PhoneBean
import java.util.*


object ContactUtils {
    /**
     *
     * @param context
     * @param numbers
     * @param types  eg:ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
     * @return
     */
    fun addContact(context: Context, name: String, numbers: ArrayList<String>, types: ArrayList<Int>) {
        addContact(context, null, name, numbers, types)
    }

    private fun addContact(context: Context, contactId: String?, name: String, numbers: ArrayList<String>, types: ArrayList<Int>) {
        val rawContactId: Long
        val values = ContentValues()
        rawContactId = if (contactId == null) {
            val cr = context.contentResolver
            val rawContactUri = cr.insert(ContactsContract.RawContacts.CONTENT_URI, values)
            ContentUris.parseId(rawContactUri)
        } else {
            java.lang.Long.parseLong(contactId)
        }
        //往data表插入姓名数据
        values.clear()
        values.put(Data.RAW_CONTACT_ID, rawContactId)
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)//内容类型
        values.put(StructuredName.GIVEN_NAME, name)
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        // 往data表插入电话数据
        for (i in numbers.indices) {
            values.clear()
            values.put(Data.RAW_CONTACT_ID, rawContactId)
            values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)// 内容类型
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, numbers[i])
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, types[i])
            context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }

    fun updateContact(context: Context, contactId: String, name: String, numbers: ArrayList<String>, types: ArrayList<Int>) {
        deleteContact(context, contactId)
        addContact(context, name, numbers, types)
    }


    private fun deleteContact(context: Context, contactId: String) {
        val ops = ArrayList<ContentProviderOperation>()
        //delete contact
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null)
                .build())
        //delete contact information such as phone number,email
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=" + contactId, null)
                .build())


        try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
        }

    }

    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    @Throws(RemoteException::class, OperationApplicationException::class)
    fun BatchAddContact(context: Context, list: List<PhoneBean>) {
        val ops = ArrayList<ContentProviderOperation>()
        var rawContactInsertIndex = 0
        for (contact in list) {
            rawContactInsertIndex = ops.size // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, null)
                    .withValue(RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build())

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.DISPLAY_NAME, contact.getMPhone())
                    .withYieldAllowed(true).build())
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.phone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build())
        }
        try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
        }
    }

    /**
     * 批量删除通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    @Throws(RemoteException::class, OperationApplicationException::class)
    fun BatchDeleteContact(context: Context, list: List<PhoneBean>) {
        val ops = ArrayList<ContentProviderOperation>()
        var rawContactInsertIndex = 0
        for (contact in list) {
            rawContactInsertIndex = ops.size // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, null)
                    .withValue(RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build())
            ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=" + contact.id, null)
                    .build())

            //delete contact information such as phone number,email
            ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=" + contact.id, null)
                    .build())

        }
        try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
        }
    }

    fun getContactId(context: Context): String? {
        val cursor = context.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToNext()) {
                val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                cursor.close()
                return contactId
            }
        }

        return null
    }

    fun getContactNameById(context: Context, contactID: String): String? {
        val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactID)
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            cursor.close()
            return name
        }

        return null
    }

    fun getPhonesByContacaId(context: Context, contactId: String): List<Contact.Phone>? {
        val cr = context.contentResolver
        val selection = ContactsContract.Contacts._ID + "=" + contactId
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, selection, null, null) ?: return null
        var phones: MutableList<Contact.Phone>? = null
        if (cursor.moveToFirst()) {
            val nameColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
            val contactName = cursor.getString(nameColumnIndex)
            //取得电话号码
            val ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null) ?: return null

            var p: Contact.Phone? = null
            val count = phoneCursor.count
            phones = ArrayList()
            val i = 0
            while (phoneCursor.moveToNext()) {
                var PhoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val type = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                //格式化手机号
                PhoneNumber = PhoneNumber.replace("-", "")
                PhoneNumber = PhoneNumber.replace(" ", "")

                p = Contact.Phone(contactName, PhoneNumber, Integer.valueOf(type)!!)
                phones.add(p)
            }
            phoneCursor.close()
        }
        cursor.close()
        return phones
    }

    //获取联系人
    private var PHONES_PROJECTION = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

    fun getSystemContactInfos(context: Context): List<ContactsBean> {
        val infos = ArrayList<ContactsBean>()
        val cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                var isAdded = true
                val contactName = cursor.getString(0)
                val phoneNumber = cursor.getString(1)
                val id = cursor.getString(2)
                for (mData in infos) {
                    if (mData.name == contactName) {
                        mData.phone += "," + phoneNumber
                        isAdded = false
                        LogUtils.i("12313123", mData.phone)
                    }
                }
                if (isAdded) {
                    val info = ContactsBean()
                    info.name = contactName
                    info.phone = phoneNumber
                    info.id = id
                    infos.add(info)
                }
            }
            cursor.close()
        }
        return infos
    }

}
