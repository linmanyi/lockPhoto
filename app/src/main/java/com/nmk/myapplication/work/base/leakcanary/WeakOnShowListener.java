package com.nmk.myapplication.work.base.leakcanary;

import android.content.DialogInterface;

import java.lang.ref.WeakReference;

/**
 * VLive_Overseas
 *
 * @author Created by H-ray on 2021/4/13.
 */

public class WeakOnShowListener implements DialogInterface.OnShowListener {
    private WeakReference<DialogInterface.OnShowListener> mRef;

    public WeakOnShowListener(DialogInterface.OnShowListener real) {
        this.mRef = new WeakReference<>(real);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        DialogInterface.OnShowListener real = mRef.get();
        if (real != null) {
            real.onShow(dialog);
        }
    }
}
