package com.nmk.myapplication.work.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nmk.myapplication.R;
import com.nmk.myapplication.work.base.leakcanary.DialogFragment;
import com.nmk.myapplication.work.manager.ActivityManager;
import com.nmk.myapplication.work.utils.view.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Created by H-ray on 2020/4/4
 * @desc : 提示弹出框
 * <p>可动态设置标题，内容</p>
 * <p>可动态配置底部按钮，与点击事件</p>
 */
public class TipsDialog extends DialogFragment {

    protected Context mContext;
    private SparseArray<Integer> btnColorList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.LoadingDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.5f;
        lp.width = (int) (ViewUtil.getScreenWidth() * 0.75);
        lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.h_dialog_tips, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        boolean isCanceledOnTouchOutside = getArguments().getBoolean("isCanceledOnTouchOutside", true);
        getDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        getDialog().setCancelable(isCanceledOnTouchOutside);
        if (!isCanceledOnTouchOutside) {
            getDialog().setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            });
        }
        TextView titleTv = v.findViewById(R.id.titleTv);
        String title = getArguments().getString("title");
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        } else {
            titleTv.setVisibility(View.GONE);
        }

        TextView tipsTv = v.findViewById(R.id.tipsTv);
        String tips = getArguments().getString("tips");
        if (!TextUtils.isEmpty(tips)) {
            tipsTv.setText(tips);
        }

        int gravity = getArguments().getInt("gravity", 0);
        if (0 != gravity) {
            tipsTv.setGravity(gravity);
        } else {
            tipsTv.setGravity(Gravity.CENTER);
        }

        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int padding = ViewUtil.dip2px(16);
        ArrayList<String> btnStrList = getArguments().getStringArrayList("btnStrList");
        LinearLayout btnLinearLayout = v.findViewById(R.id.btnLinearLayout);
        btnLinearLayout.removeAllViews();
        for (int i = 0; i < btnStrList.size(); i++) {
            TextView tv = new TextView(getContext());
            tv.setText(btnStrList.get(i));
            tv.setPaddingRelative(0, padding, 0, padding);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setLayoutParams(mLayoutParams);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.getPaint().setFakeBoldText(true);
            mLayoutParams.weight = 1;
            tv.setTag(i);
            if (i == btnStrList.size() - 1) {
                //最后一个为红色
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_333333));
            } else {
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_666666));
            }

            if (btnColorList != null && btnColorList.get(i) != null) {
                tv.setTextColor(btnColorList.get(i));
            }
            //tv.setTextColor(ContextCompat.getColor(getContext(), R.color.baseDialogBtn));
            tv.setOnClickListener(view -> {
                if (mOnBtnClickListener != null) {
                    //dismiss();
                    //使用dismiss可能会报错Can not perform this action after onSaveInstanceState
                    dismissAllowingStateLoss();
                    try {
                        mOnBtnClickListener.onClick(Integer.parseInt(view.getTag().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //dismiss();
                    dismissAllowingStateLoss();
                }
            });
            btnLinearLayout.addView(tv);

            if (i < btnStrList.size() - 1) {
                btnLinearLayout.addView(createLine());
            }
        }
    }

    private View createLine() {
        View lineView = new View(mContext);
        lineView.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
        lineView.setBackgroundColor(getContext().getColor(R.color.lineColor_E7E7E7));
        return lineView;
    }

    private OnBtnClickListener mOnBtnClickListener;

    public TipsDialog setOnBtnClickListener(
            OnBtnClickListener onBtnClickListener) {
        mOnBtnClickListener = onBtnClickListener;
        return this;
    }

    public interface OnBtnClickListener {
        void onClick(int position);
    }

    public static class Builder {
        private String title;
        private String tips;
        private ArrayList<String> btnStrList;
        private SparseArray<Integer> btnColorList;
        private int mGravity;
        private boolean isCanceledOnTouchOutside = true;

        public Builder() {

        }

        public Builder setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTips(String tips) {
            this.tips = tips;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
            this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
            return this;
        }

        public Builder addBtn(String... btn) {
            if (btnStrList == null) {
                btnStrList = new ArrayList<>();
            }
            this.btnStrList.addAll(Arrays.asList(btn));
            return this;
        }

        public Builder addBtn(String btn) {
            if (btnStrList == null) {
                btnStrList = new ArrayList<>();
            }
            this.btnStrList.add(btn);
            return this;
        }

        public Builder addBtn(String btn, int color) {
            if (btnStrList == null) {
                btnStrList = new ArrayList<>();
            }
            if (btnColorList == null) {
                btnColorList = new SparseArray();
            }
            this.btnStrList.add(btn);
            btnColorList.put(btnStrList.size() - 1, color);
            return this;
        }

        public TipsDialog create(FragmentManager fm) {
            if (fm == null || fm.isDestroyed()) {
                //如果已经被销毁了，则使用栈顶的fm
                fm = ((AppCompatActivity) ActivityManager.getInstance().getTopActivity()).getSupportFragmentManager();
            }

            TipsDialog loadingDialog = new TipsDialog();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("tips", tips);
            bundle.putInt("gravity", mGravity);
            bundle.putBoolean("isCanceledOnTouchOutside", isCanceledOnTouchOutside);
            bundle.putStringArrayList("btnStrList", btnStrList);
            loadingDialog.btnColorList = btnColorList;
            loadingDialog.setArguments(bundle);
            fm.beginTransaction()
                    .add(loadingDialog, TipsDialog.class.getCanonicalName())
                    .commitAllowingStateLoss();
            return loadingDialog;
        }

        public TipsDialog create(AppCompatActivity a) {
            return create(a.getSupportFragmentManager());
        }

        public TipsDialog create(Fragment f) {
            return create(f.getChildFragmentManager());
        }

    }

}
