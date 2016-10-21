package com.travel.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.travel.app.R;
import com.travel.app.model.Search;
import com.travel.app.activity.SearchResultActivity;
import com.travel.app.singleton.SearchResultLab;

import static com.travel.app.helper.ColorCharSequence.normal;
import static com.travel.app.helper.ColorCharSequence.color;
import static com.travel.app.helper.ColorCharSequence.bold;

/**
 * Created by user on 10.08.2016.
 */
public class TourSearchFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private static final int MAIN_CODE = 0;
    private static final int DETAIL_CODE = 1;
    private static final String ARG_MASTERDETAIL = "masterdetail";

    private RangeBar mRangeBar;
    private EditText mFromEditText, mWhereEditText;
    private TextView mFormatTextView;
    private Button mButton;

    private String mSearchLeft, mSearchRight;
    private CharSequence mStyleCharSequence;
    private SearchTourCallbacks mSearchTourCallbacks;
    private int mColorAccent;
    private int mColorPrimaryText;
    private int mArgMasterdetail;

    public interface SearchTourCallbacks {
        void onSearchClick();
    }

    public static TourSearchFragment newInstance(int masterdetail) {
        Bundle args = new Bundle();
        args.putInt(ARG_MASTERDETAIL, masterdetail);
        TourSearchFragment fragment = new TourSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSearchTourCallbacks = (SearchTourCallbacks) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_search, container, false);

        mArgMasterdetail = getArguments().getInt(ARG_MASTERDETAIL);
        mFromEditText = (EditText) view.findViewById(R.id.search_tour_from);
        mWhereEditText = (EditText) view.findViewById(R.id.search_tour_where);
        mFormatTextView = (TextView) view.findViewById(R.id.search_tour_format);
        mRangeBar = (RangeBar) view.findViewById(R.id.search_tour_rangebar);
        mButton = (Button) view.findViewById(R.id.search_tour_button);
        mRangeBar.setRangePinsByIndices(4,10);
        mColorAccent = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        mColorPrimaryText = ContextCompat.getColor(getActivity(), R.color.colorPrimaryText);

        updateTextView(mRangeBar.getLeftPinValue(), mRangeBar.getRightPinValue());

        mRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                updateTextView(leftPinValue, rightPinValue);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search searchParams = new Search();
                searchParams.setFrom(mFromEditText.getText().toString());
                searchParams.setWhere(mWhereEditText.getText().toString());
                searchParams.setDaysStart(mSearchLeft);
                searchParams.setDaysEnd(mSearchRight);
                SearchResultLab.get().setSearchParams(searchParams);

                if (mArgMasterdetail == MAIN_CODE) {
                    // активити с фрагментом TourListFragment
                    Intent intent = SearchResultActivity.newIntent(
                            getActivity(), REQUEST_CODE
                    );
                    startActivity(intent);
                } else {
                    // фрагмент TourListFragment
                    mSearchTourCallbacks.onSearchClick();
                }
            }
        });

        return view;
    }

    private void updateTextView(String leftValue, String rightValue) {
        mStyleCharSequence = normal(
                color(mColorPrimaryText, getString(R.string.search_tour_text_start)),
                bold(color(mColorAccent, getString(R.string.search_tour_format_start, leftValue))),
                color(mColorPrimaryText, getString(R.string.search_tour_text_middle)),
                bold(color(mColorAccent, getString(R.string.search_tour_format_end, rightValue))),
                color(mColorPrimaryText, getString(R.string.search_tour_text_end))
        );
        mFormatTextView.setText(mStyleCharSequence);
        mSearchLeft = leftValue;
        mSearchRight = rightValue;
    }
}
