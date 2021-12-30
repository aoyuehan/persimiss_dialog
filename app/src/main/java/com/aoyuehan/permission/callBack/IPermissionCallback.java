package com.aoyuehan.permission.callBack;

public interface IPermissionCallback {

    /**
     * 权限通过了
     */
    public default void onGranted() {};

    /**
     * 拒绝
     */
    public default void onDeny(boolean neverAsk) {};

}
