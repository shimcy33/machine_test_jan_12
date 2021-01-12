package com.app.machinetest.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.app.machinetest.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstantMethods {
    public static void showToast(Context context, String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static Dialog dialog, dialogLayer, dialogZoom;

    /**
     * To get screen width
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    /**
     * To get Current version name
     *
     * @param context
     * @return
     */


    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * To get density of device
     *
     * @return density
     */
    public static int getDisplayDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) Math.round(dm.density);
    }

    /***
     * Variable for avoid emoji from edit text
     */
    public static InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(src.charAt(index));
                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;

        }

    };

    /**
     * isPhoneNumberValid: Validate phone number using Java reg ex.
     * This method checks if the input string is a valid phone number.
     *
     * @param phoneNumber String. Phone number to validate
     * @return boolean: true if phone number is valid, false otherwise.
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
     /* Phone Number formats: (nnn)nnn-nnnn; nnnnnnnnnn; nnn-nnn-nnnn
        ^\\(? : May start with an option "(" .
	    (\\d{3}): Followed by 3 digits.
	    \\)? : May have an optional ")"
	    [- ]? : May have an optional "-" after the first 3 digits or after optional ) character.
	    (\\d{3}) : Followed by 3 digits.
	     [- ]? : May have another optional "-" after numeric digits.
	    (\\d{4})$ : ends with four digits.
         Examples: Matches following phone numbers:(123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
        //Initialize reg ex for phone number.
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Checks whether if there is active internet connection, Network/Wifi
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    public static void noInternetCall(Context context, String mActivity) {

        try {
            Log.e("try", "try  no internet");
            Class<? extends Activity> cls = Class.forName(mActivity)
                    .asSubclass(Activity.class);
            Log.e("Edunet Classes : ", "" + cls);
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);


        } catch (ClassNotFoundException ignored) {
            Log.e("intent exception " + mActivity, "----" + ignored.getMessage());
        }

    }


    public static void loadShimmerImage(Context context,
                                        String path, final ImageView ivPreview,
                                        final ShimmerFrameLayout shimmerFrameLayout) {
        Log.e("path", "" + path);
        Log.e("imgview", "" + ivPreview);


        if (context == null || path == null) {
            return;
        }

        if (path == null) {
            path = "";
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(ContextCompat.getDrawable(context, R.drawable.ic_default_preview_grey));
        requestOptions.centerInside();

        if (shimmerFrameLayout != null) {
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
        }
        ivPreview.setVisibility(View.INVISIBLE);


        if (path.equals("")) {

            Glide.with(context)
                    .load(context.getResources().getIdentifier("ic_default_preview_grey", "drawable", context.getPackageName()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (shimmerFrameLayout != null) {
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }
                            ivPreview.setVisibility(View.VISIBLE);

                            return false;
                        }


                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (shimmerFrameLayout != null) {
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }
                            ivPreview.setVisibility(View.VISIBLE);
                            ivPreview.setAlpha(0.0f);
                            ivPreview.animate()
                                    .translationY(0)
                                    .alpha(1.0f)
                                    .setListener(null)
                                    .setDuration(1000);

                            return false;
                        }
                    })
                    .into(ivPreview);
            return;
        }
        Glide
                .with(context)
                .load(path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (shimmerFrameLayout != null) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }
                        ivPreview.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (shimmerFrameLayout != null) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }
                        ivPreview.setVisibility(View.VISIBLE);
                        ivPreview.setAlpha(0.0f);
                        ivPreview.animate()
                                .translationY(0)
                                .alpha(1.0f)
                                .setListener(null)
                                .setDuration(1000);

                        return false;
                    }
                })
                .apply(requestOptions)

                .into(ivPreview);

    }


    public static int getBannerHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float ratioPX = (float) width * ((float) 3 / (float) 5);

        return (int) ratioPX;
    }


    /**
     * Method to calculate no of coulmns for grid layout manager
     *
     * @param context
     * @return
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    /**
     * Method to calculate no of coulmns for grid layout manager
     *
     * @param context
     * @return
     */
    public static int calculateNoOfColumnsForCategories(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 100);
        return noOfColumns;
    }

    /**
     * Common method for transitions between activity
     *
     * @param context
     */

    public static void overrideTransition(Context context) {
        ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * To get screen width
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    /**
     * Method to calculate no of coulmns for grid layout manager
     *
     * @param context
     * @return
     */
    public static int calculateNoOfColumnsForFilter(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) ((dpWidth / 180) / 1.5);
        return noOfColumns;
    }

    /**
     * Method to calculate no of coulmns for grid layout manager
     *
     * @param context
     * @return
     */
    public static int calculateNoOfColumnsForBookNow(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 300);
        return noOfColumns;
    }

    public static int calculateNoOfColumnsForWriters(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 120);
        return noOfColumns;
    }

    public static GradientDrawable getGradientBg(Context context, String startColor, String endColor) {
//Color.parseColor() method allow us to convert
        // a hexadecimal color string to an integer value (int color)


        int[] colors = {Color.parseColor(startColor), Color.parseColor(endColor)};

        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        gd.setCornerRadius(0f);//apply the button background to newly created drawable gradient
        return gd;

    }


    public static Dialog showApiCallProgress(Context context) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_progress_alert);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    public static Dialog showErrorInfo(Context context, String msg) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        if (dialogLayer != null) {
            if (dialogLayer.isShowing()) {
                dialogLayer.dismiss();
            }
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_error_alert);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout((int) (ConstantMethods.getScreenWidth(context) * .9), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvErrorMessage = dialog.findViewById(R.id.tvErrorMessage);
        TextView tvClose = dialog.findViewById(R.id.tvClose);
        tvErrorMessage.setText(msg + "");
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;

    }

}

