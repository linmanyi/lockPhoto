package com.nmk.myapplication.work.manager;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.nmk.myapplication.work.ui.dialog.LoadingDialog;
import com.nmk.myapplication.work.utils.glide.ImageUtil;

/**
 * @author Created by H-ray on 2020/4/4
 * @desc : 等待操作弹出框管理
 * <p> 单例模式 </p>
 * <p> 实现全局隐藏显示LoadingDialog，不依赖调用者环境 </p>
 */
public class LoadingDialogManager {

    private LoadingDialog mLoadingDialog;

    private LoadingDialogManager() {

    }

    private static class InstanceHolder {
        private static final LoadingDialogManager INSTANCE = new LoadingDialogManager();
    }

    public static LoadingDialogManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void showLoadingDialog() {
        showLoadingDialog("", true, null);
    }

    public void showLoadingDialog(DialogInterface.OnDismissListener onDismissListener) {
        showLoadingDialog("", true, onDismissListener);
    }

    public void showLoadingDialog(String tips, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener) {
        showLoadingDialog(ActivityManager.getInstance().getTopActivity(), tips, isCancelable, onDismissListener);
    }

    /**
     * 显示LoadingDialog
     */
    public void showLoadingDialog(Activity activity, String tips, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener) {
        if (activity == null || activity.isFinishing()) return;
        FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();
        if (mLoadingDialog == null && !fm.isDestroyed()) {
            mLoadingDialog = (LoadingDialog) fm.findFragmentByTag(LoadingDialog.class.getCanonicalName());
            if (mLoadingDialog != null && mLoadingDialog.isAdded()) {
                FragmentManager currentFragmentManager = mLoadingDialog.getFragmentManager();
                if (currentFragmentManager != null && currentFragmentManager != fm) {
                    // 从原来的 FragmentManager 中分离
                    currentFragmentManager.beginTransaction().remove(mLoadingDialog).commitNowAllowingStateLoss();
                }else{
                    fm.beginTransaction().remove(mLoadingDialog).commitAllowingStateLoss();
                }
            }

            mLoadingDialog = LoadingDialog.getInstance(fm, tips, isCancelable, onDismissListener);
            mLoadingDialog.setTips(tips);
        } else if (mLoadingDialog.isHidden() && !fm.isDestroyed()) {
            //存在，可能处于隐藏状态
            mLoadingDialog = (LoadingDialog) fm.findFragmentByTag(LoadingDialog.class.getCanonicalName());
            mLoadingDialog.setTips(tips);
            mLoadingDialog.show(fm, LoadingDialog.class.getCanonicalName());
        } else if (mLoadingDialog.getShowsDialog() && !fm.isDestroyed()) {
            FragmentManager currentFragmentManager = mLoadingDialog.getFragmentManager();
            if (currentFragmentManager != null && currentFragmentManager != fm) {
                // 从原来的 FragmentManager 中分离
                currentFragmentManager.beginTransaction().remove(mLoadingDialog).commitNowAllowingStateLoss();
            }

            if (mLoadingDialog != null && !mLoadingDialog.isAdded()) {
                fm.beginTransaction().remove(mLoadingDialog).commitAllowingStateLoss();
                mLoadingDialog = LoadingDialog.getInstance(fm, tips, isCancelable, onDismissListener);
                mLoadingDialog.setTips(tips);
            }
        }
    }

    public void setTip(String tips) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setTips(tips);
        }
    }

    public void hideLoadingDialog() {
        hideLoadingDialog(ActivityManager.getInstance().getTopActivity());
    }

    /**
     * 隐藏LoadingDialog
     */
    public void hideLoadingDialog(Activity activity) {

        if (ImageUtil.isDestroy(activity) || (!(activity instanceof FragmentActivity))) {
            return;
        }

        if (mLoadingDialog != null) {
            mLoadingDialog.dismissAllowingStateLoss();
            mLoadingDialog = null;
        }
    }

    public void releaseDialog(LoadingDialog loadingDialog) {
        if (loadingDialog == mLoadingDialog) {
            if (mLoadingDialog != null && mLoadingDialog.isAdded()) {
                mLoadingDialog.dismissAllowingStateLoss();
            }
            mLoadingDialog = null;
        }
    }

}
