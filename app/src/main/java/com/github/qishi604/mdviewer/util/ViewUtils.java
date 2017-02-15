package com.github.qishi604.mdviewer.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.ResultReceiver;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * UI工具类
 *
 * @author Lin
 * @version V1.0
 * @since 16/5/26
 */
public final class ViewUtils {

    public static final DisplayMetrics DISPLAY_METRICS = Resources.getSystem().getDisplayMetrics();

    private ViewUtils() {
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public static int getWidth() {
        return DISPLAY_METRICS.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public static int getHeight() {
        return DISPLAY_METRICS.heightPixels;
    }

    /**
     * dp2px(四舍五入)
     *
     * @param dp
     * @return
     */
    public static int dp2Px(float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, DISPLAY_METRICS) + 0.5f);
    }

    public static int sp2Px(float sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, DISPLAY_METRICS) + 0.5f);
    }

    /**
     * 获取控件宽
     */
    public static int getWidth(View view) {
        measureView(view);
        return view.getMeasuredWidth();
    }

    /**
     * 获取控件高
     */
    public static int getHeight(View view) {
        measureView(view);
        return view.getMeasuredHeight();
    }

    /**
     * 测量View
     */
    private static void measureView(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    /*
     * 设图片背景
     */
    public static void setBackground(View view, Drawable d) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(d);
        } else {
            view.setBackgroundDrawable(d);
        }
    }

    /**
     * 设置选中某一些 下拉框
     *
     * @param spinner
     * @param selection
     * @return void
     * kibey 2013-10-12 下午3:47:51
     */
    public static void setSelection(Spinner spinner, Object selection) {
        setSelection(spinner, selection.toString());
    }

    /**
     * 设置选中某一些 下拉框
     *
     * @param spinner
     * @param selection
     * @return void
     * kibey 2013-10-12 下午3:47:34
     */
    public static void setSelection(Spinner spinner, String selection) {
        final int count = spinner.getCount();
        for (int i = 0; i < count; i++) {
            String item = spinner.getItemAtPosition(i).toString();
            if (item.equalsIgnoreCase(selection)) {
                spinner.setSelection(i);
            }
        }
    }

    /**
     * 显示软键盘，通过获取activity当前获取焦点的View来显示软键盘
     * Activity没有获取焦点的View，则不能显示出软键盘
     *
     * @param activity Activity
     */
    public static void showSoftKeyboard(Activity activity) {
        if (null != activity && null != activity.getCurrentFocus()) {
            showSoftKeyboard(activity.getCurrentFocus());
        }
    }

    /**
     * 显示软键盘
     *
     * @param view
     * @return void
     * kibey 2013-10-12 下午3:46:44
     */
    public static void showSoftKeyboard(View view) {
        showSoftKeyboard(view, null);
    }

    /**
     * 显示软键盘
     *
     * @param view
     * @param resultReceiver
     * @return void
     * kibey 2013-10-12 下午3:47:19
     */
    public static void showSoftKeyboard(View view, ResultReceiver resultReceiver) {
        Configuration config = view.getContext().getResources().getConfiguration();
        if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (resultReceiver != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT, resultReceiver);
            } else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    /**
     * 隐藏软键盘，通过获取activity当前获取焦点的View来隐藏软键盘
     * Activity没有获取焦点的View，则不能隐藏软键盘
     *
     * @param activity Activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (null != activity && null != activity.getCurrentFocus()) {
            hideSoftKeyboard(activity.getCurrentFocus());
        }
    }

    /**
     * 关闭软键盘
     *
     * @param view View
     */
    public static void hideSoftKeyboard(View view) {
        if (null == view) {
            return;
        }
        Context context = view.getContext();
        if (null == context
                || null == view.getWindowToken()) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 截屏
     *
     * @param activity
     * @return Bitmap
     * kibey 2013-10-26 下午2:39:01
     */
    public static Bitmap shot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        return getBitmapFromView(view);
    }

    /**
     * 获得Intent中跳转前Activity的Icon,若无，则返回App图标
     *
     * @param context
     * @param i
     * @return
     */
    public static Drawable getIconForIntent(final Context context, Intent i) {
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> infos = pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
        if (infos.size() > 0) {
            return infos.get(0).loadIcon(pm);
        }
        return null;
    }

    /**
     * 代码实现旋转的菊花效果
     *
     * @param imageView 需要旋转的图片
     * @param drawable  旋转菊花
     * @return void
     * kibey 2014-2-21 下午5:09:58
     */
    public static void startAnim(ImageView imageView, int drawable) {
        try {
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(drawable);
            AnimationSet animationSet = new AnimationSet(false);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(2000);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatMode(Animation.RESTART);
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            animationSet.addAnimation(rotateAnimation);
            imageView.setAnimation(animationSet);
        } catch (Exception e) {

        }
    }

    /**
     * 停止自定义菊花的旋转
     *
     * @param imageView
     * @return void
     * kibey 2014-2-21 下午5:10:40
     */
    public static void stopAnim(ImageView imageView) {
        try {
            imageView.clearAnimation();
            imageView.setImageBitmap(null);
        } catch (Exception e) {
        }
    }

    /**
     * 加载html／普通文字，链接可点击
     * <p>
     * Populate the given {@link TextView} with the requested text, formatting through {@link Html#fromHtml(String)}
     * when applicable. Also sets {@link TextView#setMovementMethod} so inline links are handled.
     */
    public static void setTextMaybeHtml(TextView view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setText("");
            return;
        }
        if (text.contains("<")
                && text.contains(">")) {
            view.setText(Html.fromHtml(text));
            view.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            view.setText(text);
        }
    }

    /**
     * 移除所有监听
     *
     * @param view
     * @param listener
     */
    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (null != view
                && null != listener) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            } else {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
            }
        }
    }

    public static void hideNavigationBar(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        try {
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNavigationBar(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_VISIBLE;
        try {
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Rect在屏幕上去掉状态栏高度的绝对位置
     */
    public static Rect getViewAbsRect(View view, int parentX, int parentY) {
        int[] loc = new int[2];
        view.getLocationInWindow(loc);
        Rect rect = new Rect();
        rect.set(loc[0], loc[1], loc[0] + view.getMeasuredWidth(), loc[1] + view.getMeasuredHeight());
        rect.offset(-parentX, -parentY);
        return rect;
    }

    /**
     * 向上翻转
     */
    static PropertyValuesHolder mPullUpHolder = PropertyValuesHolder.ofFloat("rotation", 0f, 180f);
    /**
     * 向下翻转
     */
    static PropertyValuesHolder mPullDownHolder = PropertyValuesHolder.ofFloat("rotation", 180f, 360f);

    /**
     * 展开动画
     *
     * @param view     需要动画的对象
     * @param isExpand true向上旋转,false向下旋转
     */
    public static void expandAnimator(final View view, boolean isExpand) {
        ObjectAnimator objectAnimator;
        if (isExpand) {
            objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, mPullUpHolder);
        } else {
            objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, mPullDownHolder);
        }
        // 防止重复点击
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setClickable(true);
            }
        });
        objectAnimator.start();
    }

    /**
     * 获取view的图片
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = view.getDrawingCache(); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap = Bitmap.createBitmap(bitmap);
        }
        view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }
}
