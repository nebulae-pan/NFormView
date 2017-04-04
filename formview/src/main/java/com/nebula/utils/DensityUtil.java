package com.nebula.utils;

import android.content.Context;

/**
 * Created by pan on 2017/4/4.
 */

public class DensityUtil {
    public static float dip2Px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
