package com.pan.domain;

public class FtpConfig {
    public static final String MODE_PASSIVE = "P";
    public static final String MODE_ACTIVE = "A";
    public static final String IGNORE_LIST_CHECK = "Y";
    private String ftpId;
    private String ip;
    private int port;
    private String mde;
    private String bk;
    private String usrNme;
    private String psw;
    private String dstDir;
    private String dstFle;
    private String locDir;
    private String locFle;
    private String rmk;

    public FtpConfig() {
    }

    public boolean isPassiveMode() {
        return "P".equals(this.mde);
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMde() {
        return this.mde;
    }

    public void setMde(String mde) {
        this.mde = mde;
    }

    public String getBk() {
        return this.bk;
    }

    public void setBk(String bk) {
        this.bk = bk;
    }

    public String getFtpId() {
        return this.ftpId;
    }

    public void setFtpId(String ftpId) {
        this.ftpId = ftpId;
    }

    public String getUsrNme() {
        return this.usrNme;
    }

    public void setUsrNme(String usrNme) {
        this.usrNme = usrNme;
    }

    public String getPsw() {
        return this.psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getRmk() {
        return this.rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getDstDir() {
        return this.dstDir;
    }

    public void setDstDir(String dstDir) {
        this.dstDir = dstDir;
    }

    public String getDstFle() {
        return this.dstFle;
    }

    public void setDstFle(String dstFle) {
        this.dstFle = dstFle;
    }

    public String getLocDir() {
        return this.locDir;
    }

    public void setLocDir(String locDir) {
        this.locDir = locDir;
    }

    public String getLocFle() {
        return this.locFle;
    }

    public void setLocFle(String locFle) {
        this.locFle = locFle;
    }
}