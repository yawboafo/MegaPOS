package com.nfortics.megapos.Helper;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by bigifre on 7/6/2015.
 */
public class Typefacer {
    public Typeface getRoboBlack(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Roboto-Black.ttf");
    }


    public Typeface getRoboMediumitalic(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Roboto-MediumItalic.ttf");
    }

    public Typeface getRoboThinItalic(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Roboto-ThinItalic.ttf");
    }

    public Typeface getRoboCondensedLight(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/RobotoCondensed-Light.ttf");
    }

    public Typeface getRoboCondensedLghtItalic(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/RobotoCondensed-LightItalic.ttf");
    }

    public Typeface getRoboRegular(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Roboto-Regular.ttf");
    }

    public Typeface getRoboRealThin(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Roboto-Thin.ttf");
    }

    public Typeface getRoboCondensedRegular(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/RobotoCondensed-Regular.ttf");
    }
    public Typeface getRoboCondensedBold(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/RobotoCondensed-Bold.ttf");
    }

    public Typeface getBytes(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Bytes.TTF");
    }

    public Typeface getRobThin(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Roboto-Thin.ttf");
    }

    public Typeface getRoboBold(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/Roboto-Bold.ttf");
    }
    public Typeface getDigital7mono(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/digitalmono.ttf");
    }

    public Typeface getDigital7Italic(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/digitalitalic.ttf");
    }

    public Typeface getDigital7normal(AssetManager mgr) {
        return Typeface.createFromAsset(mgr, "fonts/digitalnormal.ttf");
    }
}
