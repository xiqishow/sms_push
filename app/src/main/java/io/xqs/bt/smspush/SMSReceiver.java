package io.xqs.bt.smspush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.xqs.bt.smspush.model.ConfigMdl;
import io.xqs.bt.smspush.util.MailUtil;
import io.xqs.bt.smspush.util.SystemUtil;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConfigMdl configMdl = MailUtil.getMailConfig(context);
        if (configMdl == null) {
            return;
        }
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if (smsMessages != null && smsMessages.length > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String title = "[" + simpleDateFormat.format(new Date()) + "]收到新的短信";
            StringBuilder bodyBuilder = new StringBuilder();
            for (SmsMessage message : smsMessages) {
                if (bodyBuilder.length() == 0) {
                    bodyBuilder.append("<p>");
                    bodyBuilder.append("<b>来自:</b>");
                    bodyBuilder.append(message.getOriginatingAddress());
                    bodyBuilder.append("</p>");
                    bodyBuilder.append("<p>");
                    bodyBuilder.append("<b>时间:</b>");
                    bodyBuilder.append(simpleDateFormat.format(new Date(message.getTimestampMillis())));
                    bodyBuilder.append("</p>");
                    bodyBuilder.append("<p>");
                    bodyBuilder.append("<b>内容:</b>");
                }
                bodyBuilder.append(message.getMessageBody());
            }
            bodyBuilder.append("</p>");
            if (!TextUtils.isEmpty(configMdl.getRemark())) {
                bodyBuilder.append("<p>");
                bodyBuilder.append(configMdl.getRemark());
                bodyBuilder.append("</p>");
            }
            bodyBuilder.append("<p>")
                    .append("电池电量为:")
                    .append(SystemUtil.getBatteryLevel(context))
                    .append("%")
                    .append("</p>");
            String body = bodyBuilder.toString();
            MailUtil.sendMail(title, body, configMdl, null);
        }
    }

}