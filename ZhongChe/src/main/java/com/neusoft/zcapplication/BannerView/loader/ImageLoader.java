package com.neusoft.zcapplication.BannerView.loader;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片加载器
 */
public abstract class ImageLoader implements ImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
