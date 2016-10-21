package com.travel.app.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.travel.app.model.Option;
import com.travel.app.R;
import com.travel.app.singleton.OptionLab;
import com.travel.app.singleton.TourListLab;
import com.travel.app.singleton.TourSearchLab;
import com.travel.app.model.Tour;

import java.util.List;
import java.util.UUID;

/**
 * Created by user on 21.08.2016.
 */
public class SendFormFragment extends DialogFragment {

    private static final String ARG_VERSION_CODE = "version_code";
    private static final String ARG_TOUR_BASE = "tour_base";
    private static final String ARG_TOUR_UUID = "tour_uuid";
    private static final String TAG = "SendFormDialog";
    private static final int REQ_CODE_TOUR = 0;
    private static final int REQ_CODE_AGENT = 1;
    private static final int REQ_CODE_CONTACT = 2;
    private static final int REQ_CODE_SIMPLE = 0;
    private static final int REQ_CODE_SEARCH = 1;

    private AlertDialog mAlertDialog;
    private EditText mNameEditText, mEmailEditText, mPhoneEditText, mCommentEditText;

    private String mName, mEmail, mPhone, mComment, mTotalComment;
    private int mVersionCode;
    private int mTourBase;
    private Tour mTour;

    public interface NoticeDialogListener {
        void onDialogPositiveClick(String name, String email, String phone, String comment, int version);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;

    public static SendFormFragment newInstance(int versionCode) {
        Bundle args = new Bundle();
        args.putInt(ARG_VERSION_CODE, versionCode);

        SendFormFragment fragment = new SendFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SendFormFragment newInstance(int versionCode, int tourBase, UUID uuid) {
        Bundle args = new Bundle();
        args.putInt(ARG_VERSION_CODE, versionCode);
        args.putInt(ARG_TOUR_BASE, tourBase);
        args.putSerializable(ARG_TOUR_UUID, uuid);

        SendFormFragment fragment = new SendFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("sendForm must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_send_form, null);

        mVersionCode = (int) getArguments().getSerializable(ARG_VERSION_CODE);
        if (mVersionCode == REQ_CODE_TOUR) {
            mTourBase = (int) getArguments().getSerializable(ARG_TOUR_BASE);
            UUID mUuid = (UUID) getArguments().getSerializable(ARG_TOUR_UUID);
            if (mTourBase == REQ_CODE_SIMPLE) {
                mTour = TourListLab.get().getTour(mUuid);
            } else {
                mTour = TourSearchLab.get().getTour(mUuid);
            }
        }

        mNameEditText = (EditText) view.findViewById(R.id.form_dialog_fio);
        mEmailEditText = (EditText) view.findViewById(R.id.form_dialog_email);
        mPhoneEditText = (EditText) view.findViewById(R.id.form_dialog_phone);
        mCommentEditText = (EditText) view.findViewById(R.id.form_dialog_comm);

        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.form_dialog_section_form)
                .setNegativeButton(R.string.form_dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogNegativeClick(SendFormFragment.this);
                    }
                })
                .setPositiveButton(R.string.form_dialog_button_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mComment.length() == 0) {
                            mComment = getString(R.string.form_send_no_comment);
                        }
                        if (mVersionCode == REQ_CODE_TOUR) {
                            mTotalComment = getString(R.string.form_send_tour, mTour.getTitle(), mTour.getHotel(), mTour.getDate(), mComment);
                        } else if (mVersionCode == REQ_CODE_AGENT){
                            List<Option> options = OptionLab.get().getOptions();
                            List<String> checkedOpts = null;
                            String optString;
                            for (Option option : options) {
                                if (option.isChecked()) {
                                    checkedOpts.add(option.getTitle());
                                }
                            }
                            assert checkedOpts != null;
                            if (checkedOpts.isEmpty()) {
                                optString = getString(R.string.form_send_no_options);
                            } else  {
                                optString = checkedOpts.toString();
                            }
                            mTotalComment = getString(R.string.form_send_agent, optString, mComment);
                        } else /*if (mVersionCode == REQ_CODE_CONTACT)*/ {
                            mTotalComment = getString(R.string.form_send_contact, mComment);
                        }
                        mListener.onDialogPositiveClick(mName, mEmail, mPhone, mTotalComment, mVersionCode);
                    }
                })
                .create();

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSend();
                mName = editable.toString();
            }
        });

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(editable).matches()) {
                    mEmailEditText.setError(getString(R.string.form_dialog_email_error));
                } else {
                    checkSend();
                    mEmail = editable.toString();
                }
            }
        });

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Patterns.PHONE.matcher(editable).matches()) {
                    mPhoneEditText.setError(getString(R.string.form_dialog_phone_error));
                } else {
                    checkSend();
                    mPhone = editable.toString();
                }
            }
        });

        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mComment = editable.toString();
            }
        });

        return mAlertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
    }

    public void checkSend() {
        if (mNameEditText.getText().length() != 0) {
            if (mEmailEditText.getText().length() != 0) {
                if (mPhoneEditText.getText().length() != 0) {
                    mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        }
    }

}
