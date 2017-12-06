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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.Global;
import utils.IApiMethods;
import utils.Utils;

import com.booksalways.shopping.R;
/**
 * Created by welcome on 13-10-2016.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    ImageView imgLogo;
    EditText edtEmil, edtPasssword;
    TextView txtForgotPassword, txtSignIn, txtNotMember, txtSignUp;
    String StredtEmil, StredtPasssword;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    RelativeLayout relativeMain;
    ProgressDialog loading;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    ProgressDialog progressDialog;
    String StrFbNane = "", STrFbEmail = "", StrID = "";
    Global global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(com.booksalways.shopping.R.layout.signin);
        getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        global = new Global(this);
        FetChXmlId();
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefBooksAlways", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        prefCartCounter = getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        loginButton = (LoginButton) findViewById(R.id.button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Utils.isNetworkAvailable(Login.this)) {
                    if (loginResult.getAccessToken() != null)
                    {
                        loginButton.setText("Sign in with Facebook");
                        GetFbData();
                    }
                } else {
                    Utils.ShowSnakBar("No Internet Connection..!!", relativeMain, Login.this);
                }
            }

            @Override
            public void onCancel() {
                //  Toast.makeText(LoginActivity.this,"Login has been cancelled..!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("HelloUsers", error.getMessage());
                Toast.makeText(Login.this, "Something wrong happen..!!", Toast.LENGTH_SHORT).show();
            }
        });


        txtForgotPassword.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        txtNotMember.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    public void GetFbData() {
        loginButton.setText("Sign in with Facebook");
//        progressDialog.show();
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                if (jsonObject != null) {
                    loginButton.setText("Sign in with Facebook");
                    try {
                        new LogInFaceBookAsynTask(jsonObject).execute("");
                    } catch (Exception e) {
                        Log.e("OK2", e.getMessage().toString());
                    }
                } else if (graphResponse.getError() != null) {
                    //   progressDialog.dismiss();
                    switch (graphResponse.getError().getCategory()) {
                        case LOGIN_RECOVERABLE:
                            break;
                        case TRANSIENT:
                            break;
                        case OTHER:
                            break;
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void FetChXmlId() {
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        edtEmil = (EditText) findViewById(R.id.edtEmil);
        edtPasssword = (EditText) findViewById(R.id.edtPasssword);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
        txtNotMember = (TextView) findViewById(R.id.txtNotMember);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtForgotPassword:
                Intent idd = new Intent(Login.this, ForgotActivity.class);
                startActivity(idd);
                overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left);

                break;


            case R.id.txtSignIn:
                StredtEmil = edtEmil.getText().toString();
                StredtPasssword = edtPasssword.getText().toString();

                if (Utils.isNetworkAvailable(Login.this)) {
                    if (TextUtils.isEmpty(StredtEmil)) {

                        Utils.ShowSnakBar("Please enter Email / Name", relativeMain, Login.this);
                    } else if (TextUtils.isEmpty(StredtPasssword)) {

                        Utils.ShowSnakBar("Please enter Password", relativeMain, Login.this);
                    } else {
                        Utils.hideKeyboard(Login.this);
                        LogInAsynTask mLogInAsynTask = new LogInAsynTask();
                        mLogInAsynTask.execute();
                    }

                } else {
                }
                break;


            case R.id.txtNotMember:
            case R.id.txtSignUp:

                Intent i = new Intent(Login.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    private class LogInFaceBookAsynTask extends AsyncTask<String, Void, Api_Model> {
        RestAdapter restAdapter;
        public JSONObject objUser;

        public LogInFaceBookAsynTask(JSONObject jsonObject) {

            objUser = jsonObject;
            Log.e("objUser", objUser.toString());
        }

        @Override
        protected void onPreExecute() {
            try {
                StrFbNane = objUser.getString("name");
                STrFbEmail = objUser.getString("email");
                StrID = objUser.getString("id");
                loading = ProgressDialog.show(Login.this, "", "Please wait...", false, false);
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(AppConstant.API_URL)
                        .build();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage().toString());
            }
        }

        @Override
        protected Api_Model doInBackground(String... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.GetSocialLogin("login", "social", STrFbEmail, StrID);

                return curators;
            } catch (Exception E) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();

            //  Log.e("  AAsss  e",curators.msgcode.toString()+" ");
            if (curators == null) {

            } else {
                Log.e("curators.message", "curators.message" + curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    editorCartCounter.putString("CartCounter", curators.cart_count);
                    editorCartCounter.commit();
                    try {
                        for (Api_Model.customer_detail dataset : curators.customer_detail) {
                            editorLogin.putString("userID", dataset.ID);
                            editorLogin.putString("name", dataset.name);
                            editorLogin.putString("email", dataset.email);
                            editorLogin.putString("phone", dataset.phone);
                            editorLogin.putString("userimage", dataset.image);
                            editorLogin.commit();
                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } catch (Exception e) {
                        Log.e("Fb", "LoginEx__" + e.getMessage());
                    }
                } else {
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, Login.this);
                    loginButton.setText("Sign in with Facebook");
                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(Login.this, RegisterActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        }
    }

    private class LogInAsynTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(Login.this, "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL).build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.GetLogin("login", "signin", StredtEmil, StredtPasssword);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.toString().equals("0")) {
                    editorCartCounter.putString("CartCounter", curators.cart_count);
                    editorCartCounter.commit();
                    try {
                        for (Api_Model.customer_detail dataset : curators.customer_detail) {

                            if (dataset.type.equalsIgnoreCase("OTP_SCREEN")) {
                                edtPasssword.setText("");
                                //   Toast.makeText(LoginActivity.this, dataset.otp, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, OTPActivity.class);
                                intent.putExtra("reg_otp", dataset.otp);
                                intent.putExtra("reg_phone", dataset.phone);
                                intent.putExtra("reg_userid", dataset.ID);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                global.setPrefBoolean("Verify", true);
                                editorLogin.putString("userID", dataset.ID);
                                editorLogin.putString("name", dataset.name);
                                editorLogin.putString("email", dataset.email);
                                editorLogin.putString("phone", dataset.phone);
                                editorLogin.putString("userimage", dataset.image);
                                editorLogin.commit();
                                Intent mainIntent = new Intent(Login.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }


                        }
                    } catch (Exception ignored) {
                    }
                } else {
                    Utils.ShowSnakBar(curators.message, relativeMain, Login.this);
                }
            }
        }
    }
}
