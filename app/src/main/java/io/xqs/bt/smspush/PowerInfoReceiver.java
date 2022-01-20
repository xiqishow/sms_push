package io.xqs.bt.smspush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import io.xqs.bt.smspush.model.ConfigMdl;
import io.xqs.bt.smspush.util.MailUtil;
import io.xqs.bt.smspush.util.SystemUtil;

public class PowerInfoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConfigMdl configMdl = MailUtil.getMailConfig(context);
        if (configMdl == null) {
            return;
        }


        String action = intent.getAction();
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<p>")
                .append("电池电量为:")
                .append(SystemUtil.getBatteryLevel(context))
                .append("%")
                .append("</p>");
        if (action == Intent.ACTION_POWER_CONNECTED) {
            bodyBuilder.append("<p>")
                    .append("已经连接充电器")
                    .append("</p>");
        } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
            bodyBuilder.append("<p>")
                    .append("已经断开充电器")
                    .append("</p>");
        } else if (action == Intent.ACTION_BATTERY_LOW) {
            bodyBuilder.append("<p>")
                    .append("电池电量低,请及时连接充电器")
                    .append("</p>");
        } else if (action == Intent.ACTION_BATTERY_LOW) {
            bodyBuilder.append("<p>")
                    .append("电池电量低,请及时连接充电器")
                    .append("</p>");
        } else if (action == Intent.ACTION_BATTERY_OKAY) {
            bodyBuilder.append("<p>")
                    .append("电池电量已经恢复")
                    .append("</p>");
        }
        if(!TextUtils.isEmpty(configMdl.getRemark())){
            bodyBuilder.append("<p>")
                    .append(configMdl.getRemark())
                    .append("</p>");
        }
        MailUtil.sendMail("电量消息", bodyBuilder.toString(), configMdl, null);
    }
}