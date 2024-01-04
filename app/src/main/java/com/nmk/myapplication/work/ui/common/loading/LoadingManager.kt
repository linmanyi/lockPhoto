package com.nmk.myapplication.work.ui.common.loading

import android.content.Context

class LoadingManager {

    private var loadDialog: LoadDialog? = null

    private constructor()

    companion object {
        private var instance: LoadingManager? = null
        fun getInstance(): LoadingManager {
            if (instance == null) {
                instance = LoadingManager()
            }
            return instance!!
        }
    }

    fun showDialog(context: Context) {
        if (instance?.loadDialog == null)
            instance?.loadDialog = LoadDialog(context)
        instance?.loadDialog?.showDialog()
    }

    fun hideDialog() {
        if (instance?.loadDialog != null) {
            instance?.loadDialog?.dismiss()
        }
    }
}