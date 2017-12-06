package com.booksalways.shopping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by welcome on 14-10-2016.
 */
public class IncomigSmsForForgotPassword extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    String substr=message.substring(31,35).trim();


                    Log.d("currentMessage",senderNum);
                    Log.d("message",message);
                    Log.i("substr", substr);
                    try
                    {
                        if (senderNum .equals("MM-ISHETH"))
                        {
                            OtpForForgotPassword SmsA = new OtpForForgotPassword ();
                            SmsA.recivedSms(substr);
                            //  Sms.progressDialog.dismiss();

                            Log.d("otpmessage",substr);

                        }
                    }
                    catch(Exception e){}

                }
            }

        } catch (Exception e)
        {

        }
    }

}