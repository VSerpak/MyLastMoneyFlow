package com.example.vserp.viewpager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import static android.R.id.message;

public class SMSBankReceiver extends BroadcastReceiver {
    public SMSBankReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] message = (Object[]) intent.getExtras().get("pdus");
            StringBuilder buffer = new StringBuilder();
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgSMS = null;
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgSMS = new SmsMessage[pdus.length];
                for (int i = 0; i < message.length; i++) {
                    msgSMS[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    buffer.append(msgSMS[i].getMessageBody());
                }
            }
            Toast.makeText(context, "SMS received = " + buffer.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
