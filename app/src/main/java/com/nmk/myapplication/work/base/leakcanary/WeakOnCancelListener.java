package com.nmk.myapplication.work.base.leakcanary;

import android.content.DialogInterface;

import java.lang.ref.WeakReference;

/**
 * VLive_Overseas
 *
 * @author Created by H-ray on 2021/4/13.
 */

public class WeakOnCancelListener implements DialogInterface.OnCancelListener {
    private WeakReference<DialogInterface.OnCancelListener> mRef;

    public WeakOnCancelListener(DialogInterface.OnCancelListener real) {
        this.mRef = new WeakReference<>(real);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        DialogInterface.OnCancelListener real = mRef.get();
        if (real != null) {
            real.onCancel(dialog);
        }
    }
}