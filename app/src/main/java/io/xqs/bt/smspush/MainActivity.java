package io.xqs.bt.smspush;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.xqs.bt.smspush.model.ConfigMdl;
import io.xqs.bt.smspush.util.MailUtil;
import io.xqs.bt.smspush.util.UIUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txt_smtp)
    EditText txtSmtp;

    @BindView(R.id.chk_use_ssl)
    CheckBox chkUseSSL;

    @BindView(R.id.txt_send_user)
    EditText txtSendUser;

    @BindView(R.id.txt_send_pass)
    EditText txtSendPass;

    @BindView(R.id.txt_send_email)
    EditText txtSendEMail;

    @BindView(R.id.txt_recv_email)
    EditText txtRecvEMail;

    @BindView(R.id.txt_remark)
    EditText txtRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }

        ConfigMdl configMdl = MailUtil.getMailConfig(this);
        if (configMdl != null) {
            txtSmtp.setText(configMdl.getSmtp());
            chkUseSSL.setChecked(configMdl.isUserSSL());
            txtSendUser.setText(configMdl.getSmtpUser());
            txtSendPass.setText(configMdl.getSmtpPass());
            txtSendEMail.setText(configMdl.getSendMail());
            txtRecvEMail.setText(configMdl.getRecvMail());
            txtRemark.setText(configMdl.getRemark());
        }

        //启动电力检测
        Intent startIntent = new Intent(this, PowerService.class);
        startService(startIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "请先授予应用读取短信的权限,部分系统还要同时授予读取营销短信", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 发送测试邮件
     */
    @OnClick(R.id.btn_send_mail)
    void onClickTestMain() {
        UIUtil.hideSystemKeyBoard(this);
        ConfigMdl configMdl = MailUtil.getMailConfig(this);
        if (configMdl != null) {
            KProgressHUD hud = KProgressHUD.create(MainActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("测试中...")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            MailUtil.sendMail("测试邮件", "这是一封测试邮件，用来测试发送邮件功能是否可用", configMdl, new MailUtil.MailUtilListener() {
                @Override
                public void onComplete(boolean success, String message) {
                    hud.dismiss();
                    if (!TextUtils.isEmpty(message)) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "请先设置配置后测试", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 点击保存
     */
    @OnClick(R.id.btn_save)
    void onClickSave() {
        UIUtil.hideSystemKeyBoard(this);
        ConfigMdl configMdl = new ConfigMdl();
        configMdl.setSmtp(txtSmtp.getText().toString());
        configMdl.setUserSSL(chkUseSSL.isChecked());
        configMdl.setSmtpUser(txtSendUser.getText().toString());
        configMdl.setSmtpPass(txtSendPass.getText().toString());
        configMdl.setSendMail(txtSendEMail.getText().toString());
        configMdl.setRecvMail(txtRecvEMail.getText().toString());
        configMdl.setRemark(txtRemark.getText().toString());
        if (!checkRequired(configMdl.getSmtp(), "发件服务器")) {
            return;
        }
        if (!checkRequired(configMdl.getSmtpUser(), "发件用户")) {
            return;
        }
        if (!checkRequired(configMdl.getSmtpPass(), "发件密码")) {
            return;
        }
        if (!checkRequired(configMdl.getSendMail(), "发件邮箱")) {
            return;
        }
        if (!checkRequired(configMdl.getRecvMail(), "收件邮箱")) {
            return;
        }
        MailUtil.saveConfig(this, configMdl);
    }

    @OnClick(R.id.btn_check_permission)
    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        } else {
            Toast.makeText(this, "已经授权,部分系统（如MIUI）需要单独开启营销短信权限,否则无法转发106等验证码短信", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkRequired(String value, String label) {
        if (TextUtils.isEmpty(value)) {
            Toast.makeText(this, "请输入" + label, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}