package com.travel.app.helper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by user on 29.09.2016.
 */

public class AnimationNetworkImageView extends NetworkImageView {

    private static final int FADE_IN_TIME_MS = 200;
    private boolean shouldAnimate = false;

    public AnimationNetworkImageView(Context context) {
        super(context);
    }

    public AnimationNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if(shouldAnimate) {
            ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1).setDuration(FADE_IN_TIME_MS).start();
            /*TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                    new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorAccentFade)),
                    new BitmapDrawable(getContext().getResources(), bm)
            });

            setImageDrawable(td);
            td.setCrossFadeEnabled(true);
            td.startTransition(FADE_IN_TIME_MS);*/
        }
        shouldAnimate = false;
    }

    @Override
    public void setImageUrl(String url, ImageLoader imageLoader) {
        shouldAnimate = determineIfAnimationIsNecessary(url, imageLoader);
        super.setImageUrl(url, imageLoader);
    }

    public boolean determineIfAnimationIsNecessary(String imgUrl, ImageLoader imageLoader) {
        int width = getWidth();
        int height = getHeight();

        boolean wrapWidth = false, wrapHeight = false;
        if (getLayoutParams() != null) {
            wrapWidth = getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
            wrapHeight = getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        // Calculate the max image width / height to use while ignoring WRAP_CONTENT dimens.
        int maxWidth = wrapWidth ? 0 : width;
        int maxHeight = wrapHeight ? 0 : height;

        return !imageLoader.isCached(imgUrl, maxWidth, maxHeight);
    }
}