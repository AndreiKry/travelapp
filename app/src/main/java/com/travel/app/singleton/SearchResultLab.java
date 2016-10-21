package com.travel.app.singleton;

import com.travel.app.model.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 26.08.2016.
 */
public class SearchResultLab {

    private static SearchResultLab sResultLab;
    private List<Search> mSearchParams;
    private static final int mPosition = 0;

    public static SearchResultLab get() {
        if (sResultLab == null) {
            sResultLab = new SearchResultLab();
        }
        return sResultLab;
    }

    private SearchResultLab() {
        mSearchParams = new ArrayList<>();
    }

    public Search getSearchParams() {
        return mSearchParams.get(mPosition);
    }

    public void setSearchParams(Search searchParams) {
        mSearchParams.clear();
        mSearchParams.add(searchParams);
    }
}
