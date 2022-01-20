package io.xqs.bt.smspush.model;

public class ConfigMdl {
    private String smtp;
    private boolean userSSL;
    private String smtpUser;
    private String smtpPass;
    private String sendMail;
    private String recvMail;
    private String remark;

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public boolean isUserSSL() {
        return userSSL;
    }

    public void setUserSSL(boolean userSSL) {
        this.userSSL = userSSL;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public String getSmtpPass() {
        return smtpPass;
    }

    public void setSmtpPass(String smtpPass) {
        this.smtpPass = smtpPass;
    }

    public String getSendMail() {
        return sendMail;
    }

    public void setSendMail(String sendMail) {
        this.sendMail = sendMail;
    }

    public String getRecvMail() {
        return recvMail;
    }

    public void setRecvMail(String recvMail) {
        this.recvMail = recvMail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
