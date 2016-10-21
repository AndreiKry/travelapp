package com.travel.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.travel.app.fragment.AppAboutFragment;
import com.travel.app.helper.FormSender;
import com.travel.app.R;
import com.travel.app.fragment.EmptyViewFragment;
import com.travel.app.fragment.AgencyAboutFragment;
import com.travel.app.fragment.AgencyAdvantagesFragment;
import com.travel.app.fragment.AgencyContactsFragment;
import com.travel.app.fragment.TourManagerFragment;
import com.travel.app.fragment.TourListFragment;
import com.travel.app.fragment.TourSearchFragment;
import com.travel.app.fragment.SendFormFragment;
import com.travel.app.fragment.TourSingleFragment;
import com.travel.app.model.Tour;

public class MainActivity
        extends AppCompatActivity
        implements SendFormFragment.NoticeDialogListener,
        TourListFragment.RecyclerTourCallbacks,
        TourSearchFragment.SearchTourCallbacks {

    private static final int REQ_CODE_SIMPLE = 0;
    private static final int REQ_CODE_SEARCH = 1;
    /*private static final int REQ_CODE_AGENT = 1;*/
    private static final int SEARCH_MAIN = 0;
    private static final int SEARCH_DETAIL = 1;
    private static final String FRAGMENT_TAG_LENTA = "lenta";
    private static final String FRAGMENT_TAG_SEARCH = "search";
    private static final String FRAGMENT_TAG_AGENT = "agent";
    private static final String FRAGMENT_TAG_ABOUT = "about";
    private static final String FRAGMENT_TAG_ADVANTAGES = "advantages";
    private static final String FRAGMENT_TAG_CONTACTS = "contacts";
    private static final String FRAGMENT_TAG_EMPTY = "empty";
    private static final String FRAGMENT_TAG_APP = "app";

    private AccountHeader mHeader = null;
    private Drawer mDrawer = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_masterdetail);

        if (isDetailed() == null) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //setRequestedOrientation(Scr);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_list);
        setSupportActionBar(toolbar);

        mHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        if (isDetailed() == null) {
            mDrawer = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(mHeader)
                    .withHasStableIds(true)
                    .withItemAnimator(new AlphaCrossFadeAnimator())
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(R.string.main_list).withIdentifier(1).withIcon(MaterialDesignIconic.Icon.gmi_collection_image),
                            new PrimaryDrawerItem().withName(R.string.main_search).withIdentifier(2).withIcon(MaterialDesignIconic.Icon.gmi_search),
                            new PrimaryDrawerItem().withName(R.string.main_manager).withIdentifier(3).withIcon(MaterialDesignIconic.Icon.gmi_male_female),
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().withName(R.string.main_about).withIdentifier(4).withIcon(MaterialDesignIconic.Icon.gmi_label_heart),
                            new PrimaryDrawerItem().withName(R.string.main_advantages).withIdentifier(5).withIcon(MaterialDesignIconic.Icon.gmi_badge_check),
                            new PrimaryDrawerItem().withName(R.string.main_contacts).withIdentifier(6).withIcon(MaterialDesignIconic.Icon.gmi_account_box_mail),
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().withName(R.string.main_rate_app).withIdentifier(77).withIcon(MaterialDesignIconic.Icon.gmi_thumb_up),
                            new PrimaryDrawerItem().withName(R.string.main_about_app).withIdentifier(69).withIcon(MaterialDesignIconic.Icon.gmi_smartphone_info)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem != null) {
                                int itemId = (int) drawerItem.getIdentifier();

                                switch (itemId) {
                                    case 1:
                                        toolbar.setTitle(R.string.main_list);
                                        backstackFragment(TourListFragment.newInstance(REQ_CODE_SIMPLE), FRAGMENT_TAG_LENTA);
                                        break;
                                    case 2:
                                        toolbar.setTitle(R.string.main_search);
                                        replaceFragment(TourSearchFragment.newInstance(SEARCH_MAIN), FRAGMENT_TAG_SEARCH);
                                        break;
                                    case 3:
                                        toolbar.setTitle(R.string.main_manager);
                                        replaceFragment(new TourManagerFragment(), FRAGMENT_TAG_AGENT);
                                        break;
                                    case 4:
                                        toolbar.setTitle(R.string.main_about);
                                        replaceFragment(new AgencyAboutFragment(), FRAGMENT_TAG_ABOUT);
                                        break;
                                    case 5:
                                        toolbar.setTitle(R.string.main_advantages);
                                        replaceFragment(new AgencyAdvantagesFragment(), FRAGMENT_TAG_ADVANTAGES);
                                        break;
                                    case 6:
                                        toolbar.setTitle(R.string.main_contacts);
                                        replaceFragment(new AgencyContactsFragment(), FRAGMENT_TAG_CONTACTS);
                                        break;
                                    case 69:
                                        toolbar.setTitle(R.string.main_about_app);
                                        replaceFragment(new AppAboutFragment(), FRAGMENT_TAG_APP);
                                        break;
                                    case 77:
                                        // Rate App
                                        try {
                                            Intent int_rate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                                            int_rate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(int_rate);
                                        } catch (Exception e) {
                                            Log.e("navdrawer", "onItemClick: ", e);
                                            mDrawer.setSelection(1);
                                        }
                                        break;
                                    default:
                                        replaceFragment(TourListFragment.newInstance(REQ_CODE_SIMPLE), FRAGMENT_TAG_LENTA);
                                        break;
                                }
                            }
                            return false;
                        }

                        private void replaceFragment(Fragment mainFragment, String mainTag) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .replace(R.id.fragment_container, mainFragment, mainTag)
                                    .commit();
                        }

                        private void backstackFragment(Fragment mainFragment, String mainTag) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Fragment fragment = fragmentManager.findFragmentByTag(mainTag);
                            if (fragment == null) {
                                fragmentManager
                                        .beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.fragment_container, mainFragment, mainTag)
                                        .addToBackStack(mainTag)
                                        .commit();
                            } else {
                                fragmentManager
                                        .beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.fragment_container, fragment, mainTag)
                                        .commit();
                            }
                        }
                    })
                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {

                        }

                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                            InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                        }
                    })
                    .withSavedInstance(savedInstanceState)
                    .build();
        } else {
            mDrawer = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(mHeader)
                    .withHasStableIds(true)
                    .withItemAnimator(new AlphaCrossFadeAnimator())
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(R.string.main_list).withIdentifier(1).withIcon(MaterialDesignIconic.Icon.gmi_collection_image),
                            new PrimaryDrawerItem().withName(R.string.main_search).withIdentifier(2).withIcon(MaterialDesignIconic.Icon.gmi_search),
                            new PrimaryDrawerItem().withName(R.string.main_manager_contacts).withIdentifier(3).withIcon(MaterialDesignIconic.Icon.gmi_account_box_mail),
                            new PrimaryDrawerItem().withName(R.string.main_about_advantages).withIdentifier(4).withIcon(MaterialDesignIconic.Icon.gmi_label_heart),
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().withName(R.string.main_rate_app).withIdentifier(77).withIcon(MaterialDesignIconic.Icon.gmi_thumb_up),
                            new PrimaryDrawerItem().withName(R.string.main_about_app).withIdentifier(69).withIcon(MaterialDesignIconic.Icon.gmi_smartphone_info)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem != null) {
                                int itemId = (int) drawerItem.getIdentifier();

                                switch (itemId) {
                                    case 1:
                                        toolbar.setTitle(R.string.main_list);
                                        backstackFragment(TourListFragment.newInstance(REQ_CODE_SIMPLE), FRAGMENT_TAG_LENTA, new EmptyViewFragment(), FRAGMENT_TAG_EMPTY);
                                        break;
                                    case 2:
                                        toolbar.setTitle(R.string.main_search);
                                        backstackFragment(TourSearchFragment.newInstance(SEARCH_DETAIL), FRAGMENT_TAG_SEARCH, new EmptyViewFragment(), FRAGMENT_TAG_EMPTY);
                                        break;
                                    case 3:
                                        toolbar.setTitle(R.string.main_manager_contacts);
                                        replaceFragment(new TourManagerFragment(), FRAGMENT_TAG_AGENT, new AgencyContactsFragment(), FRAGMENT_TAG_CONTACTS);
                                        break;
                                    case 4:
                                        toolbar.setTitle(R.string.main_about_advantages);
                                        replaceFragment(new AgencyAboutFragment(), FRAGMENT_TAG_ABOUT, new AgencyAdvantagesFragment(), FRAGMENT_TAG_ADVANTAGES);
                                        break;
                                    case 69:
                                        toolbar.setTitle(R.string.main_about_app);
                                        replaceFragment(new EmptyViewFragment(), FRAGMENT_TAG_EMPTY, new AppAboutFragment(), FRAGMENT_TAG_APP);
                                        break;
                                    case 77:
                                        // Rate App
                                        try {
                                            Intent int_rate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                                            int_rate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(int_rate);
                                        } catch (Exception e) {
                                            Log.e("navdrawer", "onItemClick: ", e);
                                            mDrawer.setSelection(69);
                                        }
                                        break;
                                    default:
                                        replaceFragment(TourListFragment.newInstance(REQ_CODE_SIMPLE), FRAGMENT_TAG_LENTA, new EmptyViewFragment(), FRAGMENT_TAG_EMPTY);
                                        break;
                                }
                            }
                            return false;
                        }

                        private void replaceFragment(Fragment mainFragment, String mainTag, Fragment detailFragment, String detailTag) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    //.setCustomAnimations(new AlphaCrossFadeAnimator(), new)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .replace(R.id.fragment_container, mainFragment, mainTag)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .replace(R.id.detail_fragment_container, detailFragment, detailTag)
                                    .commit();
                        }

                        private void backstackFragment(Fragment mainFragment, String mainTag, Fragment detailFragment, String detailTag) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Fragment fragment = fragmentManager.findFragmentByTag(mainTag);
                            if (fragment == null) {
                                fragmentManager
                                        .beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.fragment_container, mainFragment, mainTag)
                                        .addToBackStack(mainTag)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.detail_fragment_container, detailFragment, detailTag)
                                        .commit();
                            } else {
                                fragmentManager
                                        .beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.fragment_container, fragment, mainTag)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.detail_fragment_container, detailFragment, detailTag)
                                        .commit();
                            }
                        }
                    })
                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {

                        }

                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                            InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                        }
                    })
                    .withSavedInstance(savedInstanceState)
                    .build();
        }

        if (savedInstanceState == null) {
            mDrawer.setSelection(1);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mHeader.saveInstanceState(outState);
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
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
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTourClick(Tour tour, int requestCode) {
        if (isDetailed() == null) {
            Intent intent = SingleTourActivity.newIntent(MainActivity.this, requestCode, tour.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = TourSingleFragment.newInstance(requestCode, tour.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onSearchClick() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, TourListFragment.newInstance(REQ_CODE_SEARCH), FRAGMENT_TAG_LENTA)
                .replace(R.id.detail_fragment_container, new EmptyViewFragment(), FRAGMENT_TAG_EMPTY)
                .commit();
    }

    @Override
    public void onEmptyResult(Activity activity) {
        if (isDetailed() == null) {
            NavUtils.navigateUpFromSameTask(activity);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_SEARCH);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, FRAGMENT_TAG_SEARCH)
                    .commit();
        }
    }

    public View isDetailed() {
        return findViewById(R.id.detail_fragment_container);
    }
}
