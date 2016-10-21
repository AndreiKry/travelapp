package com.travel.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.travel.app.R;
import com.travel.app.fragment.TourListFragment;
import com.travel.app.helper.SingleFragmentActivity;
import com.travel.app.model.Tour;

/**
 * Created by user on 10.08.2016.
 */
public class SearchResultActivity extends SingleFragmentActivity implements TourListFragment.RecyclerTourCallbacks {

    private static final String EXTRA_SEARCH_CODE = "ru.turizmufa.sunshinetravel.search_code";

    public static Intent newIntent(Context context, int searchCode) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_CODE, searchCode);
        return intent;
    }

    @Override
    protected Fragment CreateFragment() {
        //int reqCode = getIntent().getIntExtra(EXTRA_SEARCH_CODE, 1);
        int reqCode = 0;
        return TourListFragment.newInstance(reqCode);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEmptyResult(Activity activity) {
        NavUtils.navigateUpFromSameTask(activity);
    }

    @Override
    public void onTourClick(Tour tour, int requestCode) {
        Intent intent = SingleTourActivity.newIntent(this, requestCode, tour.getId());
        startActivity(intent);
    }
}
