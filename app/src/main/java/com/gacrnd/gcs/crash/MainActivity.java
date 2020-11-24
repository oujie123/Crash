package com.gacrnd.gcs.crash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {

    private static float sNonCompatDesity;  // 系统的Density
    private static float sNonCompatScaleDensity; //系统的ScaleDensity

    public static void setCustomDisplayMetrics(Activity activity, final Application application) {
        final DisplayMetrics metrics = application.getResources().getDisplayMetrics();
        if (sNonCompatDesity == 0) {
            sNonCompatDesity = metrics.density;
            sNonCompatScaleDensity = metrics.scaledDensity;
            // 监听系统设置中切换字体
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        // 360px是基准的设计稿,计算得到真实屏幕与图片缩放比例。
        final float targetDensity = metrics.widthPixels / 360F;
        final float targetScaleDesity = targetDensity * (sNonCompatScaleDensity / sNonCompatDesity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        metrics.density = targetDensity;
        metrics.scaledDensity = targetScaleDesity;
        metrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaleDesity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在setContentView前调用
        setCustomDisplayMetrics(this, this.getApplication());
        setContentView(R.layout.activity_main);
    }
}
