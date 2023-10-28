package com.hray.library.helper.picture_selector

import android.content.Context
import android.net.Uri
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.utils.DateUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File

/**
 * ImageSelector引擎
 * @author Created by H-ray on 2023/1/19.
 */

/** 自定义压缩 **/
class ImageCompressEngine : CompressFileEngine {

    override fun onStartCompress(
        context: Context?,
        source: ArrayList<Uri>?,
        call: OnKeyValueResultCallbackListener?
    ) {
        if(context == null || source?.isNotEmpty() != true){
            call?.onCallback("", "")
            return
        }
        Luban.with(context)
            .load(source)
            .ignoreBy(150)
            .setRenameListener { filePath ->
                val indexOf = filePath.lastIndexOf(".")
                val postfix = if (indexOf != -1) filePath.substring(indexOf) else ".jpg"
                DateUtils.getCreateFileName("CMP_") + postfix
            }.setCompressListener(object : OnNewCompressListener {
                override fun onStart() {
                }

                override fun onSuccess(source: String, compressFile: File) {
                    call?.onCallback(source, compressFile.absolutePath)
                }

                override fun onError(source: String, e: Throwable?) {
                    call?.onCallback(source, "")
                }
            }).launch()
    }

}