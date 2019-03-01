package com.theapache64.pigeon.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import javax.inject.Inject

class DeviceRepository @Inject constructor() {

    @SuppressLint("MissingPermission", "HardwareIds")
    fun getImei(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId
        }
    }

}