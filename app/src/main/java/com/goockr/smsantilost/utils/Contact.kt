package com.goockr.smsantilost.utils

import android.text.TextUtils
import java.util.*

class Contact @JvmOverloads constructor(name: String, phones: List<Phone>? = null) {
    var age: Int = 0

    var contactId: Int = 0

    var email: String? = null

    var name: String? = null

    private var phones: MutableList<Phone>? = null

    var photoId: String? = null

    var sex: Char = ' '

    class Phone(name: String?, var number: String?, var type: Int) {
        var name: String? = null

        @JvmOverloads constructor(number: String, type: Int = TYPE_OTHER) : this(null, number, type) {}

        init {
            var name = name
            if (TextUtils.isEmpty(number)) {
                throw NullPointerException("the number is empty.")
            }
            if (TextUtils.isEmpty(name)) {
                name = "Unknow"
            }
            this.name = name
        }


        override fun toString(): String {
            return "[name:" + name + "]number:" + this.number + "[type:" + this.type + "]"
        }

        override fun equals(o: Any?): Boolean {
            // TODO Auto-generated method stub
            if (this === o) {
                return true
            }

            if (o is Phone) {
                val p = o as Phone?
                if (this.type != p!!.type) {
                    return false
                }
                if (this.name != p.name) {
                    return false
                }
                return if (this.number != p.number) {
                    false
                } else true
            }

            return super.equals(o)
        }

        companion object {
            val TYPE_ASSISTANT = 19
            val TYPE_CALLBACK = 8
            val TYPE_CAR = 9
            val TYPE_COMPANY_MAIN = 10
            val TYPE_FAX_HOME = 5
            val TYPE_FAX_WORK = 4
            val TYPE_HOME = 1
            val TYPE_ISDN = 11
            val TYPE_MAIN = 12
            val TYPE_MMS = 20
            val TYPE_MOBILE = 2
            val TYPE_OTHER = 7
            val TYPE_OTHER_FAX = 13
            val TYPE_PAGER = 6
            val TYPE_RADIO = 14
            val TYPE_TELEX = 15
            val TYPE_TTY_TDD = 16
            val TYPE_WORK = 3
            val TYPE_WORK_MOBILE = 17
            val TYPE_WORK_PAGER = 18
        }

    }

    init {
        var name = name
        if (TextUtils.isEmpty(name)) {
            name = "Unknow"
        }
        this.name = name


        if (this.phones == null) {
            this.phones = ArrayList()
        } else {
            this.phones!!.clear()
        }
        this.phones!!.addAll(phones!!)

    }

    fun addPhone(phone: Phone?): Boolean {
        if (phone == null) {
            return false
        }

        try {
            this.phones!!.add(phone)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun getPhones(): List<Phone>? {
        return phones
    }

    fun removePhone(phone: Phone?): Boolean {
        if (phone == null) {
            return false
        }

        if (this.phones!!.contains(phone)) {
            try {
                this.phones!!.remove(phone)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

        }
        return true
    }

    fun removePhone(number: String): Boolean {
        if (TextUtils.isEmpty(number)) {
            return false
        }

        val iterator = this.phones!!.iterator()
        while (iterator.hasNext()) {
            if (number == iterator.next().number) {
                try {
                    this.phones!!.remove(iterator.next())
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }

            }
        }
        return true
    }

}
