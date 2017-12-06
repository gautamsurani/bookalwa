package com.booksalways.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/**
 * Created by welcome on 13-10-2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtUserName, edtMobile, edtPassword, edtEmail, etReferralCode;
    String StredtUserName = "", StredtMobile = "", StredtPassword = "", strEmail = "", strReferralCode = "";
    TextView txtConditionRed, txtCreateAccount, txtsignin;
    CheckBox checkTurmCondition;

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    ProgressDialog loading;
    RelativeLayout relativeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        FetchXmlID();
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefBooksAlways", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        txtCreateAccount.setOnClickListener(this);
        txtsignin.setOnClickListener(this);
        txtConditionRed.setOnClickListener(this);
    }

    private void FetchXmlID() {
        edtUserName = (EditText) findViewById(R.id.edtUserName);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        etReferralCode = (EditText) findViewById(R.id.etReferralCode);

        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtConditionRed = (TextView) findViewById(R.id.txtConditionRed);
        txtCreateAccount = (TextView) findViewById(R.id.txtCreateAccount);
        txtsignin = (TextView) findViewById(R.id.txtsignin);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        checkTurmCondition = (CheckBox) findViewById(R.id.checkTurmCondition);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtConditionRed:
                startActivity(new Intent(RegisterActivity.this, TermCondition.class));
                break;

            case R.id.txtCreateAccount:
                StredtUserName = edtUserName.getText().toString();
                StredtMobile = edtMobile.getText().toString();
                StredtPassword = edtPassword.getText().toString();
                strEmail = edtEmail.getText().toString();
                strReferralCode = etReferralCode.getText().toString();

                if (Utils.isNetworkAvailable(RegisterActivity.this)) {
                    if (TextUtils.isEmpty(StredtUserName)) {
                        Utils.ShowSnakBar("Please enter Name", relativeMain, RegisterActivity.this);
                    } else if (TextUtils.isEmpty(StredtMobile)) {
                        Utils.ShowSnakBar("Please enter Mobile", relativeMain, RegisterActivity.this);
                    } else if (StredtMobile.length() != 10) {
                        Utils.ShowSnakBar("Invalid Mobile", relativeMain, RegisterActivity.this);
                    } else if (TextUtils.isEmpty(StredtPassword)) {
                        Utils.ShowSnakBar("Please enter Password", relativeMain, RegisterActivity.this);
                    } else if (StredtPassword.length() < 4) {
                        Utils.ShowSnakBar("Password is short!", relativeMain, RegisterActivity.this);
                    } else if (!checkTurmCondition.isChecked()) {
                        Utils.ShowSnakBar("Select " + getResources().getString(R.string.tandc), relativeMain, RegisterActivity.this);
                    } else {
                        Utils.hideKeyboard(RegisterActivity.this);
                        RegisterAsynTask mRegisterAsynTask = new RegisterAsynTask();
                        mRegisterAsynTask.execute();
                    }
                }
                break;
            case R.id.txtsignin:
                Intent i = new Intent(RegisterActivity.this, Login.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class RegisterAsynTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(RegisterActivity.this, "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {

                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.GetSignUp("register", StredtUserName, strEmail, StredtPassword, StredtMobile,strReferralCode);

            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    try {
                        for (Api_Model.customer_detail dataset : curators.customer_detail) {

                            /*editorLogin.putString("userID", dataset.ID);
                            editorLogin.putString("name", dataset.name);
                            editorLogin.putString("email", dataset.email);
                            editorLogin.putString("phone", dataset.phone);
                            editorLogin.putString("userimage", dataset.image);
                            editorLogin.commit();*/

                            if (dataset.type.equalsIgnoreCase("OTP_SCREEN")) {
                                Toast.makeText(RegisterActivity.this, curators.message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                                intent.putExtra("reg_otp", dataset.otp);
                                intent.putExtra("reg_phone", dataset.phone);
                                intent.putExtra("reg_userid", dataset.ID);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            } else {
                                editorLogin.putString("userID", dataset.ID);
                                editorLogin.putString("name", dataset.name);
                                editorLogin.putString("email", dataset.email);
                                editorLogin.putString("phone", dataset.phone);
                                editorLogin.putString("userimage", dataset.image);
                                editorLogin.commit();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Register", "ActivityEx__" + e.getMessage());

                    }

                } else {
                    Utils.showToastShort(curators.message, RegisterActivity.this);
                }
            }
        }
    }
}

