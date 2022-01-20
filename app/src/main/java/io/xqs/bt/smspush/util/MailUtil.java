package io.xqs.bt.smspush.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import io.xqs.bt.smspush.model.ConfigMdl;

public class MailUtil {

    public static void saveConfig(Context context,ConfigMdl configMdl){
        String configJson = new Gson().toJson(configMdl);
        SharedPreferences preferences = context.getSharedPreferences("SMS_MAIN_CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CONFIG",configJson);
        editor.apply();
    }

    public static ConfigMdl getMailConfig(Context context){
        SharedPreferences preferences = context.getSharedPreferences("SMS_MAIN_CONFIG", Context.MODE_PRIVATE);
        String configJson = preferences.getString("CONFIG","");
        if(TextUtils.isEmpty(configJson)){
            return null;
        }
        ConfigMdl configMdl = new Gson().fromJson(configJson,ConfigMdl.class);
        return configMdl;
    }

    public static void sendMail(String title, String content, ConfigMdl configMdl,MailUtilListener listener) {
        HandlerThread handlerThread = new HandlerThread("SocketOperation");
        handlerThread.start();
        Handler requestHandler = new Handler(handlerThread.getLooper());
        requestHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    new MailSender(title, content,configMdl).sendMail();
                    complete(listener,true,"发送成功");
                }
                catch (AuthenticationFailedException e){
                    complete(listener,false,"邮件服务器身份验证失败，请检查用户、密码");
                }
                catch (MessagingException e) {
                    complete(listener,false,e.getLocalizedMessage());
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    complete(listener,false,e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private static void complete(MailUtilListener listener,boolean success,String message){
        if(listener != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.onComplete(success,message);
                }
            });
        }
    }

    public interface MailUtilListener{
        void onComplete(boolean success,String essage);
    }
}
