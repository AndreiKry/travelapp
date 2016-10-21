package com.travel.app.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by user on 21.08.2016.
 */
public class StarCount {

    public static void setStarVisibility(ImageView starOne, ImageView starTwo,
                                  ImageView starThree, ImageView starFour,
                                  ImageView starFive, LinearLayout linearLayout,
                                  String hotelStars) {
        int tourStarCount;

        try {
            tourStarCount = Integer.parseInt(hotelStars);
        } catch (NumberFormatException nfe) {
            tourStarCount = 0;
        }

        switch (tourStarCount) {
            case 1:
                starOne.setVisibility(View.VISIBLE);
                starTwo.setVisibility(View.INVISIBLE);
                starThree.setVisibility(View.INVISIBLE);
                starFour.setVisibility(View.INVISIBLE);
                starFive.setVisibility(View.INVISIBLE);
                break;
            case 2:
                starOne.setVisibility(View.VISIBLE);
                starTwo.setVisibility(View.VISIBLE);
                starThree.setVisibility(View.INVISIBLE);
                starFour.setVisibility(View.INVISIBLE);
                starFive.setVisibility(View.INVISIBLE);
                break;
            case 3:
                starOne.setVisibility(View.VISIBLE);
                starTwo.setVisibility(View.VISIBLE);
                starThree.setVisibility(View.VISIBLE);
                starFour.setVisibility(View.INVISIBLE);
                starFive.setVisibility(View.INVISIBLE);
                break;
            case 4:
                starOne.setVisibility(View.VISIBLE);
                starTwo.setVisibility(View.VISIBLE);
                starThree.setVisibility(View.VISIBLE);
                starFour.setVisibility(View.VISIBLE);
                starFive.setVisibility(View.INVISIBLE);
                break;
            case 5:
                starOne.setVisibility(View.VISIBLE);
                starTwo.setVisibility(View.VISIBLE);
                starThree.setVisibility(View.VISIBLE);
                starFour.setVisibility(View.VISIBLE);
                starFive.setVisibility(View.VISIBLE);
                break;
            default:
                starOne.setVisibility(View.INVISIBLE);
                starTwo.setVisibility(View.INVISIBLE);
                starThree.setVisibility(View.INVISIBLE);
                starFour.setVisibility(View.INVISIBLE);
                starFive.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.GONE);
                break;
        }
    }
}
