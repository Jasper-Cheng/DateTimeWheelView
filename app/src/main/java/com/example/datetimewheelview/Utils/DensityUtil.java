package com.example.datetimewheelview.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by 17258 on 2018/3/27.
 */

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     * @author SHANHY
     * @date 2015年10月28日
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     * @author SHANHY
     * @date 2015年10月28日
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度 px
     *
     * @param context
     * @return
     */
    public static int getScreenWidthPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeightPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕宽度 px
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display dm = wm.getDefaultDisplay();
        return dm.getWidth();
    }

    /**
     * 获取屏幕宽度 px
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display dm = wm.getDefaultDisplay();
        return dm.getHeight();
    }

    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     * 0为水平 270是正上方
     *
     * @param radius   半径
     * @param cirAngle 角度
     * @return x，y
     */
    public static float[] getCoordinatePoint(float centerX, float centerY, float radius, float cirAngle) {
        float[] point = new float[2];
        //将角度转换为弧度
        double arcAngle = Math.toRadians(cirAngle);
        if (cirAngle < 90) {
            point[0] = (float) (centerX + Math.cos(arcAngle) * radius);
            point[1] = (float) (centerY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 90) {
            point[0] = centerX;
            point[1] = centerY + radius;
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = Math.PI * (180 - cirAngle) / 180.0;
            point[0] = (float) (centerX - Math.cos(arcAngle) * radius);
            point[1] = (float) (centerY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 180) {
            point[0] = centerX - radius;
            point[1] = centerY;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = Math.PI * (cirAngle - 180) / 180.0;
            point[0] = (float) (centerX - Math.cos(arcAngle) * radius);
            point[1] = (float) (centerY - Math.sin(arcAngle) * radius);
        } else if (cirAngle == 270) {
            point[0] = centerX;
            point[1] = centerY - radius;
        } else {
            arcAngle = Math.PI * (360 - cirAngle) / 180.0;
            point[0] = (float) (centerX + Math.cos(arcAngle) * radius);
            point[1] = (float) (centerY - Math.sin(arcAngle) * radius);
        }

        return point;
    }


}
