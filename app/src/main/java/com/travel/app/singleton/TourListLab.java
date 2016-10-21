package com.travel.app.singleton;

import com.travel.app.model.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TourListLab {

    private static TourListLab sTourLab;
    private List<Tour> mTours;
    private int mPage = 1;

    public static TourListLab get() {
        if (sTourLab == null) {
            sTourLab = new TourListLab();
        }
        return sTourLab;
    }

    private TourListLab() {
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

    public void clearTourList() {
        mTours.clear();
    }

    public void setLentaPage(int page) {
        mPage = page;
    }

    public int getLentaPage() {
        return mPage;
    }
}
