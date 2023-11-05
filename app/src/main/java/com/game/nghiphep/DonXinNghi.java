package com.game.nghiphep;

import java.util.Date;

public class DonXinNghi {

    private String documentID;

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    private String msnv1;
    private String lido;
    private int trangthai;

    public String getMsnv1() {
        return msnv1;
    }

    public void setMsnv1(String msnv1) {
        this.msnv1 = msnv1;
    }


    public String getLido() {
        return lido;
    }

    public void setLido(String lido) {
        this.lido = lido;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    private Date ngay;
}
