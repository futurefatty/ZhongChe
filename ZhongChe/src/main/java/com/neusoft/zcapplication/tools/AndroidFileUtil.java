package com.neusoft.zcapplication.tools;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

public class AndroidFileUtil {
    public static Intent openFile(String filePath, Uri fileUri) {

        System.out.println("打开的文件路径 : " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        /* 取得扩展名 */
        String fileName = file.getName();
        String end = fileName.substring(file.getName().lastIndexOf(".") + 1,
                file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        Intent intent = null;
        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            intent = getImageFileIntent(filePath, fileUri);
        } else if (end.equals("ppt") || end.equals("pptx")) {
            intent = getPptFileIntent(filePath, fileUri);
        } else if (end.equals("xls") || end.equals("xlsx")||end.equals("xml")) {
//          intent = getExcelFileIntent(filePath);
            intent = getExcelFileIntent(filePath, fileUri);
        } else if (end.equals("doc")) {
            intent = getWordFileIntent(filePath, fileUri);
        } else if (end.equals("docx")) {
            intent = getWordFileIntent(filePath, fileUri);
        } else if (end.equals("pdf")) {
            intent = getPdfFileIntent(filePath, fileUri);
        } else if (end.equals("chm")) {
            intent = getChmFileIntent(filePath, fileUri);
        } else if (end.equals("txt")) {
            intent = getTextFileIntent(filePath, false, fileUri);
        }if (end.equals("mp4")) {
            intent = getMp4FileIntent(filePath,fileUri);
        }
        return intent;
    }



    // 播放音乐
    public static Intent openMusic(String filePath, Uri fileUri) {

        File file = new File(filePath);
        System.out.println("打开的文件路径 : " + filePath);
        if (!file.exists()) {
            return null;
        }
        /* 取得扩展名 */
        String fileName = file.getName();
        String end = fileName.substring(file.getName().lastIndexOf(".") + 1,
                file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        Intent intent = null;
        intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(new File(filePath));
        Uri uri = uriString(filePath, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }


    private static Intent getMp4FileIntent(String param,Uri filUri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "video/mp4");
        return intent;
    }

    public static Intent getImageFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(new File(param));
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      Uri uri = Uri.fromFile(new File(param));
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
//      Uri uri = Uri.fromFile(new File(param));
//      intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      Uri uri = Uri.fromFile(new File(param));
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(new File(param));
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
//            Uri uri = uriString(param, filUri);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param, Uri filUri) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      Uri uri = Uri.fromFile(new File(param));
        Uri uri = uriString(param, filUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static Uri uriString(String param, Uri filUri) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //data是file类型,忘了复制过来
            uri = filUri;
        } else {
            uri = Uri.fromFile(new File(param));
        }
        return uri;
    }

}
