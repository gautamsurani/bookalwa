package com.booksalways.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.Global;
import utils.IApiMethods;
import utils.Utils;

public class OTPActivity extends AppCompatActivity {

    EditText etOne;
    Toolbar toolbar;
    String reg_otp = "", reg_phone = "", reg_userid = "", strOne = "";
    TextView tvResendOtp, txtVerifyOTP;
    ProgressDialog loading;
    RelativeLayout relativeMain;
    boolean IsGoToManul = true;
    private int OTP_TIME_OUT = 10000;
    ProgressDialog progressDialog;
    Global global;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp2);
        global = new Global(this);
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefBooksAlways", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg_otp = bundle.getString("reg_otp");
            reg_phone = bundle.getString("reg_phone");
            reg_userid = bundle.getString("reg_userid");
        }

        progressDialog = new ProgressDialog(OTPActivity.this);
        progressDialog.setMessage("Verifying mobile number...");
        progressDialog.show();
        progressDialog.setCancelable(true);


        initComponent();

        txtVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOne = etOne.getText().toString().trim();
                if (strOne.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else if (!strOne.equalsIgnoreCase(reg_otp)) {
                    Toast.makeText(getApplicationContext(), "OTP not match", Toast.LENGTH_SHORT).show();
                } else {

                    loading = ProgressDialog.show(OTPActivity.this, "", "Please wait...", false, false);
                    OTPAsynTask mOtpasync = new OTPAsynTask();
                    mOtpasync.execute();

                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IsGoToManul = false;
                progressDialog.dismiss();

            }
        }, OTP_TIME_OUT);
    }

    private void initComponent() {
        etOne = (EditText) findViewById(R.id.etOtp);
        txtVerifyOTP = (TextView) findViewById(R.id.txtVerifyOTP);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        tvResendOtp = (TextView) findViewById(R.id.txtResendOtp);
    }

    private class OTPAsynTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {

            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL).build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);

                Api_Model api_model = methods.GetOTPVerified("otp_verify", "verify", reg_phone, reg_userid, reg_otp);

                return api_model;
            } catch (Exception E) {
                E.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    try {
                        Api_Model.customer_detail_verify dataset = curators.customer_detail_verify;
                        global.setPrefBoolean("Verify", true);
                        editorLogin.putString("userID", dataset.ID);
                        editorLogin.putString("name", dataset.name);
                        editorLogin.putString("email", dataset.email);
                        editorLogin.putString("phone", dataset.phone);
                        editorLogin.putString("userimage", dataset.image);
                        editorLogin.commit();
                        Intent id = new Intent(OTPActivity.this, MainActivity.class);
                        startActivity(id);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();

                    } catch (Exception ignored) {
                    }

                } else {
                    Utils.ShowSnakBar(curators.message, relativeMain, OTPActivity.this);
                }
            }
        }
    }
}
