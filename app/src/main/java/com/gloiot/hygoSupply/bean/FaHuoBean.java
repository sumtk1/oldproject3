package com.gloiot.hygoSupply.bean;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

/**
 * 作者：Ljy on 2017/1/3.
 * 邮箱：enjoy_azad@sina.com
 */

public class FaHuoBean extends BaseObservable{

    //是否是物流发货
    private ObservableBoolean isWuliu;
    //是否线下交易
    private ObservableBoolean isOffline;
    //快递公司
    private String kuaidigongsi;
    //快递编号
    private String kuaidibianhao;
    //线下交易说明
    private String offlineExplain;
    //线下交易验证码
    private String offlineCode;

    public String getOfflineCode() {
        return offlineCode;
    }

    public void setOfflineCode(String offlineCode) {
        this.offlineCode = offlineCode;
    }

    public ObservableBoolean getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(ObservableBoolean isOffline) {
        this.isOffline = isOffline;
    }

    public String getOfflineExplain() {
        return offlineExplain;
    }

    public void setOfflineExplain(String offlineExplain) {
        this.offlineExplain = offlineExplain;
        notifyChange();
    }

    public ObservableBoolean getIsWuliu() {
        return isWuliu;
    }

    public void setIsWuliu(ObservableBoolean isWuliu) {
        this.isWuliu = isWuliu;
    }

    public String getKuaidigongsi() {
        return kuaidigongsi;
    }

    public void setKuaidigongsi(String kuaidigongsi) {
        this.kuaidigongsi = kuaidigongsi;
    }

    public String getKuaidibianhao() {
        return kuaidibianhao;
    }

    public void setKuaidibianhao(String kuaidibianhao) {
        this.kuaidibianhao = kuaidibianhao;
    }

    @Override
    public String toString() {
        return "FaHuoBean{" +
                "isWuliu=" + isWuliu +
                ", kuaidigongsi='" + kuaidigongsi + '\'' +
                ", kuaidibianhao='" + kuaidibianhao + '\'' +
                '}';
    }
}
