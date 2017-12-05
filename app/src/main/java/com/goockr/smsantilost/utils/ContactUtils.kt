package com.goockr.smsantilost.utils

import android.content.ContentProviderOperation
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.RawContacts.Data
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
        if (contactId == null) {
            val cr = context.contentResolver
            val rawContactUri = cr.insert(ContactsContract.RawContacts.CONTENT_URI, values)
            rawContactId = ContentUris.parseId(rawContactUri)
        } else {
            rawContactId = java.lang.Long.parseLong(contactId)
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

    fun updateContact(context: Context, contactId: String, name: String, numbers:ArrayList<String>, types: ArrayList<Int>) {
        deleteContact(context, contactId)
        addContact(context, name, numbers, types)
    }


    fun deleteContact(context: Context, contactId: String) {
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
            phones = ArrayList<Contact.Phone>()
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

}
