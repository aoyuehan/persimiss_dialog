package com.aoyuehan.permission.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.aoyuehan.permission.bean.PermissionBean;
import com.aoyuehan.permission.callBack.AgreeInterface;
import com.aoyuehan.permission.callBack.CallBack;
import com.aoyuehan.permission.callBack.IPermissionCallback;
import com.aoyuehan.permission.dialog.NotificPopDialog;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;


public class PermissionDialogHelper {
    private Context context;
    private static PermissionDialogHelper mInstance;
    private PermissionHelper permissionHelper;
    private PermissionBean mPer;
    private ArrayList<PermissionBean> permissionList;
    private String name;

    public PermissionDialogHelper setPermission(PermissionBean permission) {
        this.mPer = permission;
        return this;
    }

    public PermissionDialogHelper setPermissionList(ArrayList<PermissionBean> permissionList) {
        this.permissionList = permissionList;
        return this;
    }

    public PermissionDialogHelper setName(String name) {
        this.name = name;
        return this;
    }

    public static PermissionDialogHelper getInstance(Context context) {
        mInstance = new PermissionDialogHelper();
        mInstance.context = context;
        mInstance.permissionHelper = PermissionHelper.getInstance((FragmentActivity) context);
        return mInstance;
    }

    /*app所需要的必要权限数组*/
    public void isShowNeedArray(CallBack callBack, int resultCode) {
        if (permissionList != null && permissionList.size() > 0) {
            boolean isExist = false;
            for (PermissionBean per : permissionList) {
                if (!permissionHelper.hasPermission(context, per.getPermission())) {
                    isExist = true;
                    showNeedNotificationPup(per, callBack, resultCode);
                    break;
                }
            }
            if (!isExist) {
                //全部显示完毕
                callBack.onPeer();
            }
        } else {
            //全部显示完毕
            callBack.onPeer();
        }
    }

    private void showNeedNotificationPup(PermissionBean per, CallBack callBack, int resultCode) {
        new NotificPopDialog(context, name + per.getTitle(), per.getContent(), "不同意", "同意", new AgreeInterface() {
            @Override
            public void noAgree() {
                /*弹出警告弹窗*/
                new NotificPopDialog(context, name, "我们需要获取您的" + per.getTitle() + "才能继续提供服务，如您不允许将无法继续正常的使用我们的产品。", "继续退出", "我知道了", new AgreeInterface() {
                    @Override
                    public void noAgree() {
                        callBack.onFinish();
                    }

                    @Override
                    public void agree() {
                        isShowNeedArray(callBack, resultCode);
                    }
                }).show();
            }

            @Override
            public void agree() {
                permissionHelper.check(new IPermissionCallback() {
                    @Override
                    public void onGranted() {
                        isShowNeedArray(callBack, resultCode);
                    }

                    @Override
                    public void onDeny(boolean neverAsk) {
                        if (neverAsk) {
                            isShowNeedArray(callBack, resultCode);
                        } else {
                            /*应该弹出跳转设置按钮弹窗*/
                            showSetPermission(per, callBack, resultCode, new DialogCallBack() {
                                @Override
                                public void onDialog() {
                                    /*弹出警告弹窗*/
                                    new NotificPopDialog(context, name, "我们需要获取您的" + per.getTitle() + "才能继续提供服务，如您不允许将无法继续正常的使用我们的产品。", "继续退出", "我知道了", new AgreeInterface() {
                                        @Override
                                        public void noAgree() {
                                            callBack.onFinish();
                                        }

                                        @Override
                                        public void agree() {
                                            isShowNeedArray(callBack, resultCode);
                                        }
                                    }).show();
                                }
                            });

                        }
                    }
                }, per.getPermission());
            }
        }).show();
    }

    /*
     * 显示非必要的权限弹窗，使用时候再弹窗就可以
     * */
    public void isShowArray(CallBack callBack) {
        if (permissionList != null && permissionList.size() > 0) {
            boolean isExist = false;
            for (PermissionBean per : permissionList) {
                if (!permissionHelper.hasPermission(context, per.getPermission())) {
                    Boolean isLoad = SpUtils.getInstance(context).get(per.getId(), Boolean.class);
                    System.out.println("权限弹窗：" + per.getId() + ":" + isLoad);
                    if (isLoad == null || !isLoad) {
                        System.out.println("权限弹窗设置：" + per.getId());
                        SpUtils.getInstance(context).set(per.getId(), true);
                        showNotificationPup(per, callBack);
                        break;
                    }
                } else {
                    callBack.onSingle(per.getId(), true);
                }
            }
            if (!isExist) {
                //全部显示完毕
                callBack.onPeer();
            }
        }
    }

    private void showNotificationPup(PermissionBean per, CallBack callBack) {
        new NotificPopDialog(context, per.getTitle(), per.getContent(), "不同意", "同意", new AgreeInterface() {
            @Override
            public void noAgree() {
                callBack.onSingle(per.getId(), false);
                isShowArray(callBack);
            }

            @Override
            public void agree() {
                permissionHelper.check(new IPermissionCallback() {
                    @Override
                    public void onGranted() {
                        isShowArray(callBack);
                    }

                    @Override
                    public void onDeny(boolean neverAsk) {
                        isShowArray(callBack);
                    }
                }, per.getPermission());
            }
        }).show();
    }

