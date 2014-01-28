package com.example.picasso;

import android.graphics.Color;

import com.squareup.picasso.Picasso;

final class Data {
  static final String BASE = "http://i.imgur.com/";
  static final String EXT = ".jpg";
  static final String[] URLS = {
      BASE + "CqmBjo5" + EXT, BASE + "zkaAooq" + EXT, BASE + "0gqnEaY" + EXT,
      BASE + "9gbQ7YR" + EXT, BASE + "aFhEEby" + EXT, BASE + "0E2tgV7" + EXT,
      BASE + "P5JLfjk" + EXT, BASE + "nz67a4F" + EXT, BASE + "dFH34N5" + EXT,
      BASE + "FI49ftb" + EXT, BASE + "DvpvklR" + EXT, BASE + "DNKnbG8" + EXT,
      BASE + "yAdbrLp" + EXT, BASE + "55w5Km7" + EXT, BASE + "NIwNTMR" + EXT,
      BASE + "DAl0KB8" + EXT, BASE + "xZLIYFV" + EXT, BASE + "HvTyeh3" + EXT,
      BASE + "Ig9oHCM" + EXT, BASE + "7GUv9qa" + EXT, BASE + "i5vXmXp" + EXT,
      BASE + "glyvuXg" + EXT, BASE + "u6JF6JZ" + EXT, BASE + "ExwR7ap" + EXT,
      BASE + "Q54zMKT" + EXT, BASE + "9t6hLbm" + EXT, BASE + "F8n3Ic6" + EXT,
      BASE + "P5ZRSvT" + EXT, BASE + "jbemFzr" + EXT, BASE + "8B7haIK" + EXT,
      BASE + "aSeTYQr" + EXT, BASE + "OKvWoTh" + EXT, BASE + "zD3gT4Z" + EXT,
      BASE + "z77CaIt" + EXT,
  };

  static final String[] COLORS = {
		  Picasso.SCHEME_CUSTOM + "://" + Color.BLACK,
		  Picasso.SCHEME_CUSTOM + "://" + Color.BLUE,
		  Picasso.SCHEME_CUSTOM + "://" + Color.RED,
		  Picasso.SCHEME_CUSTOM + "://" + Color.CYAN,
		  Picasso.SCHEME_CUSTOM + "://" + Color.DKGRAY,
		  Picasso.SCHEME_CUSTOM + "://" + Color.GRAY,
		  Picasso.SCHEME_CUSTOM + "://" + Color.GREEN,
		  Picasso.SCHEME_CUSTOM + "://" + Color.LTGRAY,
		  Picasso.SCHEME_CUSTOM + "://" + Color.MAGENTA,
		  Picasso.SCHEME_CUSTOM + "://" + Color.WHITE,
		  Picasso.SCHEME_CUSTOM + "://" + Color.YELLOW
  };

  private Data() {
    // No instances.
  }
}
