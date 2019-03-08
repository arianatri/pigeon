package com.theapache64.pigeon.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import javax.inject.Inject

class DeviceRepository @Inject constructor(

) {

    /**
     * Gets device IMEI. Can only be called if app has permission to READ_PHONE_STATE
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getImei(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId
        }
    }

    /**
     * Gets device owner name. If can't returns 'Unknown'
     */
    fun getOwnerName(context: Context): String {

        var ownerName: String? = null

        context.contentResolver
            .query(ContactsContract.Profile.CONTENT_URI, null, null, null, null)
            ?.apply {
                if (count > 0 && moveToFirst()) {
                    getString(getColumnIndex(ContactsContract.Profile.DISPLAY_NAME)).let { name ->
                        ownerName = name
                    }
                }

                close()
            }

        return ownerName ?: "Unknown"
    }

}