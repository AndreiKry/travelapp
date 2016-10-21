package com.travel.app.singleton;

import com.travel.app.model.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TourSearchLab {

    private static TourSearchLab sTourLab;
    private List<Tour> mTours;
    private int mPage = 1;

    public static TourSearchLab get() {
        if (sTourLab == null) {
            sTourLab = new TourSearchLab();
        }
        return sTourLab;
    }

    private TourSearchLab() {
        mTours = new ArrayList<>();
    }

    public void addTour(Tour tour) {
        mTours.add(tour);
    }

    public void addAllTour(List<Tour> nineTours) {
        mTours.addAll(nineTours);
    }

    public List<Tour> getTours() {
        return mTours;
    }

    public Tour getTour(UUID uuid) {
        for (Tour tour : mTours) {
            if (tour.getId().equals(uuid)) {
                return tour;
            }
        }
        return null;
    }

    public void clearSearchList() {
        mTours.clear();
    }

    public void setSearchPage(int page) {
        mPage = page;
    }

    public int getSearchPage() {
        return mPage;
    }
}
