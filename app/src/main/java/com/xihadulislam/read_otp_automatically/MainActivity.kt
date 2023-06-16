package com.xihadulislam.read_otp_automatically

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.xihadulislam.read_otp_automatically.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OTPBroadcastReceiver.OTPReceiveListener {

    companion object {
        private const val TAG = "MainActivity"

        // TODO: sms format
        /**

        Your ExampleApp code is: 123ABC78
        FA+9qCX9VSu

         */
        // you can get has key by calling  getHasKey() method
    }

    private lateinit var binding: ActivityMainBinding

    var otpBroadcastReceiver: OTPBroadcastReceiver = OTPBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getHasKey()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver()
    }


    private fun getHasKey() {
        val appSignatureHelper = AppSignatureHelper(this)
        val appSignatures = appSignatureHelper.appSignatures
        Log.d(TAG, "getHasKey: $appSignatures")
    }


    private fun registerReceiver() {
        startSMSRetrieverClient()
        registerReceiver(otpBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        otpBroadcastReceiver.init(this)

    }

    private fun unregisterReceiver() {
        unregisterReceiver(otpBroadcastReceiver)
    }


    private fun startSMSRetrieverClient() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener { aVoid: Void? -> }
        task.addOnFailureListener { e: Exception? -> }
    }

    override fun onOTPReceived(otp: String?) {
        otp?.let {
            binding.edOtp.setText(it)
        }
    }

    override fun onOTPTimeOut() {
        Log.d(TAG, "onOTPTimeOut: ")
    }


}