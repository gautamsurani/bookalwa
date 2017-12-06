package com.booksalways.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.WalletHistoryAdapter;
import models.WalletModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;


public class WalletHistory extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    Dialog dialog;
    SharedPreferences prefSuper;
    SharedPreferences.Editor editorSuperInfo;
    ProgressDialog progressDialog;
    ArrayList<WalletModel> walletlist = new ArrayList<WalletModel>();
    LinearLayoutManager mLayoutManager;
    RecyclerView rvWalletlist;
    ProgressBar progressBar1;
    int pagecode = 0;
    boolean IsLAstLoading = true;
    LinearLayout relativeMain;
    ProgressDialog loading;
    String strUserId = "", strMobile = "";
    TextView tvAddmoney, tvAvailablebal, tvAvailablebalsuper, Superwallet;
    String strWalletBalance = "";
    String strSuperWalletBalance = "";
    String activityType = "";
    String strSuperMsg = "";
    private WalletHistoryAdapter walletHistoryAdapter;
    private LinearLayout lyt_not_found;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences prefDecorentwallet;
    SharedPreferences.Editor editorBooksAlwayswallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallethistory);
        context = this;

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefBooksAlways", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        strUserId = prefLogin.getString("userID", null);
        strMobile = prefLogin.getString("phone", null);
        prefSuper = getApplicationContext().getSharedPreferences("DataSuperInfo", 0); // 0 - for private mode
        editorSuperInfo = prefSuper.edit();
        strSuperMsg = prefSuper.getString("SuperInfoMsg", null);
        Log.e("DataSuperInfo", "DataSuperInfo" + strSuperMsg);
        prefDecorentwallet = getApplicationContext().getSharedPreferences("WalletPrefBooksAlways", 0); // 0 - for private mode
        editorBooksAlwayswallet = prefDecorentwallet.edit();

        editorBooksAlwayswallet.putString("BooksAlwaysWallet", strWalletBalance);
        editorBooksAlwayswallet.commit();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activityType = bundle.getString("activityType");
        }
        initToolbar();
        initComponent();

        if (Utils.isNetworkAvailable(WalletHistory.this)) {
            loading = ProgressDialog.show(WalletHistory.this, "", getResources().getString(R.string.string_plzwait), false, false);
            GetWalletHistoryDetails getMyWalletHistory = new GetWalletHistoryDetails();
            getMyWalletHistory.execute();

        } else {
            retryInternet();
        }

        mLayoutManager = new LinearLayoutManager(context);
        rvWalletlist.setLayoutManager(mLayoutManager);
        rvWalletlist.setHasFixedSize(true);
        walletHistoryAdapter = new WalletHistoryAdapter(WalletHistory.this, WalletHistory.this, walletlist);
        rvWalletlist.setAdapter(walletHistoryAdapter);
        Superwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(WalletHistory.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialog.setContentView(R.layout.supermony_info_dialog);
                ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
                TextView Cashbackmsg = (TextView) dialog.findViewById(R.id.Cashbackmsg);
                Cashbackmsg.setText(strSuperMsg);
                Log.e("Super", "belence" + strSuperWalletBalance);
                TextView user_super_walletTv = (TextView) dialog.findViewById(R.id.user_super_walletTv);
                if (strSuperWalletBalance.equalsIgnoreCase("0.00")) {
                    user_super_walletTv.setText(getResources().getString(R.string.Rs) + " " + "0.00");
                } else if (strSuperWalletBalance.equalsIgnoreCase("")) {
                    user_super_walletTv.setText(getResources().getString(R.string.Rs) + " " + "0.00");
                } else {
                    user_super_walletTv.setText(getResources().getString(R.string.rs) + " " + strSuperWalletBalance);
                }

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        rvWalletlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (IsLAstLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight()) {
                            IsLAstLoading = false;
                            progressBar1.setVisibility(View.VISIBLE);
                            pagecode++;

                            GetWalletHistoryDetails getMyWalletHistory = new GetWalletHistoryDetails();
                            getMyWalletHistory.execute();

                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });

        tvAddmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletlist.clear();
                pagecode = 0;
                Intent intentWallet = new Intent(WalletHistory.this, WalletActivity.class);
                intentWallet.putExtra("WalletBalance",strWalletBalance);
                startActivity(intentWallet);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Toast.makeText(context, "Under Construction..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView toolbartitle = (TextView) toolbar.findViewById(R.id.eshop);
        toolbartitle.setText("Wallet History");
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initComponent() {
        Superwallet = (TextView) findViewById(R.id.Superwallet);
        rvWalletlist = (RecyclerView) findViewById(R.id.rvWalletHistory);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        relativeMain = (LinearLayout) findViewById(R.id.relativeMain);
        tvAddmoney = (TextView) findViewById(R.id.tvAddmoney);
        tvAvailablebal = (TextView) findViewById(R.id.tvAvailablebal);
        tvAvailablebalsuper = (TextView) findViewById(R.id.tvAvailablebalsuper);
    }

    public void retryInternet() {
        try {
            final Dialog dialog = new Dialog(WalletHistory.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_internet_not_found);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Window window = dialog.getWindow();
            WindowManager.LayoutParams param = window.getAttributes();

            dialog.setCanceledOnTouchOutside(true);
            TextView txtDialogRetryOk = (TextView) dialog.findViewById(R.id.txtDialogRetryOk);
            TextView txtDialogRetryCancel = (TextView) dialog.findViewById(R.id.txtDialogRetryCancel);

            txtDialogRetryCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            txtDialogRetryOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Utils.isNetworkAvailable(WalletHistory.this)) {
                        dialog.dismiss();
                        loading = ProgressDialog.show(WalletHistory.this, "", getResources().getString(R.string.string_plzwait), false, false);
                        GetWalletHistoryDetails getMyWalletHistory = new GetWalletHistoryDetails();
                        getMyWalletHistory.execute();
                    } else {
                        Utils.ShowSnakBar(getResources().getString(R.string.string_nointernet), relativeMain, WalletHistory.this);
                    }

                }
            });


            dialog.show();
        } catch (Exception e) {

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isNetworkAvailable(WalletHistory.this)) {
            walletlist.clear();
            loading = ProgressDialog.show(WalletHistory.this, "", "Please wait...", false, false);
            GetWalletHistoryDetails getMyWalletHistory = new GetWalletHistoryDetails();
            getMyWalletHistory.execute();

        } else {
            retryInternet();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (activityType.equalsIgnoreCase("WalletSuccess")) {
            Intent intent = new Intent(WalletHistory.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (activityType.equalsIgnoreCase("MyAccount")) {
            Intent intent = new Intent(WalletHistory.this, MyAccountActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            Intent intent = new Intent(WalletHistory.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    private class GetWalletHistoryDetails extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL)
                    .build();
            //  walletlist.clear();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.getWalletHistory("wallet_transction", strUserId, strMobile, Integer.toString(pagecode));
                return curators;
            } catch (Exception E) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            IsLAstLoading = true;
            progressBar1.setVisibility(View.GONE);
            if (curators == null) {
            } else {

                if (curators.msgcode.equals("0")) {
                    try {
                        strWalletBalance = curators.walletBal;
                        strSuperWalletBalance = curators.super_walletBal;

                        tvAvailablebal.setText(getResources().getString(R.string.Rs) + " " + strWalletBalance);
                        if (strSuperWalletBalance.equalsIgnoreCase("0.00")) {
                            tvAvailablebalsuper.setText(getResources().getString(R.string.Rs) + " " + "0");
                        }
                        tvAvailablebalsuper.setText(getResources().getString(R.string.Rs) + " " + strSuperWalletBalance);
                        for (Api_Model.transction_data dataset : curators.transction_data) {
                            WalletModel walletModel;
                            walletModel = new WalletModel(dataset.OrderID, dataset.Remark, dataset.symbol, dataset.Amount, dataset.type, dataset.TransactionDate);
                            walletlist.add(walletModel);
                        }
                        walletHistoryAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                } else {
                    tvAvailablebal.setText(getResources().getString(R.string.Rs) + " " + "0");
                    tvAvailablebalsuper.setText(getResources().getString(R.string.Rs) + " " + "0");
                    if (walletlist.size() == 0) {
                        lyt_not_found.setVisibility(View.VISIBLE);
                    }
                    Utils.ShowSnakBar(curators.message, relativeMain, WalletHistory.this);
                }
            }
        }
    }
}
