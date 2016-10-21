package com.travel.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.travel.app.helper.FormSender;
import com.travel.app.R;
import com.travel.app.helper.SingleFragmentActivity;
import com.travel.app.fragment.SendFormFragment;
import com.travel.app.fragment.TourSingleFragment;

import java.util.UUID;

/**
 * Created by user on 16.08.2016.
 */
public class SingleTourActivity extends SingleFragmentActivity implements SendFormFragment.NoticeDialogListener{

    private static final String EXTRA_REQUEST_CODE = "ru.turizmufa.sunshinetravel.req_code_single";
    private static final String EXTRA_TOUR_ID = "ru.turizmufa.sunshinetravel.tour_id";
    private static final int REQ_CODE_SIMPLE = 0;

    public static Intent newIntent(Context context, int reqCode, UUID tourId) {
        Intent intent = new Intent(context, SingleTourActivity.class);
        intent.putExtra(EXTRA_REQUEST_CODE, reqCode);
        intent.putExtra(EXTRA_TOUR_ID, tourId);

        return intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_tour;
    }

    @Override
    protected Fragment CreateFragment() {
        int reqCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, REQ_CODE_SIMPLE);
        UUID uuid  = (UUID) getIntent().getSerializableExtra(EXTRA_TOUR_ID);
        return TourSingleFragment.newInstance(reqCode, uuid);
    }

    @Override
    public void onDialogPositiveClick(String name, String email, String phone, String comment, int version) {

        FormSender.sendForm(this, name, email, phone, comment, version);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

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
}
