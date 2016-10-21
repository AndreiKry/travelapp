package com.travel.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.travel.app.helper.EndlessRecyclerOnScrollListener;
import com.travel.app.helper.InternetConnection;
import com.travel.app.helper.SampleData;
import com.travel.app.singleton.PageLab;
import com.travel.app.R;
import com.travel.app.singleton.TourListLab;
import com.travel.app.helper.StarCount;
import com.travel.app.model.Tour;
import com.travel.app.helper.VolleyRequestQueue;

import java.util.List;


/**
 * Created by user on 10.08.2016.
 */

public class TourListFragment extends Fragment {

    private static final String RECYCLER_TOUR = "recycler_tour";
    private static final String SINGLE_TOUR = "single_tour";
    private static final String ARG_REQ_CODE = "req_code";
    private static final int REQ_CODE_SIMPLE = 0;
    private static final int REQ_CODE_SEARCH = 1;

    private TourAdapter mTourAdapter;
    private RecyclerView mTourRecyclerView;
    private SwipeRefreshLayout mTourSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Button mReconnectButton, mRefillButton;
    private LinearLayout mErrorLinearLayout;
    private TextView mErrorTextView;
    private RecyclerTourCallbacks mRecyclerTourCallbacks;
    private List<Tour> mEmptyList = null;
    /*private List<Tour> mNineList;*/

    /*private String mSearchFrom, mSearchWhere, mSearchDaysStart, mSearchDaysEnd;*/
    private int mRequestCode;
    /*private JsonObjectRequest jsonReq;*/

    public interface RecyclerTourCallbacks {
        void onTourClick(Tour tour, int requestCode);
        void onEmptyResult(Activity activity);
    }

    public static TourListFragment newInstance(int reqCode) {
        Bundle args = new Bundle();
        args.putInt(ARG_REQ_CODE, reqCode);
        TourListFragment fragment = new TourListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRecyclerTourCallbacks = (RecyclerTourCallbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestCode = getArguments().getInt(ARG_REQ_CODE);
        //SampleData.generateTours();
        /*if (mRequestCode == REQ_CODE_SEARCH) {
            Search searchParams = SearchResultLab.get().getSearchParams();
            mSearchFrom = searchParams.getFrom();
            mSearchWhere = searchParams.getWhere();
            mSearchDaysStart = searchParams.getDaysStart();
            mSearchDaysEnd = searchParams.getDaysEnd();
            TourSearchLab.get().clearSearchList();
        }*/

        updateUI(mRequestCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_list, container, false);

        mTourRecyclerView = (RecyclerView) view.findViewById(R.id.tours_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mTourRecyclerView.setLayoutManager(mLinearLayoutManager);
        mTourRecyclerView.setAdapter(mTourAdapter);
        mTourSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tours_swipe_refresh_layout);
        mTourSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent)
        );
        mErrorLinearLayout = (LinearLayout) view.findViewById(R.id.tour_error_layout);
        mErrorTextView = (TextView) view.findViewById(R.id.tours_error_text);
        mReconnectButton = (Button) view.findViewById(R.id.tours_error_connection);
        mRefillButton = (Button) view.findViewById(R.id.tours_error_empty);

        mTourSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*if (mRequestCode == REQ_CODE_SIMPLE) {
                    TourListLab.get().clearTourList();
                } else {
                    TourSearchLab.get().clearSearchList();
                }
                mTourAdapter = new TourAdapter(mEmptyList);
                mTourRecyclerView.setAdapter(mTourAdapter);
                updateUI(mRequestCode);
                mTourRecyclerView.setAdapter(mTourAdapter);
                PageLab.setPageOne(mRequestCode);
                fetchData(mRequestCode);*/

