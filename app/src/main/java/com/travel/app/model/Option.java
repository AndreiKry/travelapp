package com.travel.app.model;

import java.util.UUID;

/**
 * Created by user on 10.09.2016.
 */
public class Option {

    private UUID mId;
    private String mTitle;
    private boolean mChecked;

    public Option() {
        mId = UUID.randomUUID();
        mChecked = false;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
