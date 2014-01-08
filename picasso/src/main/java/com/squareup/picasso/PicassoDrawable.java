/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.picasso;

import static android.graphics.Color.WHITE;
import static com.squareup.picasso.Picasso.LoadedFrom.MEMORY;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.widget.ImageView;

final public class PicassoDrawable extends Drawable implements Runnable {
  // Only accessed from main thread.
  private static final Paint DEBUG_PAINT = new Paint();

  

  /**
   * Create or update the drawable on the target {@link ImageView} to display the supplied bitmap
   * image.
   */
  static void setBitmap(ImageView target, Context context, Bitmap bitmap,
      Picasso.LoadedFrom loadedFrom, long fadeTime, boolean debugging) {
    Drawable placeholder = target.getDrawable();
    if (placeholder instanceof AnimationDrawable) {
      ((AnimationDrawable) placeholder).stop();
    }
    PicassoDrawable drawable =
        new PicassoDrawable(context, placeholder, bitmap, loadedFrom, fadeTime, debugging);
    target.setImageDrawable(drawable);
  }

  /**
   * Create or update the drawable on the target {@link ImageView} to display the supplied
   * placeholder image.
   */
  static void setPlaceholder(ImageView target, int placeholderResId, Drawable placeholderDrawable) {
    if (placeholderResId != 0) {
      target.setImageResource(placeholderResId);
    } else if( null != placeholderDrawable ){
      target.setImageDrawable(placeholderDrawable);
    }
    if (target.getDrawable() instanceof AnimationDrawable) {
      ((AnimationDrawable) target.getDrawable()).start();
    }
  }

  private final boolean debugging;
  private final float density;
  private final Picasso.LoadedFrom loadedFrom;
  final BitmapDrawable image;

  Drawable placeholder;

  long startTimeMillis;
  boolean animating;
  double alpha = 0xFF;
  long fadeTime;

  PicassoDrawable(Context context, Drawable placeholder, Bitmap bitmap,
      Picasso.LoadedFrom loadedFrom, long fadeTime, boolean debugging) {
    Resources res = context.getResources();
    
    this.debugging = debugging;
    this.density = res.getDisplayMetrics().density;

    this.loadedFrom = loadedFrom;

    this.image = new BitmapDrawable(res, bitmap);
    
    this.fadeTime = fadeTime;

    boolean fade = loadedFrom != MEMORY && (fadeTime > 0);
    
    if (fade) {
      this.placeholder = placeholder;
      animating = true;
      startTimeMillis = SystemClock.uptimeMillis();
    }
  }
  
  public void recycle() {
    if( null != image && null != image.getBitmap() && image.getBitmap().isRecycled() ) {
      image.getBitmap().recycle();
    }
  }

  @Override public void draw(Canvas canvas) {
    if (!animating) {
      image.draw(canvas);
    } else {
      double normalized = (double)(SystemClock.uptimeMillis() - startTimeMillis) / fadeTime;
      if (normalized >= 1f) {
        animating = false;
        placeholder = null;
        image.draw(canvas);
      } else {
        if (placeholder != null) {
          placeholder.draw(canvas);
        }

        int partialAlpha = (int) (alpha * normalized);
        image.setAlpha(partialAlpha);
        image.draw(canvas);
        image.setAlpha((int) alpha);
        scheduleSelf( this, SystemClock.uptimeMillis() + 16 );
      }
    }

    if (debugging) {
      drawDebugIndicator(canvas);
    }
  }
  
  @Override
	public void run() {
	  invalidateSelf();
	}

  @Override public int getIntrinsicWidth() {
    return image.getIntrinsicWidth();
  }

  @Override public int getIntrinsicHeight() {
    return image.getIntrinsicHeight();
  }

  @Override public void setAlpha(int alpha) {
    this.alpha = alpha;
    if (placeholder != null) {
      placeholder.setAlpha(alpha);
    }
    image.setAlpha(alpha);
  }

  @Override public void setColorFilter(ColorFilter cf) {
    if (placeholder != null) {
      placeholder.setColorFilter(cf);
    }
    image.setColorFilter(cf);
  }

  @Override public int getOpacity() {
    return image.getOpacity();
  }

  @Override protected void onBoundsChange(Rect bounds) {
    super.onBoundsChange(bounds);

    image.setBounds(bounds);
    if (placeholder != null) {
      placeholder.setBounds(bounds);
    }
  }

  private void drawDebugIndicator(Canvas canvas) {
    DEBUG_PAINT.setColor(WHITE);
    Path path = getTrianglePath(new Point(0, 0), (int) (16 * density));
    canvas.drawPath(path, DEBUG_PAINT);

    DEBUG_PAINT.setColor(loadedFrom.debugColor);
    path = getTrianglePath(new Point(0, 0), (int) (15 * density));
    canvas.drawPath(path, DEBUG_PAINT);
  }

  private static Path getTrianglePath(Point p1, int width) {
    Point p2 = new Point(p1.x + width, p1.y);
    Point p3 = new Point(p1.x, p1.y + width);

    Path path = new Path();
    path.moveTo(p1.x, p1.y);
    path.lineTo(p2.x, p2.y);
    path.lineTo(p3.x, p3.y);

    return path;
  }
}
