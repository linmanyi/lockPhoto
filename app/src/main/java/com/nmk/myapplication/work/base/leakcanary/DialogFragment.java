package com.nmk.myapplication.work.base.leakcanary;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * VLive_Overseas
 *
 * @author Created by H-ray on 2021/4/13.
 */

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new WeakDialog(requireContext(), getTheme());
    }
}