package com.travel.app.singleton;

public class PageLab {

    private static final int REQ_CODE_SIMPLE = 0;
    private static final int FIRST_PAGE = 1;

    public static int getPage(int reqCode) {
        int page;
        if (reqCode == REQ_CODE_SIMPLE) {
            page = TourListLab.get().getLentaPage();
        } else {
            page = TourSearchLab.get().getSearchPage();
        }
        return page;
    }

    public static void setPage(int reqCode) {
        int page = getPage(reqCode);
        page++;
        if (reqCode == REQ_CODE_SIMPLE) {
            TourListLab.get().setLentaPage(page);
        } else {
            TourSearchLab.get().setSearchPage(page);
        }
    }

    public static void setPageOne(int reqCode) {
        if (reqCode == REQ_CODE_SIMPLE) {
            TourListLab.get().setLentaPage(FIRST_PAGE);
        } else {
            TourSearchLab.get().setSearchPage(FIRST_PAGE);
        }
    }
}
