package com.nmk.myapplication.work.base.leakcanary;

import android.content.DialogInterface;

import java.lang.ref.WeakReference;

/**
 * VLive_Overseas
 *
 * @author Created by H-ray on 2021/4/13.
 */

public class WeakOnDismissListener implements DialogInterface.OnDismissListener {
    private WeakReference<DialogInterface.OnDismissListener> mRef;

    public WeakOnDismissListener(DialogInterface.OnDismissListener real) {
        this.mRef = new WeakReference<>(real);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DialogInterface.OnDismissListener real = mRef.get();
        if (real != null) {
            real.onDismiss(dialog);
        }
    }
}
