package com.nmk.myapplication.work.utils.file;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.nmk.myapplication.app.MyApplication;
import com.nmk.myapplication.work.utils.glide.ImageUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * <p>
 * Created by H-ray on 2018/1/18
 */
public class FileUtil {

    /**
     * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
     */
    public static String getCacheDir(Context context) {
        String cacheDir;
        if (context.getExternalCacheDir() != null && sdCardIsAvailable()) {
            cacheDir = context.getExternalCacheDir().toString();
        } else {
            cacheDir = context.getCacheDir().toString();
        }
        return cacheDir;
    }

    /**
     * 检测SD卡是否可用
     *
     * @return true为可用，否则为不可用
     */
    public static boolean sdCardIsAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * <p>
     * 为兼容Q，使用getExternalFilesDir
     */
    private static String sdCardPath = "";
    public static String getSdCardPath() {
        if(!TextUtils.isEmpty(sdCardPath)){
            return sdCardPath;
        }

        File file;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            File[] files = ContextCompat.getExternalFilesDirs(MyApplication.mContext, null);
            if (files != null && files.length > 0) {
                file = files[0];
            } else {
                return "";
            }
        } else {
            try {
                file = MyApplication.mContext.getFilesDir();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        if (file == null) {
            return "";
        }
        sdCardPath = file.toString();
        return sdCardPath;
    }

    private static String downloadPath = "";

    public static String getDownloadPath() {
        if(!TextUtils.isEmpty(downloadPath)){
            return downloadPath;
        }
        downloadPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        return downloadPath;
    }

    /**
     * 根据文件路径创建文件夹和文件
     * <p>
     * eg: /userImg/userImg.jpg
     * 先依据/拆分为字符串数组，然后逐级创建文件夹和文件
     */
    public static File createMoreFiles(String path) {
        String sdPath = getSdCardPath();
        StringBuilder sb = new StringBuilder(sdPath);
        String[] filesArr = path.split("/");
        File files;

        //逐级创建文件夹
        for (String filesName : filesArr) {
            if (filesArr.length < 1 || filesName.contains(".")) {
                continue;
            }

            sb.append(File.separator).append(filesName);
            files = new File(sb.toString());
            if (!files.exists()) {
                files.mkdirs();
            }
        }

        //创建文件
        return createFile(path);
    }

    /**
     * 创建文件
     */
    public static File createFile(String fileName) {
        if (fileName.indexOf(".") == -1) {
            return null;
        }
        String path;
        if (fileName.startsWith("/")) {
            path = getSdCardPath() + fileName;
        } else {
            path = getSdCardPath() + File.separator + fileName;
        }

        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 删除某个文件夹下全部文件
     *
     * @param filePath 文件夹路径
     */
    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                // 删除文件夹
                file.delete();
            }
        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 将Bitmap写入SD卡中
     *
     * @param filesName 文件名称
     */
    public static void saveFile(File file, String filesName) {
        try {
            InputStream inputStream = new FileInputStream(file.getPath());
            inputstreamToFile(inputStream, filesName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Bitmap写入SD卡中
     *
     * @param filesName 文件名称
     */
    public static void saveLoadFile(File file, String filesName) {
        try {
            InputStream inputStream = new FileInputStream(file.getPath());
            inputstreamToDownload(inputStream, filesName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static InputStream getInputStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 对文件进行压缩
     *
     * @param filePath 原文件路径
     */
    public static InputStream getInputStream(String filePath) {
        InputStream inputStream = null;
        try {
            File file = new File(filePath);
            inputStream = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static File inputstreamToDownload(InputStream ins, String fileName) {
        OutputStream os;
        File file;
        try {
            String path = getDownloadPath() + "/" + fileName;
            os = new FileOutputStream(path);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();

            file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * InputStream to file
     */
    public static File inputstreamToFile(InputStream ins, String fileName) {
        createMoreFiles(fileName);
        OutputStream os;
        try {
            os = new FileOutputStream(getSdCardPath() + fileName);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return createFile(fileName);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件夹大小
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 转换文件大小
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format(((double) fileS / 1024)) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(((double) fileS / 1048576)) + "MB";
        } else {
            fileSizeString = df.format(((double) fileS / 1073741824)) + "GB";
        }
        return fileSizeString;
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isExitFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 压缩完成的Zip路径
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
        //完成和关闭
        outZip.finish();
        outZip.close();
    }

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString + fileString + "/", fileList[i], zipOutputSteam);
            }
        }
    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static boolean upZipFile(File zipFile, String folderPath) {
        boolean ret = true;
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            if (!desDir.mkdirs()) {
                return false;
            }
        } else if (!desDir.isDirectory()) {
            deleteFile(desDir);
            if (!desDir.mkdirs()) {
                return false;
            }
        }
        InputStream in = null;
        OutputStream out = null;
        ZipFile zf = null;
        try {
            zf = new ZipFile(zipFile);
        } catch (IOException e) {
            ret = false;
        }
        if (zf != null) {
            byte buffer[] = new byte[1024 * 1024];
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                try {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String str = folderPath + File.separator + entry.getName();
                    str = new String(str.getBytes("8859_1"), "GB2312");
                    File desFile = new File(str);
                    if (entry.isDirectory()) {
                        continue;
                    }
                    if (desFile.exists()) {
                        desFile.delete();
                    }
                    File fileParentDir = desFile.getParentFile();
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs();
                    }
                    desFile.createNewFile();
                    out = new FileOutputStream(desFile);
                    int realLength;
                    in = zf.getInputStream(entry);
                    while ((realLength = in.read(buffer)) > 0) {
                        out.write(buffer, 0, realLength);
                    }
                } catch (IOException e) {
                    ret = false;
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        ret = false;
                    }
                }
            }
            try {
                zf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

}
