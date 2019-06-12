package com.yumi.android.mobile.ads.utils.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;

import java.io.IOException;
import java.io.InputStream;

public class ResFactory {

    public static Drawable getDrawableByAssets(String name, Context ctx) {
        try {
            InputStream is = ctx.getAssets().open(name + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            byte[] nine = bitmap.getNinePatchChunk();
            boolean isNinePatchChunk = NinePatch.isNinePatchChunk(nine);
            if (isNinePatchChunk) {
                NinePatchDrawable drawable = new NinePatchDrawable(ctx.getResources(), bitmap, nine, new Rect(), null);
                return drawable;
            } else {
                BitmapDrawable drawable = new BitmapDrawable(ctx.getResources(), bitmap);
                return drawable;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StateListDrawable getStateListDrawable(String name, Context ctx) {
        StateListDrawable drawable = new StateListDrawable();
        if (drawable != null) {
            drawable.addState(ViewSeed.getPressed(), getDrawableByAssets(name + "_pressed", ctx));
            drawable.addState(ViewSeed.getEmpty(), getDrawableByAssets(name + "_normal", ctx));
        }
        return drawable;
    }

    public static StateListDrawable getStateListDrawable_9(String name, Context ctx) {
        StateListDrawable drawable = new StateListDrawable();
        if (drawable != null) {
            drawable.addState(ViewSeed.getPressed(), getDrawableByAssets(name + "_pressed.9", ctx));
            drawable.addState(ViewSeed.getEmpty(), getDrawableByAssets(name + "_normal.9", ctx));
        }
        return drawable;
    }

}
