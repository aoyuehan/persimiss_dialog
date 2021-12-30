package com.aoyuehan.permission.callBack;

public interface CallBack {

    /*权限数字*/
    void onPeer();
    /*权限数字*/
    void onFinish();

    /*单一权限*/
    void onSingle(String id, boolean isAgree);
}
