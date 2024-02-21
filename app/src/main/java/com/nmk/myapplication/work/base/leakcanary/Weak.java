package com.nmk.myapplication.work.base.leakcanary;

import android.content.DialogInterface;

/**
 * VLive_Overseas
 *
 * @author Created by H-ray on 2021/4/13.
 */

public class Weak {
    public static WeakOnCancelListener proxy(DialogInterface.OnCancelListener real) {
        return new WeakOnCancelListener(real);
    }

    public static WeakOnDismissListener proxy(DialogInterface.OnDismissListener real) {
        return new WeakOnDismissListener(real);
    }

    public static WeakOnShowListener proxy(DialogInterface.OnShowListener real) {
        return new WeakOnShowListener(real);
    }
}
