package com.libit.wingspayroll.Network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class StaticDataHelper {


    public static final String USER_ID = "user_id";
    public static final String LANGUAGE = "locale_language";
    public static final String ARABIC = "arabic_language";
    public static final String ENGLISH = "english_language";

    public static final String FCM_TOKEN = "fcm_token";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String EMAIL_ID = "email_id";
    public static final String USER_NAME = "user_name";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String GENDER = "gender";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String CONTACT_NO = "contact_no";
    public static final String FULL_NAME = "full_name";
    public static final String LOCATION = "location";
    public static final String ADDRESS = "address";
    public static final String CREATE_VENDOR_OID = "create_vendor_oid";
    public static final String CREATE_VENUE_OID = "create_venue_oid";

    public static final String OID = "oid";
    public static final String CREATE_EVENT_OID = "create_event_oid";

    public static final String SHARE_EVENT_TITLE = "share_event_title";
    public static final String SHARE_VENDOR_TITLE = "share_vendor_title";
    public static final String SHARE_VENUE_TITLE = "share_venue_title";
    public static final String SHARE_EVENT_OPTION_OID = "share_event_option_oid";
    public static final String MY_SHARE_EVENT_OPTION_OID = "my_share_event_option_oid";


    public static final String EDIT_EVENT_IMAGE_LINK = "edit_event_image_link";
    public static final String EDIT_VENDOR_IMAGE_LINK = "edit_vendor_image_link";
    public static final String EDIT_VENUE_IMAGE_LINK = "edit_venue_image_link";
    public static final String EDIT_VENDOR_GALLERY_LINK = "edit_vendor_gallery_link";

    public static final String EDIT_VENUE_GALLERY_LINK = "edit_venue_gallery_link";

    public static final String EDIT_SITEMAP_IMAGE_LINK = "edit_sitmap_upload";


    public static final String CREATE_EVENT_IMAGE_LINK = "create_event_image_link";
    public static final String CREATE_VENDOR_IMAGE_LINK = "create_vendor_image_link";
    public static final String CREATE_VENUE_IMAGE_LINK = "create_venue_image_link";
    public static final String CREATE_VENDOR_GALLERY_LINK = "create_vendor_gallery_link";
    public static final String CREATE_VENUE_GALLERY_LINK = "create_venue_gallery_link";

    public static final String CREATE_SITEMAP_IMAGE_LINK = "create_sitmap_upload";


    public static final String EDIT_EVENT_OID = "edit_event_oid";
    public static final String EDIT_VENDOR_OID = "edit_vendor_oid";

    public static final String EDIT_VENUE_OID = "edit_venue_oid";


    public static final String EDIT_TICKET_OID_FIRST = "edit_ticket_first";
    public static final String EDIT_TICKET_OID_SECOND = "edit_ticket_second";
    public static final String EDIT_JOB_OID_FIRST = "edit_job_first";
    public static final String EDIT_JOB_OID_SECOND = "edit_job_second";
    public static final String EDIT_VENDOR_PACKAGE_OID_FIRST = "edit_vendor_package_first";

    public static final String EDIT_VENDOR_PACKAGE_OID_SECOND = "edit_vendor_package_second";
    public static final String EDIT_VENUE_AREA_FIRST = "edit_venue_area_first";
    public static final String EDIT_VENUE_AREA_SECOND = "edit_venue_area_second";
    public static final Object SHARED_PREFERENCE_NAME_COOKIE = "cookies_shared";
    public static final String PURCHASE_TOKEN = "purchase_token";
    public static final String COOKIES = "cookies";
    public static final String JSESSIONID = "jsessionid";
    public static final String COOKIE_EMPTY = "cookie_empty";
    public static final String COOKIES_CART = "cookie_cart";
    public static final String PURCHASE_TOKEN_CART = "purchase_token_cart";
    public static final String JSESSIONID_CART = "jsessionid_cart";
    public static final String CART_ITEM_COUNT = "cart_item_count";
    public static final String CART_UPDATE_STRING = "cart_update_string";
    public static final String CART_ADYEN_CHECKOUT = "cart_adyen_checkout";
    public static final String COOKIES_SETTING = "cookies_setting";
    public static final String COOKIES_CFDUID = "cookies_cfduid";


    private static SharedPreferences pref;
    public static String isUserLogin = "isUserLogin";
    public static String sellerid;
    public static String BID;
    public static String WALLET_AMOUNT = "wallet_amount";
    public static String APP_REVIEW = "app_review";
    public static String REFERRAL_POINTS = "referral_points";
    private Activity activity;
    private AlertDialog b;
    static AlertDialog alertDialog;

    public static StaticDataHelper getInstance(Context activity) {
        return new StaticDataHelper(activity);
    }

    public StaticDataHelper(Context activity) {
        this.activity = (AppCompatActivity) activity;
    }

    public static void setStringInPreferences(Context ctx, String key, String value) {
        pref = ctx.getSharedPreferences("orgeva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setIntegerInPreferences(Context ctx, String key, int value) {
        pref = ctx.getSharedPreferences("orgeva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

//        public static boolean isNetworkAvailable(Context context) {
//            ConnectivityManager connectivityManager
//                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        }

    public static void setBooleanInPreferences(Context ctx, String key, boolean value) {
        pref = ctx.getSharedPreferences("orgeva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);

        editor.apply();
    }

    public static String getStringFromPreferences(Context ctx, String key) {
        pref = ctx.getSharedPreferences("orgeva", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
    public static String getStringInPreferences(Context ctx, String key) {
        pref = ctx.getSharedPreferences("orgeva", Context.MODE_PRIVATE);
        String a=pref.getString(key,"");
        return a;
    }

    public static int getIntegerFromPreferences(Context ctx, String key) {
        pref = ctx.getSharedPreferences("orgeva", Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public static boolean getBooleanFromPreferences(Context ctx, String key) {
        pref = ctx.getSharedPreferences("orgeva", Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromBase64(String bmp) {
        byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap byteArrayToBitmap(byte[] ar) {
        return BitmapFactory.decodeByteArray(ar, 0, ar.length);
    }

    public static byte[] bitmapToByteArray(Bitmap ar) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ar.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static String getMimeType(String url) {
        String extension = url.substring(url.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
        return mimeType;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void showLoadingDialog() {
        if (!b.isShowing()) {
            b.show();
        }
    }

    public void hideLoadingDialog() {
        if (b.isShowing()) {
            b.dismiss();
        }
    }
}
