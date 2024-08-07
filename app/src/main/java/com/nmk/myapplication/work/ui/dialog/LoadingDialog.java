package com.nmk.myapplication.work.ui.dialog;

import static com.nmk.myapplication.app.MyApplication.mContext;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.nmk.myapplication.R;
import com.nmk.myapplication.work.manager.LoadingDialogManager;
import com.nmk.myapplication.work.ui.anim.AnimationsContainer;

/**
 * @author Created by H-ray on 2020/4/4
 * @desc : 等待操作弹出框
 */
public class LoadingDialog extends DialogFragment {

    private TextView tipsTv;
    private ImageView progressImg;
    private Dialog mDialog;
    private DialogInterface.OnDismissListener onDismissListener;

    public static LoadingDialog getInstance(FragmentManager fm, String tips, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("tips", tips);
        bundle.putBoolean("isCancelable", isCancelable);
        loadingDialog.setArguments(bundle);
        loadingDialog.onDismissListener = onDismissListener;
        fm.beginTransaction()
                .add(loadingDialog, LoadingDialog.class.getCanonicalName())
                .commitAllowingStateLoss();
        return loadingDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.LoadingDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDialog = getDialog();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0;
        lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(getArguments().getBoolean("isCancelable"));
        if (onDismissListener != null) {
            getDialog().setOnDismissListener(onDismissListener);
        }
        View rootView = inflater.inflate(R.layout.dialog_loading, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        tipsTv = v.findViewById(R.id.tipsTv);
//        progressImg = v.findViewById(R.id.progressImg);
        String tips = getArguments().getString("tips");
        setTips(tips);
//        animStat();
    }

    /**
     * 设置提示信息
     */
    public void setTips(String tips) {
        if (tipsTv == null) {
            return;
        }
        if (TextUtils.isEmpty(tips)) {
            tipsTv.setVisibility(View.GONE);
        } else {
            tipsTv.setText(tips);
            tipsTv.setVisibility(View.VISIBLE);
        }
    }

    private AnimationsContainer.FramesSequenceAnimation animation;

//    public void animStat() {
//        if (animation == null) {
//            animation = AnimationsContainer.getInstance(R.array.refresh_anim, 25)
//                    .createProgressDialogAnim(mContext, progressImg);
//        }
//        animation.start();
//    }
//
//    public void animStop() {
//        //刷新结束,需要释放anim动画资源，否则内存容易泄漏
//        if (animation != null) {
//            animation.stop();
//            animation = null;
//        }
//    }

    @Override
    public void onDestroy() {
//        animStop();
        LoadingDialogManager.getInstance().releaseDialog(this);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        super.onDestroy();
    }

}
