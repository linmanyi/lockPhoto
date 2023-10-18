package com.nmk.myapplication.ui.view

import android.text.Editable
import android.text.TextWatcher

/**
作者：Hayden
时间：2021/7/27 10:50
描述：为了不用每次都重写3个方法，需要哪个重写哪个
 */
open class TextWatcherNew : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}