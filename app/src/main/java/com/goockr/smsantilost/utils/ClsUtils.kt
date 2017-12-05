package com.goockr.smsantilost.utils

/************************************ ������Ժ��� *  */

import android.bluetooth.BluetoothDevice
import android.util.Log

object ClsUtils {
    /**
     * ���豸��� �ο�Դ�룺platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    @Throws(Exception::class)
    fun createBond(btClass: Class<*>, btDevice: BluetoothDevice): Boolean {
        val createBondMethod = btClass.getMethod("createBond")
        return createBondMethod.invoke(btDevice) as Boolean
    }

    /**
     * ���豸������ �ο�Դ�룺platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    @Throws(Exception::class)
    fun removeBond(btClass: Class<*>, btDevice: BluetoothDevice): Boolean {
        val removeBondMethod = btClass.getMethod("removeBond")
        return removeBondMethod.invoke(btDevice) as Boolean
    }

    @Throws(Exception::class)
    fun setPin(btClass: Class<out BluetoothDevice>, btDevice: BluetoothDevice,
               str: String): Boolean {
        try {
            val removeBondMethod = btClass.getDeclaredMethod("setPin",
                    *arrayOf<Class<*>>(ByteArray::class.java))
            val returnValue = removeBondMethod.invoke(btDevice,
                    *arrayOf<Any>(str.toByteArray())) as Boolean
            Log.e("returnValue", "" + returnValue)
        } catch (e: SecurityException) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return true

    }

    // ȡ���û�����
    @Throws(Exception::class)
    fun cancelPairingUserInput(btClass: Class<*>,
                               device: BluetoothDevice): Boolean {
        val createBondMethod = btClass.getMethod("cancelPairingUserInput")
        //        cancelBondProcess(btClass, device);
        return createBondMethod.invoke(device) as Boolean
    }

    // ȡ�����


    @Throws(Exception::class)
    fun cancelBondProcess(btClass: Class<*>,
                          device: BluetoothDevice): Boolean {
        val createBondMethod = btClass.getMethod("cancelBondProcess")
        return createBondMethod.invoke(device) as Boolean
    }

    //ȷ�����

    @Throws(Exception::class)
    fun setPairingConfirmation(btClass: Class<*>, device: BluetoothDevice, isConfirm: Boolean) {
        val setPairingConfirmation = btClass.getDeclaredMethod("setPairingConfirmation", Boolean::class.javaPrimitiveType)
        setPairingConfirmation.invoke(device, isConfirm)
    }


    /**
     *
     * @param clsShow
     */
    fun printAllInform(clsShow: Class<*>) {
        try {
            // ȡ�����з���
            val hideMethod = clsShow.methods
            var i = 0
            while (i < hideMethod.size) {
                Log.e("method name", hideMethod[i].name + ";and the i is:"
                        + i)
                i++
            }
            // ȡ�����г���
            val allFields = clsShow.fields
            i = 0
            while (i < allFields.size) {
                Log.e("Field name", allFields[i].name)
                i++
            }
        } catch (e: SecurityException) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }
}  
