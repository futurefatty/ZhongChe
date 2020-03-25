package com.neusoft.zcapplication.BannerView;

import android.support.v4.view.ViewPager.PageTransformer;

import com.neusoft.zcapplication.BannerView.transformer.AccordionTransformer;
import com.neusoft.zcapplication.BannerView.transformer.BackgroundToForegroundTransformer;
import com.neusoft.zcapplication.BannerView.transformer.CubeInTransformer;
import com.neusoft.zcapplication.BannerView.transformer.CubeOutTransformer;
import com.neusoft.zcapplication.BannerView.transformer.DefaultTransformer;
import com.neusoft.zcapplication.BannerView.transformer.DepthPageTransformer;
import com.neusoft.zcapplication.BannerView.transformer.FlipHorizontalTransformer;
import com.neusoft.zcapplication.BannerView.transformer.FlipVerticalTransformer;
import com.neusoft.zcapplication.BannerView.transformer.ForegroundToBackgroundTransformer;
import com.neusoft.zcapplication.BannerView.transformer.RotateDownTransformer;
import com.neusoft.zcapplication.BannerView.transformer.RotateUpTransformer;
import com.neusoft.zcapplication.BannerView.transformer.ScaleInOutTransformer;
import com.neusoft.zcapplication.BannerView.transformer.StackTransformer;
import com.neusoft.zcapplication.BannerView.transformer.TabletTransformer;
import com.neusoft.zcapplication.BannerView.transformer.ZoomInTransformer;
import com.neusoft.zcapplication.BannerView.transformer.ZoomOutSlideTransformer;
import com.neusoft.zcapplication.BannerView.transformer.ZoomOutTranformer;

//import com.youth.banner.transformer.AccordionTransformer;
//import com.youth.banner.transformer.BackgroundToForegroundTransformer;
//import com.youth.banner.transformer.CubeInTransformer;
//import com.youth.banner.transformer.CubeOutTransformer;
//import com.youth.banner.transformer.DefaultTransformer;
//import com.youth.banner.transformer.DepthPageTransformer;
//import com.youth.banner.transformer.FlipHorizontalTransformer;
//import com.youth.banner.transformer.FlipVerticalTransformer;
//import com.youth.banner.transformer.ForegroundToBackgroundTransformer;
//import com.youth.banner.transformer.RotateDownTransformer;
//import com.youth.banner.transformer.RotateUpTransformer;
//import com.youth.banner.transformer.ScaleInOutTransformer;
//import com.youth.banner.transformer.StackTransformer;
//import com.youth.banner.transformer.TabletTransformer;
//import com.youth.banner.transformer.ZoomInTransformer;
//import com.youth.banner.transformer.ZoomOutSlideTransformer;
//import com.youth.banner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
