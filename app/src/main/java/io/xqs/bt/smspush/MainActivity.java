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

        //??????????????????
        Intent startIntent = new Intent(this, PowerService.class);
        startService(startIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "???????????????????????????????????????,????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * ??????????????????
     */
    @OnClick(R.id.btn_send_mail)
    void onClickTestMain() {
        UIUtil.hideSystemKeyBoard(this);
        ConfigMdl configMdl = MailUtil.getMailConfig(this);
        if (configMdl != null) {
            KProgressHUD hud = KProgressHUD.create(MainActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("?????????...")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            MailUtil.sendMail("????????????", "?????????????????????????????????????????????????????????????????????", configMdl, new MailUtil.MailUtilListener() {
                @Override
                public void onComplete(boolean success, String message) {
                    hud.dismiss();
                    if (!TextUtils.isEmpty(message)) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * ????????????
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
        if (!checkRequired(configMdl.getSmtp(), "???????????????")) {
            return;
        }
        if (!checkRequired(configMdl.getSmtpUser(), "????????????")) {
            return;
        }
        if (!checkRequired(configMdl.getSmtpPass(), "????????????")) {
            return;
        }
        if (!checkRequired(configMdl.getSendMail(), "????????????")) {
            return;
        }
        if (!checkRequired(configMdl.getRecvMail(), "????????????")) {
            return;
        }
        MailUtil.saveConfig(this, configMdl);
    }

    @OnClick(R.id.btn_check_permission)
    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        } else {
            Toast.makeText(this, "????????????,??????????????????MIUI???????????????????????????????????????,??????????????????106??????????????????", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkRequired(String value, String label) {
        if (TextUtils.isEmpty(value)) {
            Toast.makeText(this, "?????????" + label, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}