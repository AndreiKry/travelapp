package com.travel.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.travel.app.R;
import com.travel.app.singleton.TourListLab;
import com.travel.app.singleton.TourSearchLab;
import com.travel.app.helper.StarCount;
import com.travel.app.model.Tour;

import java.util.UUID;

/*import io.github.yavski.fabspeeddial.FabSpeedDial;*/

import static com.travel.app.helper.ColorCharSequence.bold;
import static com.travel.app.helper.ColorCharSequence.color;
import static com.travel.app.helper.ColorCharSequence.normal;

/**
 * Created by user on 16.08.2016.
 */
public class TourSingleFragment extends Fragment {

    private static final String ARG_REQUEST_CODE = "req_code";
    private static final String ARG_TOUR_ID = "tour_id";
    private static final String DIALOG_FORM = "DialogForm";
    private static final int DIALOG_TOUR = 0;

    private TextView mSingleHotel, mSingleCountry, mSingleDescription,
                     mSingleFrom, mSingleDate, mSingleMeal,
                     mSingleDays, mSinglePeople, mSinglePrice, mSingleTitle;
    private ImageView mSingleStarOne, mSingleStarTwo,
            mSingleStarThree, mSingleStarFour, mSingleStarFive;
    private LinearLayout mLinearLayoutHotelStars;
    private int mColorAccent;
    private int mColorPrimaryText;


    private Tour mTour;
    private int mReqCode;

    public static TourSingleFragment newInstance(int reqCode, UUID tourId) {
        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, reqCode);
        args.putSerializable(ARG_TOUR_ID, tourId);

        TourSingleFragment fragment = new TourSingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID tourId = (UUID) getArguments().getSerializable(ARG_TOUR_ID);
        mReqCode = getArguments().getInt(ARG_REQUEST_CODE);
        if (mReqCode == 0) {
            mTour = TourListLab.get().getTour(tourId);
        } else {
            mTour = TourSearchLab.get().getTour(tourId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_single, container, false);

        mColorAccent = ContextCompat.getColor(getActivity(),R.color.colorAccent);
        mColorPrimaryText = ContextCompat.getColor(getActivity(), R.color.colorPrimaryText);

        mSingleTitle = (TextView) view.findViewById(R.id.single_title);
        mSingleHotel = (TextView) view.findViewById(R.id.single_hotel);
        mSingleCountry = (TextView) view.findViewById(R.id.single_country);
        mSingleDescription = (TextView) view.findViewById(R.id.single_description);
        mSingleFrom = (TextView) view.findViewById(R.id.single_from);
        mSingleDate = (TextView) view.findViewById(R.id.single_date);
        mSingleMeal = (TextView) view.findViewById(R.id.single_meal);
        mSingleDays = (TextView) view.findViewById(R.id.single_days);
        mSinglePeople = (TextView) view.findViewById(R.id.single_people);
        mSinglePrice = (TextView) view.findViewById(R.id.single_price);

        mSingleStarOne = (ImageView) view.findViewById(R.id.single_hotel_star_one);
        mSingleStarTwo = (ImageView) view.findViewById(R.id.single_hotel_star_two);
        mSingleStarThree = (ImageView) view.findViewById(R.id.single_hotel_star_three);
        mSingleStarFour = (ImageView) view.findViewById(R.id.single_hotel_star_four);
        mSingleStarFive = (ImageView) view.findViewById(R.id.single_hotel_star_five);
        mLinearLayoutHotelStars = (LinearLayout) view.findViewById(R.id.single_hotel_stars);

        StarCount.setStarVisibility(mSingleStarOne, mSingleStarTwo,
                mSingleStarThree, mSingleStarFour,
                mSingleStarFive, mLinearLayoutHotelStars,
                mTour.getStar());

        mSingleTitle.setText(mTour.getTitle());
        mSingleHotel.setText(mTour.getHotel());
        mSingleCountry.setText(mTour.getCountry());
        mSingleDescription.setText(mTour.getDescription());
        mSingleFrom.setText(getString(R.string.single_tour_from, mTour.getFrom()));
        mSingleDate.setText(getString(R.string.single_tour_date, mTour.getDate()));
        mSingleMeal.setText(getString(R.string.single_tour_meal, mTour.getMeal()));
        mSingleDays.setText(getString(R.string.single_tour_days, mTour.getDay()));
        mSinglePeople.setText(getString(R.string.single_tour_people, mTour.getPeople()));
        CharSequence priceFormatted = normal(
                color(mColorPrimaryText, getString(R.string.single_tour_price_start)),
                bold(color(mColorAccent, getString(R.string.single_tour_price_end, mTour.getPrice())))

        );
        mSinglePrice.setText(priceFormatted);

        FloatingActionButton callButton = (FloatingActionButton) view.findViewById(R.id.fab_action_call);
        FloatingActionButton emailButton = (FloatingActionButton) view.findViewById(R.id.fab_action_email);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+mTour.getPhone()));
                startActivity(callIntent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                SendFormFragment dialog = SendFormFragment.newInstance(DIALOG_TOUR, mReqCode, mTour.getId());
                dialog.show(fragmentManager, DIALOG_FORM);
            }
        });

        ScrollView mScrollView = (ScrollView) view.findViewById(R.id.single_scroll);
        mScrollView.requestFocus();

        return view;
    }

}
