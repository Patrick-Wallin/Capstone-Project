package com.patrickwallin.projects.collegeinformation;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 7/1/2017.
 */

public class ResultDetailActivityFragment extends Fragment implements OnDataSelectionChangeListener {
    @Nullable
    @BindView(R.id.college_image_view) ImageView mCollegeImageView;
    @Nullable
    @BindView(R.id.website_value_text_view) TextView mWebsiteTextView;
    @Nullable
    @BindView(R.id.location_value_text_view) TextView mLocationTextView;
    @Nullable
    @BindView(R.id.college_name_text_view) TextView mCollegeNameTextView;
    @Nullable
    @BindView(R.id.tuition_in_state_value_text_view) TextView mTuitionInStateTextView;
    @Nullable
    @BindView(R.id.tuition_out_state_value_text_view) TextView mTuitionOutStateTextView;
    @Nullable
    @BindView(R.id.aid_fed_loans_value_text_view) TextView mAidFederalLoansTextView;
    @Nullable
    @BindView(R.id.aid_pell_grants_value_text_view) TextView mAidPellGrantsTextView;
    @Nullable
    @BindView(R.id.admission_sat_reading_25_value_text_view) TextView mAdmissionSATReading25TextView;
    @Nullable
    @BindView(R.id.admission_sat_reading_75_value_text_view) TextView mAdmissionSATReading75TextView;
    @Nullable
    @BindView(R.id.admission_sat_math_25_value_text_view) TextView mAdmissionSATMath25TextView;
    @Nullable
    @BindView(R.id.admission_sat_math_75_value_text_view) TextView mAdmissionSATMath75TextView;
    @Nullable
    @BindView(R.id.admission_sat_writing_25_value_text_view) TextView mAdmissionSATWriting25TextView;
    @Nullable
    @BindView(R.id.admission_sat_writing_75_value_text_view) TextView mAdmissionSATWriting75TextView;
    @Nullable
    @BindView(R.id.admission_act_25_value_text_view) TextView mAdmissionACT25TextView;
    @Nullable
    @BindView(R.id.admission_act_75_value_text_view) TextView mAdmissionACT75TextView;
    @Nullable
    @BindView(R.id.likes_image_view) ImageView mLikesImageView;

    private FavoriteCollegeData mFavoriteCollegeData;
    private Context mContext;

    public ResultDetailActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getResources().getBoolean(R.bool.is_this_tablet)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Bundle bundleResult = getArguments();
        if(bundleResult != null) {
            mFavoriteCollegeData = Parcels.unwrap(bundleResult.getParcelable("resultdetailinfo"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_result_detail_fragment,container,false);

        ButterKnife.bind(this,rootView);

        DecimalFormat format = new DecimalFormat("0.00");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();


        //int color = Color.parseColor("#ffffff");
        //mLikesImageView.setColorFilter(color);

        if(mFavoriteCollegeData != null) {
            if (mFavoriteCollegeData.getImageLink() != null && !mFavoriteCollegeData.getImageLink().trim().isEmpty()) {
                Picasso.with(mContext)
                        .load(mFavoriteCollegeData.getImageLink())
                        .placeholder(R.drawable.no_image_available_building)
                        .into(mCollegeImageView);
            } else {
                Picasso.with(mContext)
                        .load(R.drawable.no_image_available_building)
                        .into(mCollegeImageView);
            }
            mCollegeNameTextView.setText(mFavoriteCollegeData.getName());
            mWebsiteTextView.setText(mFavoriteCollegeData.getWebsite());
            mLocationTextView.setText(mFavoriteCollegeData.getCity() + " " + mFavoriteCollegeData.getState());
            StringBuilder location = new StringBuilder();
            location.append(mFavoriteCollegeData.getLatitude());
            location.append(",");
            location.append(mFavoriteCollegeData.getLongitude());
            mLocationTextView.setTag(location.toString());

            mTuitionInStateTextView.setText(numberFormat.format(mFavoriteCollegeData.getCostInState()));
            mTuitionOutStateTextView.setText(numberFormat.format(mFavoriteCollegeData.getCostOutState()));
            mAidFederalLoansTextView.setText(String.format("%.2f", mFavoriteCollegeData.getPctFedLoans() * 100));
            mAidPellGrantsTextView.setText(String.format("%.2f", mFavoriteCollegeData.getPctPellGrants() * 100));

            mAdmissionSATReading25TextView.setText(String.format("%.0f", mFavoriteCollegeData.getSATCriticalReading25()));
            mAdmissionSATReading75TextView.setText(String.format("%.0f", mFavoriteCollegeData.getSATCriticalReading75()));

            mAdmissionSATMath25TextView.setText(String.format("%.0f", mFavoriteCollegeData.getSATMath25()));
            mAdmissionSATMath75TextView.setText(String.format("%.0f", mFavoriteCollegeData.getSATMath75()));

            mAdmissionSATWriting25TextView.setText(String.format("%.0f", mFavoriteCollegeData.getSATWriting25()));
            mAdmissionSATWriting75TextView.setText(String.format("%.0f", mFavoriteCollegeData.getSATWriting75()));

            mAdmissionACT25TextView.setText(String.format("%.0f", mFavoriteCollegeData.getACT25()));
            mAdmissionACT75TextView.setText(String.format("%.0f", mFavoriteCollegeData.getACT75()));

            mLikesImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mWebsiteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder fullAddress = new StringBuilder();
                    fullAddress.append(mWebsiteTextView.getText().toString());
                    if (!fullAddress.toString().toLowerCase().startsWith("http://") && !fullAddress.toString().toLowerCase().startsWith("https://")) {
                        fullAddress.setLength(0);
                        fullAddress.append("http://");
                        fullAddress.append(mWebsiteTextView.getText().toString());
                    }


                    Uri uri = Uri.parse(fullAddress.toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            mLocationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String location = v.getTag().toString();

                    Uri gmmIntentUri = Uri.parse("geo:" + location);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    try {
                        startActivity(mapIntent);
                    } catch (ActivityNotFoundException ex) {

                    }

                }
            });
        }
        return rootView;
    }


    @Override
    public void OnDataSelectionChanged(Bundle bundle) {
        if(bundle != null) {
            mFavoriteCollegeData = Parcels.unwrap(bundle.getParcelable("resultdetailinfo"));
        }
    }
}