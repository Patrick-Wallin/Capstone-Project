package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.firebasestorage.FirebaseStorageProcessor;

/**
 * Created by piwal on 7/3/2017.
 */

public class FetchRecordIntoTableTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private TextView mSplashLoadingTextView;
    private String mMessage;
    private Uri mUri;
    private ContentValues mContentValues;

    public FetchRecordIntoTableTask(Context context, Uri uri, ContentValues contentValues, String message, TextView splashLoadingTextView) {
        mContext = context;
        mSplashLoadingTextView = splashLoadingTextView;
        mMessage = message;
        mUri = uri;
        mContentValues = contentValues;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mContext.getContentResolver().insert(mUri, mContentValues);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mSplashLoadingTextView.setText(mMessage);
    }
}
