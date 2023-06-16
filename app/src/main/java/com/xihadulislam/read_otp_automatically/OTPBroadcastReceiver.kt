package com.xihadulislam.read_otp_automatically

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Matcher
import java.util.regex.Pattern


class OTPBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {

            val smsStatus = intent.extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (smsStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = intent.extras?.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                    otpReceiveListener?.onOTPReceived(getOptFromMsg(message))
                }

                CommonStatusCodes.TIMEOUT -> {
                    otpReceiveListener?.onOTPTimeOut()
                }


            }

        }


    }

    private fun getOptFromMsg(message: String?): String? {
        message?.let {
            val pattern: Pattern = Pattern.compile("(\\d{4})")
            val matcher: Matcher = pattern.matcher(it)
            if (matcher.find()) {
                return matcher.group(0)
            }
        }
        return null
    }


    private var otpReceiveListener: OTPReceiveListener? = null

    fun init(otpReceiveListener: OTPReceiveListener?) {
        this.otpReceiveListener = otpReceiveListener
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut()
    }


}