                TourListLab.get().clearTourList();
                mTourAdapter = new TourAdapter(mEmptyList);
                mTourRecyclerView.setAdapter(mTourAdapter);
                updateUI(mRequestCode);
                mTourRecyclerView.setAdapter(mTourAdapter);
                fillList(mTourAdapter);
            }
        });

        mTourRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                //fetchData(mRequestCode);
                fillList(mTourAdapter);
            }

            @Override
            protected int getCurrentPage() {
                return PageLab.getPage(mRequestCode);
            }
        });

        mReconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fetchData(mRequestCode);
                fillList(mTourAdapter);
            }
        });

        mRefillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerTourCallbacks.onEmptyResult(getActivity());
            }
        });

        //if (getTours(mRequestCode).isEmpty()) {
        if (TourListLab.get().getTours().isEmpty()) {
            //fetchData(mRequestCode);
            fillList(mTourAdapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(mRequestCode);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTourSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onStop() {
        super.onStop();
        //mNineList = new ArrayList<>();
        //VolleyRequestQueue.getInstance(getActivity()).getRequestQueue().cancelAll(RECYCLER_TOUR);
    }

    public void updateUI(int reqCode) {
        //mTourAdapter = new TourAdapter(getTours(reqCode));
        mTourAdapter = new TourAdapter(TourListLab.get().getTours());
    }

    /*public List<Tour> getTours(int reqCode) {
        List<Tour> tours;
        if (reqCode == REQ_CODE_SIMPLE) {
            TourListLab tourLab = TourListLab.get();
            tours = tourLab.getTours();
        } else {
            TourSearchLab tourLab = TourSearchLab.get();
            tours = tourLab.getTours();
        }

        return tours;
    }*/

    public void fillList(TourAdapter tourAdapter) {
        /*if (getTours(mRequestCode).size() == 0) {
            mTourSwipeRefreshLayout.setRefreshing(true);
        }*/
        if (InternetConnection.isConnected(getContext())) {
            mTourRecyclerView.setVisibility(View.VISIBLE);
            mErrorLinearLayout.setVisibility(View.GONE);
            SampleData.generateTours();
            tourAdapter.notifyDataSetChanged();
            mTourSwipeRefreshLayout.setRefreshing(false);
        } else {
            mTourRecyclerView.setVisibility(View.GONE);
            mErrorLinearLayout.setVisibility(View.VISIBLE);
            mErrorTextView.setText(getString(R.string.error_internet));
            mReconnectButton.setVisibility(View.VISIBLE);
        }
    }

    /*public void fetchData(int reqCode) {
        String toursUrl;
        int page = PageLab.getPage(reqCode);
        if (InternetConnection.isConnected(getContext())) {
            mTourRecyclerView.setVisibility(View.VISIBLE);
            mErrorLinearLayout.setVisibility(View.GONE);
            if (reqCode == REQ_CODE_SIMPLE) {
                //toursUrl = getString(R.string.list_url, String.valueOf(page));
                toursUrl = getString(R.string.list_url);
            } else {
                toursUrl = getString(R.string.search_url);
            }
            getTourParseResult(mTourAdapter, reqCode, toursUrl);
            PageLab.setPage(reqCode);
        } else {
            mTourRecyclerView.setVisibility(View.GONE);
            mErrorLinearLayout.setVisibility(View.VISIBLE);
            mErrorTextView.setText(getString(R.string.error_internet));
            mReconnectButton.setVisibility(View.VISIBLE);
        }
    }*/

    /*public void getTourParseResult(final TourListFragment.TourAdapter tourAdapter, final int reqCode, String fromUrl) {

        if (getTours(mRequestCode).size() == 0) {
            mTourSwipeRefreshLayout.setRefreshing(true);
        }

        jsonReq = new JsonObjectRequest(Request.Method.GET,
                fromUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        int countTotal = response.getInt("count_total");

                        if (countTotal == 0) {
                            mTourRecyclerView.setVisibility(View.GONE);
                            mErrorLinearLayout.setVisibility(View.VISIBLE);
                            mErrorTextView.setText(getString(R.string.error_empty));
                            mRefillButton.setVisibility(View.VISIBLE);
                        } else {
                            mErrorLinearLayout.setVisibility(View.GONE);
                            mTourRecyclerView.setVisibility(View.VISIBLE);
                        }

                        JSONArray toursArray = response.getJSONArray("tours");
                        mNineList = new ArrayList<>();

                        for (int i = 0; i < toursArray.length(); i++) {
                            JSONObject tourObject = postsArray.getJSONObject(i);
                            Tour tour = new Tour();
                            //parse tourObject
                            mNineList.add(tour);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // notify data changes to list adapater
                if (reqCode == REQ_CODE_SIMPLE) {
                    TourListLab.get().addAllTour(mNineList);
                } else {
                    TourSearchLab.get().addAllTour(mNineList);
                }
                tourAdapter.notifyDataSetChanged();
                mTourSwipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Do something
            }
        });
        // Adding request to volley request queue
        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonReq);
    }*/

    public class TourHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Tour mTour;
        public NetworkImageView mTourImageView;
        private ImageLoader mImageLoader;
        private TextView mTourPrice, mTourCountry, mTourHotel, mTourDateDaysFrom;
        private ImageView mStarOne, mStarTwo, mStarThree, mStarFour, mStarFive;
        private LinearLayout mHotelStars;

        public TourHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTourImageView = (NetworkImageView) itemView.findViewById(R.id.tour_image);
            mImageLoader = VolleyRequestQueue.getInstance(getActivity()).getImageLoader();

            mStarOne = (ImageView) itemView.findViewById(R.id.tour_hotel_star_one);
            mStarTwo = (ImageView) itemView.findViewById(R.id.tour_hotel_star_two);
            mStarThree = (ImageView) itemView.findViewById(R.id.tour_hotel_star_three);
            mStarFour = (ImageView) itemView.findViewById(R.id.tour_hotel_star_four);
            mStarFive = (ImageView) itemView.findViewById(R.id.tour_hotel_star_five);
            mHotelStars = (LinearLayout) itemView.findViewById(R.id.tour_hotel_stars);
            mTourPrice = (TextView) itemView.findViewById(R.id.tour_price);
            mTourCountry = (TextView) itemView.findViewById(R.id.tour_country);
            mTourHotel = (TextView) itemView.findViewById(R.id.tour_hotel);
            mTourDateDaysFrom = (TextView) itemView.findViewById(R.id.tour_days_date_from);
        }

        public void bindTour(Tour tour) {
            mTour = tour;

            mTourPrice.setText(mTour.getPrice());
            mTourCountry.setText(mTour.getCountry());
            mTourHotel.setText(mTour.getHotel());
            String formatString = getString(R.string.list_tour_formatted_string,
                    mTour.getDay(),
                    mTour.getDate(),
                    mTour.getFrom());
            CharSequence styleString = Html.fromHtml(formatString);
            mTourDateDaysFrom.setText(styleString);
            // ImageView | default | error
            mImageLoader.get(mTour.getThumbnailUrl(),
                    ImageLoader.getImageListener(mTourImageView,
                            R.drawable.default_image_for_loader,
                            R.drawable.default_image_for_loader));
            mTourImageView.setImageUrl(mTour.getThumbnailUrl(), mImageLoader);

            StarCount.setStarVisibility(
                    mStarOne, mStarTwo,
                    mStarThree, mStarFour,
                    mStarFive, mHotelStars,
                    mTour.getStar());
        }

        @Override
        public void onClick(View view) {
            mRecyclerTourCallbacks.onTourClick(mTour, mRequestCode);
        }
    }

    public class TourAdapter extends RecyclerView.Adapter<TourHolder> {

        private List<Tour> mTours;

        public TourAdapter(List<Tour> tours) {
            mTours = tours;
        }

        @Override
        public TourHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_list_recycler, parent, false);
            return new TourHolder(view);
        }

        @Override
        public void onBindViewHolder(TourHolder holder, int position) {
            Tour tour = mTours.get(position);
            holder.bindTour(tour);

            Matrix matrix = holder.mTourImageView.getImageMatrix();
            matrix.postTranslate(0,-100);

            holder.mTourImageView.setImageMatrix(matrix);
            holder.itemView.setTag(holder);
        }

        @Override
        public int getItemCount() {
            return mTours.size();
        }

        @Override
        public void onViewRecycled(TourHolder holder) {
            super.onViewRecycled(holder);

            Matrix matrix = holder.mTourImageView.getImageMatrix();
            matrix.reset();
            holder.mTourImageView.setImageMatrix(matrix);
        }
    }
}
