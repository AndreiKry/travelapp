package com.travel.app.singleton;

import com.travel.app.model.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 16.08.2016.
 */
public class OptionLab {

    private static OptionLab sOptionLab;
    private List<Option> mOptions;

    public static OptionLab get() {
        if (sOptionLab == null) {
            sOptionLab = new OptionLab();
        }

        return sOptionLab;
    }

    private OptionLab() {
        mOptions = new ArrayList<>();
    }

    public void addOption(Option option) {
        mOptions.add(option);
    }

    public List<Option> getOptions() {
        return mOptions;
    }

    public void updateOption(Option option) {
        UUID uuid = option.getId();

        for (Option opt : mOptions) {
            if (opt.getId().equals(uuid)) {
                opt.setChecked(!opt.isChecked());
            }
        }
    }
}
