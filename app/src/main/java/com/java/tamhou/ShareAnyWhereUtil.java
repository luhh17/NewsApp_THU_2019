package com.java.tamhou;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yaphet
 * on 2018/3/21.
 */

public class ShareAnyWhereUtil {

    /**
     * bitmapToUri
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Uri bitmapToUri(Activity context, Bitmap bitmap) {
        if (!checkPermission(context)) {
            return null;
        }
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
    }

    /**
     * drawableToUri
     *
     * @param context
     * @param drawable
     * @return
     */
    public static Uri drawableToUri(Activity context, Drawable drawable) {
        if (!checkPermission(context)) {
            return null;
        }
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), ((BitmapDrawable) drawable).getBitmap(), null, null));
    }

    /**
     * viewToUri
     *
     * @param context
     * @param view
     * @return
     */
    public static Uri viewToUri(Activity context, View view) {
        if (!checkPermission(context)) {
            return null;
        }
        view.buildDrawingCache();
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), view.getDrawingCache(), null, null));
    }

    /**
     * createUriList
     *
     * @param uris
     * @return
     */
    public static ArrayList<Uri> createUriList(Uri... uris) {
        ArrayList<Uri> result = new ArrayList<>();
        Collections.addAll(result, uris);
        return result;
    }

    /**
     * 分享到Wechat
     *
     * @param context
     * @param uris
     * @param desc
     */
    public static void shareWechat(Activity context, ArrayList<Uri> uris, String desc) {
        if (!checkPermission(context)) {
            return;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra("android.intent.extra.TEXT", desc);
        intent.putExtra("Kdescription", desc);
        context.startActivity(intent);
    }

    public static void shareWechatText(Context context, String content) {
        Intent intent = new Intent();
        ComponentName cop = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(cop);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("android.intent.extra.TEXT", content);
//            intent.putExtra("sms_body", content);
        intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 分享到新浪微博
     *
     * @param context
     * @param uris
     * @param desc
     */
    public static void shareWeibo(Activity context, ArrayList<Uri> uris, String desc) {
        if (!checkPermission(context)) {
            return;
        }
        Intent intent = new Intent();
        intent.setPackage("com.sina.weibo");
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra("Kdescription", desc);
        context.startActivity(intent);
    }

    /**
     * 分享到QQ
     *
     * @param context
     * @param uris
     * @param desc
     */
    public static void shareQQ(Activity context, ArrayList<Uri> uris, String desc) {
        if (!checkPermission(context)) {
            return;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra("Kdescription", desc);
        context.startActivity(intent);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param context
     * @param uris
     * @param desc
     */
    public static void shareWechatMoments(Activity context, ArrayList<Uri> uris, String desc) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra("Kdescription", desc);
        context.startActivity(intent);
    }

    public static void shareMoments(Context context, Uri uri, String text) {
        Intent intent = new Intent();
        try {
            String authority = context.getPackageName() + ".FileProvider";
            ComponentName componentName = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(componentName);
//插入多张图片 ACTION_SEND_MULTIPLE

            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra("Kdescription", text);

            //7.0以上需要添加临时读取权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 权限检查
     *
     * @param context
     * @return
     */
    private static boolean checkPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] mPermissionList = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(context, mPermissionList, 1);
                return false;
            }
        }
        return true;
    }

}