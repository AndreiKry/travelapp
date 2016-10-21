package com.travel.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travel.app.R;
import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by user on 31.08.2016.
 */

public class AgencyContactsFragment extends Fragment {

    private static final String DIALOG_FORM = "DialogForm";
    private static final int DIALOG_CONTACTS = 2;

    private RecyclerView mContactsRecyclerView;
    private TextView mEmailTextView, mPhoneTextView, mAddressTextView;
    private GoogleMap mMap;
    private MapView mMapView;
    private Marker mMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agency_contacts, container, false);

        mContactsRecyclerView = (RecyclerView) view.findViewById(R.id.contacts_recycler_view);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ContactsAdapter contactsAdapter = new ContactsAdapter();
        mContactsRecyclerView.setAdapter(contactsAdapter);
        mContactsRecyclerView.setItemAnimator(new FadeInAnimator());

        mEmailTextView = (TextView) view.findViewById(R.id.contacts_email);
        mPhoneTextView = (TextView) view.findViewById(R.id.contacts_phone);
        mAddressTextView = (TextView) view.findViewById(R.id.contacts_address);
        mMapView = (MapView) view.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                UiSettings uiSettings = mMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setZoomGesturesEnabled(false);

                LatLng travelApp = new LatLng(-33.8854455, 151.2154462);
                mMarker = mMap.addMarker(new MarkerOptions()
                        .position(travelApp)
                        .title(getString(R.string.agency_contacts_marker_title))
                );

                CameraPosition cameraPosition = new CameraPosition
                        .Builder()
                        .target(travelApp)
                        .zoom(16)
                        .bearing(360)
                        .tilt(0)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);

                mMarker.showInfoWindow();
            }
        });
        mAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMarker.isInfoWindowShown()) {
                    mMarker.hideInfoWindow();
                } else {
                    mMarker.showInfoWindow();
                }
            }
        });

        mEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                SendFormFragment dialog = SendFormFragment.newInstance(DIALOG_CONTACTS);
                dialog.show(fragmentManager, DIALOG_FORM);
            }
        });
        mPhoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+9(999)9999999"));
                startActivity(callIntent);
            }
        });

        ScrollView mScrollView = (ScrollView) view.findViewById(R.id.contacts_scroll_view);
        mScrollView.requestFocus();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private Button mCallButton, mVKButton;

        public ContactHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.contacts_name_text_view);
            mCallButton = (Button) itemView.findViewById(R.id.contacts_call_button);
            mVKButton = (Button) itemView.findViewById(R.id.contacts_vk_button);
        }

        public void bindContact(String name){
            mTextView.setText(name);
        }
    }

    public class ContactsAdapter extends SimpleSectionedAdapter<ContactHolder> {

        protected String[][] contacts = {{"John Doe", "John Doe"},
                {"John Doe", "John Doe", "John Doe"}};
        protected String[][] phones = {{"+99999999999", "+99999999999"},
                {"+99999999999", "+99999999999", "+99999999999"}};
        // или другие соц сети
        protected String[][] vkLinks = {{"https://vk.com/1", "https://vk.com/1"},
                {"https://vk.com/1", "https://vk.com/1", "https://vk.com/1"}};

        @Override
        protected String getSectionHeaderTitle(int section) {
            return section == 0 ? "Directors" : "Managers";
        }

        @Override
        protected int getSectionCount() {
            return 2;
        }

        @Override
        protected int getItemCountForSection(int section) {
            return section == 0 ? 2 : 3;
        }

        @Override
        protected ContactHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_contact_recycler, parent, false);
            return new ContactHolder(view);
        }

        @Override
        protected void onBindItemViewHolder(ContactHolder holder, final int section, final int position) {
            holder.bindContact(contacts[section][position]);
            holder.mCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionCall(phones[section][position]);
                }
            });
            if ((section == 1) && (position == 1)) {
                holder.mVKButton.setVisibility(View.INVISIBLE);
            } else {
                holder.mVKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        actionLink(vkLinks[section][position]);
                    }
                });
            }
        }

        private void actionCall(String tel) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+tel));
            startActivity(callIntent);
        }

        private void actionLink(String link) {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(link));
            startActivity(sendIntent);
        }
    }
}