    public void isShowSingle(CallBack callBack) {
        if (!permissionHelper.hasPermission(context, mPer.getPermission())) {
            new NotificPopDialog(context, mPer.getTitle(), mPer.getContent(), "不同意", "同意", new AgreeInterface() {
                @Override
                public void noAgree() {
                    callBack.onSingle(mPer.getId(), false);
                }

                @Override
                public void agree() {
                    permissionHelper.check(new IPermissionCallback() {
                        @Override
                        public void onGranted() {
                            callBack.onSingle(mPer.getId(), true);
                        }

                        @Override
                        public void onDeny(boolean neverAsk) {
                            if (neverAsk) {
                                callBack.onSingle(mPer.getId(), false);
                            } else {
                                /*应该弹出跳转设置按钮弹窗*/
                                new AlertDialog.Builder(context).setTitle("权限申请")
                                        .setMessage("在设置->应用->权限中，开启" + mPer.getTitle() + "，否则无法正常使用应用.")
                                        .setNegativeButton("取消", (dialogInterface, which) -> {
                                            callBack.onSingle(mPer.getId(), false);
                                        })
                                        .setPositiveButton("去设置", (dialogInterface, which) -> {
                                            Intent intent = new Intent();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            if (Build.VERSION.SDK_INT >= 9) {
                                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                                            } else if (Build.VERSION.SDK_INT <= 8) {
                                                intent.setAction(Intent.ACTION_VIEW);
                                                intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                                intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                                            }
                                            context.startActivity(intent);
                                        })
                                        .setCancelable(false).show();
                            }
                        }
                    }, mPer.getPermission());
                }
            }).show();
        } else {
            /*有权限*/
            callBack.onSingle(mPer.getId(), true);
        }
    }

    /*单个权限必须要给才能使用*/
    public void isShowNeedSingle(CallBack callBack, int resultCode) {
        if (!permissionHelper.hasPermission(context, mPer.getPermission())) {
            /*无权限*/
            new NotificPopDialog(context, mPer.getTitle(), mPer.getContent(), "不同意", "同意", new AgreeInterface() {
                @Override
                public void noAgree() {
                    /*弹出警告弹窗*/
                    new NotificPopDialog(context, name, "我们需要获取您的" + mPer.getTitle() + "才能继续提供服务，如您不允许将无法继续正常的使用我们的产品。", "继续退出", "我知道了", new AgreeInterface() {
                        @Override
                        public void noAgree() {
                            /*第二次拒绝直接返回*/
                            callBack.onSingle(mPer.getId(), false);
                        }

                        @Override
                        public void agree() {
                            System.out.println("isShowSingleNotification1");
                            isShowNeedSingle(callBack, resultCode);
                        }
                    }).show();
                }

                @Override
                public void agree() {
                    permissionHelper.check(new IPermissionCallback() {
                        @Override
                        public void onGranted() {
                            callBack.onSingle(mPer.getId(), true);
                        }

                        @Override
                        public void onDeny(boolean neverAsk) {
                            if (neverAsk) {
                                new NotificPopDialog(context, name, "我们需要获取您的" + mPer.getTitle() + "才能继续提供服务，如您不允许将无法继续使用正常使用我们的产品。", "继续退出", "我知道了", new AgreeInterface() {
                                    @Override
                                    public void noAgree() {
                                        callBack.onSingle(mPer.getId(), false);
                                    }

                                    @Override
                                    public void agree() {
                                        System.out.println("isShowSingleNotification2");
                                        isShowNeedSingle(callBack, resultCode);
                                    }
                                }).show();
                            } else {
                                /*拒绝不在提示*/
                                showSetPermission(mPer, callBack, resultCode, new DialogCallBack() {
                                    @Override
                                    public void onDialog() {
                                        /*弹出警告弹窗*/
                                        new NotificPopDialog(context, name, "我们需要获取您的" + mPer.getTitle() + "才能继续提供服务，如您不允许将无法继续正常的使用我们的产品。", "继续退出", "我知道了", new AgreeInterface() {
                                            @Override
                                            public void noAgree() {
                                                callBack.onSingle(mPer.getId(), false);
                                            }

                                            @Override
                                            public void agree() {
                                                System.out.println("isShowSingleNotification3");
                                                isShowNeedSingle(callBack, resultCode);
                                            }
                                        }).show();
                                    }
                                });
                            }
                        }
                    }, mPer.getPermission());
                }
            }).show();
        } else {
            /*有权限*/
            callBack.onSingle(mPer.getId(), true);
        }
    }

    /*跳转设置页面设置权限*/
    private void showSetPermission(PermissionBean per, CallBack callBack, int resultCode, DialogCallBack dialog) {
        /*应该弹出跳转设置按钮弹窗*/
        new AlertDialog.Builder(context).setTitle("权限申请")
                .setMessage("在设置->应用->权限中，开启" + per.getTitle() + "，否则无法正常使用应用.")
                .setNegativeButton("取消", (dialogInterface, which) -> {
                    dialog.onDialog();
                })
                .setPositiveButton("去设置", (dialogInterface, which) -> {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                    }
                    ((FragmentActivity) context).startActivityForResult(intent, resultCode);
                })
                .setCancelable(false).show();
    }

    public interface DialogCallBack {
        void onDialog();
    }
}

