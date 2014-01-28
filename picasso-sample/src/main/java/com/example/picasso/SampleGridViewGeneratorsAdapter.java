package com.example.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Generator;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

final class SampleGridViewGeneratorsAdapter extends BaseAdapter {
  private final Context context;
  private final List<String> urls = new ArrayList<String>();

  public SampleGridViewGeneratorsAdapter( Context context ) {
    this.context = context;

    // Ensure we get a different ordering of images on each run.
    Collections.addAll(urls, Data.COLORS);
    Collections.shuffle(urls);

	  Log.d( Picasso.LOG_TAG, "urls: " + urls );

    // Triple up the list.
    ArrayList<String> copy = new ArrayList<String>(urls);
    urls.addAll(copy);
    urls.addAll(copy);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    SquaredImageView view = (SquaredImageView) convertView;
    if (view == null) {
      view = new SquaredImageView(context);
      view.setScaleType(CENTER_CROP);
    }

    // Get the image URL for the current position.
    String url = getItem(position);

    // Trigger the download of the URL asynchronously into the image view.
    Picasso.with(context) //
        .load(url) //
		.withGenerator( new Generator() {
			@Override
			public Bitmap decode( final Uri uri ) throws IOException {
				int color = Integer.parseInt( uri.getHost() );
				int bgColor = Color.argb( 100, Color.red( color ), Color.green( color ), Color.blue( color ) );

				Bitmap bitmap = Bitmap.createBitmap( 400, 400, Bitmap.Config.ARGB_8888 );
				bitmap.eraseColor( bgColor );

				Canvas canvas = new Canvas( bitmap );
				Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
				paint.setColor( color );
				canvas.drawCircle( 200, 200, 100, paint );

				return bitmap;
			}
		} )
        .error( R.drawable.error ) //
        .fit() //
        .into(view);

    return view;
  }

  @Override public int getCount() {
    return urls.size();
  }

  @Override public String getItem(int position) {
    return urls.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }
}
