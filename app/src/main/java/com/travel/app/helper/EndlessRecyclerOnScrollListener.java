package com.travel.app.helper;

import android.graphics.Matrix;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.travel.app.fragment.TourListFragment;

/**
 * Created by user on 14.03.2016.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int startingPageIndex = 1; // Sets the starting page index
    private int currentPage = 1;
    private float scrollSpeed = 0.5f;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    protected int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        Matrix imageMatrix;
        float tempSpeed = 0;

        if (dy > 0) {
            tempSpeed = scrollSpeed;
        } else if (dy < 0) {
            tempSpeed = -scrollSpeed;
        }

        for (int i = firstVisibleItem; i < (firstVisibleItem + visibleItemCount); i++) {
            ImageView imageView = ((TourListFragment.TourHolder) mLinearLayoutManager.findViewByPosition(i).getTag()).mTourImageView;
            imageMatrix = imageView.getImageMatrix();
            imageMatrix.postTranslate(0, tempSpeed);
            imageView.setImageMatrix(imageMatrix);
            imageView.invalidate();
        }

        if (totalItemCount < previousTotal) {
            currentPage = startingPageIndex;
            previousTotal = totalItemCount;
            if (totalItemCount == 0) { loading = true; }
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            currentPage = getCurrentPage();
            //currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);

}