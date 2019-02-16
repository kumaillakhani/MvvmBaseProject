package com.example.mvvmbaseproject.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mvvmbaseproject.R;
import com.example.mvvmbaseproject.interfaces.MainActivityResultCallbacks;
import com.example.mvvmbaseproject.remote.RemoteRequest;
import com.example.mvvmbaseproject.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.mvvmbaseproject.remote.RemoteRequest.makeRequest;
import static com.example.mvvmbaseproject.remote.RemoteRequest.responseError;
import static com.example.mvvmbaseproject.utils.Common.AlertTitles.mError;
import static com.example.mvvmbaseproject.utils.Common.HandlerTimeout;
import static com.example.mvvmbaseproject.utils.Common.Messages.SomeThingWentWrong;
import static com.example.mvvmbaseproject.utils.Common.ShowErrorDialog;
import static com.example.mvvmbaseproject.utils.Common.countDownTimer;
import static com.example.mvvmbaseproject.utils.Common.countDownTimerFinished;
import static com.example.mvvmbaseproject.utils.Common.handler;
import static com.example.mvvmbaseproject.utils.Common.pd;
import static com.example.mvvmbaseproject.utils.Common.runnable;
import static com.example.mvvmbaseproject.utils.Common.startCountDown;

public class MainActivityViewModel extends ViewModel implements View.OnClickListener {

    private MainActivityResultCallbacks mainActivityResultCallbacks;
    private Button butnGet, butnSend;
    private TextView tvResponse;
    private double i = 0;

    public MainActivityViewModel(MainActivityResultCallbacks mainActivityResultCallbacks) {
        this.mainActivityResultCallbacks = mainActivityResultCallbacks;

        butnGet = (Button) ((Activity) Common.mContext).findViewById(R.id.butnGet);
        butnGet.setOnClickListener(this);
        butnSend = (Button) ((Activity) Common.mContext).findViewById(R.id.butnSend);
        butnSend.setOnClickListener(this);
        tvResponse = (TextView) ((Activity) Common.mContext).findViewById(R.id.tvResponse);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.butnGet:
                CallService();
                break;
            case R.id.butnSend:
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("cor", i+1);
                    CallService(jsonBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void CallService() {
        try {
            RemoteRequest.result = "";
            pd = new ProgressDialog(Common.mContext);
            pd.setTitle("Loading...");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            RemoteRequest.result = makeRequest(Common.mContext);
            ServerResponse();
        } catch (Exception e) {

        }
    }

    private void CallService(JSONObject jsonBody) {
        try {
            RemoteRequest.result = "";
            pd = new ProgressDialog(Common.mContext);
            pd.setTitle("Loading...");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            RemoteRequest.result = makeRequest(Common.mContext, jsonBody);
            ServerResponse();
        } catch (Exception e) {

        }
    }

    private void ServerResponse() {
        startCountDown();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
//                Log.d("Runnable","Handler is working");
                if(!RemoteRequest.result.equals("") || countDownTimerFinished){ // just remove call backs
                    handler.removeCallbacks(this);
                    countDownTimer.cancel();
                    countDownTimerFinished = true;
                    GetResponse(RemoteRequest.result);
                } else { // post again
                    handler.postDelayed(this, HandlerTimeout);
                }
            }
        };
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, HandlerTimeout);
    }

    private void GetResponse(String result) {
        Log.d("result >>> ", "\" "+result + " \"");
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.trim());

                if (jsonObject.has("Success")) {
                    if (pd != null) {
                        pd.dismiss();
                        if(jsonObject.optString("Cor").length() > 0) {
                            tvResponse.setText(jsonObject.optString("Cor"));
                        } else {
                            tvResponse.setText(jsonObject.optString("Success"));
                        }
                    }
                } else {
                    responseError = jsonObject.getJSONObject("GetLoginResult").opt("ErrorMessage").toString();
                    Log.d("Data", responseError);

                    if (pd != null) {
                        pd.dismiss();
                    }

                    mainActivityResultCallbacks.onError(mError);
                    ShowErrorDialog(mError, responseError, "OK", ((Activity)Common.mContext));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSONException 1:", e.toString());

                if (pd != null) {
                    pd.dismiss();
                }

                ShowErrorDialog(mError, SomeThingWentWrong, "OK", ((Activity)Common.mContext));
                mainActivityResultCallbacks.onError(mError);
            }

        }
        pd.dismiss();
    }
}
