package com.stelle.stelleapp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.stelle.stelleapp.BaseApplication;

public class Utils {

    public static Typeface getThisTypeFace(Context context, int type) {
        Typeface typeface;
        switch (type) {

            case AppConstants.OPEN_SANS_REGULAR:
                typeface = FontCache.getFont(context, "fonts/OpenSans-Regular.ttf");
                break;
            case AppConstants.PT_SANS_WEB_REGULAR:
                typeface = FontCache.getFont(context, "fonts/PT_Sans-Web-Regular.ttf");
                break;
            default:
                typeface = FontCache.getFont(context, "fonts/OpenSans-Regular.ttf");
                break;
        }
        return typeface;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public static void showToast(Context context, String msgString) {
        Toast.makeText(context, msgString, Toast.LENGTH_SHORT).show();
    }

    public static String getMimeType(String url) {
        String type = "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (!TextUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        } else {
            String reCheckExtension = MimeTypeMap.getFileExtensionFromUrl(url.replaceAll("\\s+", ""));
            if (!TextUtils.isEmpty(reCheckExtension)) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(reCheckExtension);
            }
        }
        return type;
    }

    public static int convertDipToPixels(Context context, int dip) {
        Resources resources = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int convertDpToPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp,
                context.getResources().getDisplayMetrics());
    }

    public static boolean isApplicationInBackGround(Context context) {
        return ((BaseApplication) context).getResumeCount()
                == ((BaseApplication) context).getPauseCount();
    }



    public static String cloudinaryUrlFromContext(Context context) {
        String url = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                url = (String) info.metaData.get("CLOUDINARY_URL");
            }
        } catch (PackageManager.NameNotFoundException e) {
            // No metadata found
        }
        return url;
    }

    public static String getCircularCloudinaryImageUrl(String baseUrl, String baseUrlThumbnailPostFix, String imageId, int toolbarIconRadius) {
        CloudinaryImageUrl cloudinaryImageUrl =
                new CloudinaryImageUrl.Builder(baseUrl, imageId, toolbarIconRadius * 2,
                        toolbarIconRadius * 2, baseUrlThumbnailPostFix)
                        .cornerRadius(toolbarIconRadius)
                        .crop(CloudinaryImageUrl.CROP_THUMB)
                        .build();
        if (cloudinaryImageUrl != null) {
            return cloudinaryImageUrl.getTransformedImageUrl();
        }
        return "";

    }

}
