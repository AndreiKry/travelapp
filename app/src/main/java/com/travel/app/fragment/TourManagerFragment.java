package com.travel.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.travel.app.helper.InternetConnection;
import com.travel.app.helper.SampleData;
import com.travel.app.model.Option;
import com.travel.app.R;
import com.travel.app.singleton.OptionLab;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TourManagerFragment extends Fragment {

    private static final String DIALOG_FORM = "DialogForm";
    private static final int DIALOG_CONTACTS = 1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 2;

    private SwipeRefreshLayout mManagerSwipeRefreshLayout;
    private ManagerAdapter mManagerAdapter;
    private RecyclerView mManagerRecyclerView;
    private LinearLayout mManagerLinearLayout;
    private Button mReconnectButton;

    /*private int mCountTotal;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_manager, container, false);

        mManagerSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.agent_swipe_refresh_layout);
        mManagerSwipeRefreshLayout.setEnabled(false);
        mManagerSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.primary),
                ContextCompat.getColor(getActivity(), R.color.accent)
        );
        mManagerRecyclerView = (RecyclerView) view.findViewById(R.id.agent_recycler_view);
        mManagerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mManagerRecyclerView.setAdapter(mManagerAdapter);
        mManagerRecyclerView.setItemAnimator(new SlideInUpAnimator());

        mManagerLinearLayout = (LinearLayout) view.findViewById(R.id.agent_error_layout);
        mReconnectButton = (Button) view.findViewById(R.id.agent_error_connection);
        mReconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();
            }
        });

        fetchData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        mManagerSwipeRefreshLayout.setRefreshing(false);
    }

    private void updateUI() {
        OptionLab optionLab = OptionLab.get();
        List<Option> optionList = optionLab.getOptions();

        mManagerAdapter = new ManagerAdapter(optionList);
    }

    private void fetchData() {
        OptionLab optionLab = OptionLab.get();
        List<Option> optionList = optionLab.getOptions();

        if (optionList.size() == 0) {
            if (InternetConnection.isConnected(getContext())) {
                mManagerLinearLayout.setVisibility(View.GONE);
                /*String manager_url = getString(R.string.manager_url);
                getManagerParseResult(mManagerAdapter, manager_url);*/
                fillData(mManagerAdapter);
            } else {
                mManagerLinearLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void fillData(final ManagerAdapter managerAdapter) {
        mManagerSwipeRefreshLayout.setRefreshing(true);
        SampleData.generateOptions();
        SampleData.generateOptions();
        managerAdapter.notifyItemRangeInserted(0, 17);
        mManagerSwipeRefreshLayout.setRefreshing(false);
    }

    /*public void getManagerParseResult(final TourManagerFragment.ManagerAdapter agentAdapter, String fromUrl) {

        mManagerSwipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                fromUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        mCountTotal = response.getInt("count_total");
                        JSONArray optionArray = response.getJSONArray("option");
                        for (int i = 0; i < optionArray.length(); i++) {
                            Option option = new Option();
                            JSONObject optionObject = postsArray.getJSONObject(i);
                            //parse optionObject
                            OptionLab.get().addOption(option);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                agentAdapter.notifyItemRangeInserted(0, mCountTotal);
                mManagerSwipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Adding request to volley request queue
        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonReq);
    }*/

    private class ManagerHolder extends RecyclerView.ViewHolder {

        private Option mOption;
        private SwitchCompat mSwitchCompat;

        public ManagerHolder(View itemView) {
            super(itemView);

            mSwitchCompat = (SwitchCompat) itemView.findViewById(R.id.agent_item_switch);
            mSwitchCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OptionLab.get().updateOption(mOption);
                }
            });
        }

        public void bindItem(Option option) {
            mOption = option;
            mSwitchCompat.setText(mOption.getTitle());
            mSwitchCompat.setChecked(mOption.isChecked());
        }
    }

    public class ManagerAdapter extends RecyclerView.Adapter<ManagerHolder> {

        private List<Option> mOptionList;

        public ManagerAdapter(List<Option> optionList) {
            mOptionList = optionList;
        }

        @Override
        public ManagerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == TYPE_HEADER){
                View view = layoutInflater.inflate(R.layout.item_manager_header, parent, false);
                return new ManagerHolder(view);
            } else if (viewType == TYPE_ITEM) {
                View view = layoutInflater.inflate(R.layout.item_manager_recycler, parent, false);
                return new ManagerHolder(view);
            } else if (viewType == TYPE_FOOTER){
                View view = layoutInflater.inflate(R.layout.item_manager_footer, parent, false);
                Button sendButton = (Button) view.findViewById(R.id.agent_item_button);
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = getFragmentManager();
                        SendFormFragment dialog = SendFormFragment.newInstance(DIALOG_CONTACTS);
                        dialog.show(fragmentManager, DIALOG_FORM);
                    }
                });
                return new ManagerHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(ManagerHolder holder, int position) {
            Option option = mOptionList.get(position);
            holder.bindItem(option);
        }

        @Override
        public int getItemCount() {
            return mOptionList.size() ;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader (position)) {
                return TYPE_HEADER;
            } else if(isPositionFooter (position)) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }

        private boolean isPositionFooter(int position) {

            return position == mOptionList.size() - 1;
        }

        private boolean isPositionHeader (int position) {
            return position == 0;
        }
    }

}
