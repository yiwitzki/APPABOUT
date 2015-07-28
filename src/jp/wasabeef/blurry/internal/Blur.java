package jp.wasabeef.blurry.internal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Blur {

    public static Bitmap rs(View view, BlurFactor factor) {
        int width = factor.width / factor.sampling;
        int height = factor.height / factor.sampling;

        if (Helper.hasZero(width, height)) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) factor.sampling, 1 / (float) factor.sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        PorterDuffColorFilter filter =
                new PorterDuffColorFilter(factor.color, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);

        view.buildDrawingCache();
        Bitmap cache = view.getDrawingCache();
        canvas.drawBitmap(cache, 0, 0, paint);
        cache.recycle();
        view.destroyDrawingCache();

        RenderScript rs = RenderScript.create(view.getContext());
        Allocation input = Allocation.createFromBitmap(rs, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        blur.setInput(input);
        blur.setRadius(factor.radius);
        blur.forEach(output);
        output.copyTo(bitmap);

        rs.destroy();

        if (factor.sampling == BlurFactor.DEFAULT_SAMPLING) {
            return bitmap;
        } else {
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, factor.width, factor.height, true);
            bitmap.recycle();
            return scaled;
        }
    }
}
