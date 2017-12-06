package com.booksalways.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.Orderdetailadapter;
import models.OrderdetailModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;


public class OrderDetailsActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String strOrderiD;
    ProgressDialog loading;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    BooksAlways application;
    String StrUserId;
    TextView orderdetaildate, orderdetailstatus, orderdetailamount,
            tvmobilenumber, tvemailaddress, tvorderaddress, txtName, txtSuperwallet, txtBooksAlwayswallet;
    private Orderdetailadapter orderdetailadapter;
    ArrayList<OrderdetailModel> mydetailOrderlist = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;

    String StrCartCounter;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    TextView txtSubTotal, txtShippingCharge, txtDiscount, txtGrandTotal;
    LinearLayout linearDiscount, linearBooksAlwaysWallet, linearSuperWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_orderdetail);
        Bundle bundle = getIntent().getExtras();
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefBooksAlways", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();

        StrUserId = prefLogin.getString("userID", null);

        if (null != bundle) {
            strOrderiD = getIntent().getExtras().getString("OrderId");
        }


        prefCartCounter = OrderDetailsActivity.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");


        initToolbar();
        inotComp();
        Tools.systemBarLolipop(this);


        if (Utils.isNetworkAvailable(getBaseContext())) {
            loading = ProgressDialog.show(OrderDetailsActivity.this, "", "Please wait...", false, false);
            loadOrderdetails mloadOrderdetails = new loadOrderdetails();
            mloadOrderdetails.execute();
        } else {
            Utils.showToastShort("Please Check Your Internet Connection", OrderDetailsActivity.this);
        }
        mLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        orderdetailadapter = new Orderdetailadapter(OrderDetailsActivity.this, mydetailOrderlist);
        recyclerView.setAdapter(orderdetailadapter);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Order Detail");
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
    }

    public void inotComp() {
        orderdetaildate = (TextView) findViewById(R.id.orderdetaildate);
        orderdetailstatus = (TextView) findViewById(R.id.orderdetailstatus);
        orderdetailamount = (TextView) findViewById(R.id.orderdetailamount);
        txtName = (TextView) findViewById(R.id.txtName);

        txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);
        txtShippingCharge = (TextView) findViewById(R.id.txtShippingCharge);
        txtDiscount = (TextView) findViewById(R.id.txtDiscount);
        txtGrandTotal = (TextView) findViewById(R.id.txtGrandTotal);
        linearDiscount = (LinearLayout) findViewById(R.id.linearDiscount);
        linearSuperWallet = (LinearLayout) findViewById(R.id.linearSuperWallet);
        linearBooksAlwaysWallet = (LinearLayout) findViewById(R.id.linearBooksAlwaysWallet);
        tvmobilenumber = (TextView) findViewById(R.id.tvmobilenumber);
        tvemailaddress = (TextView) findViewById(R.id.tvemailaddress);
        tvorderaddress = (TextView) findViewById(R.id.tvorderaddress);

        txtSuperwallet = (TextView) findViewById(R.id.txtSuperwallet);
        txtBooksAlwayswallet = (TextView) findViewById(R.id.txtBooksAlwayswallet);
        recyclerView = (RecyclerView) findViewById(R.id.rvProductdetaillist);
        recyclerView.setNestedScrollingEnabled(false);


    }


    private class loadOrderdetails extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.getOrderDetailsApi("orders", "detail", StrUserId, strOrderiD);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }


        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {

                ShowRetryDialog("GetNoticeListArrayForUpdates");
            } else {
                if (curators.msgcode.equals("0")) {
                    for (Api_Model.item_list dataset : curators.item_list) {
                        OrderdetailModel myOrderModel;
                        myOrderModel = new OrderdetailModel(dataset.SR, dataset.name, dataset.quantity, dataset.price,
                                dataset.color, dataset.type);
                        mydetailOrderlist.add(myOrderModel);

                    }
                    orderdetailadapter.notifyDataSetChanged();
                    orderdetaildate.setText(curators.time);
                    orderdetailstatus.setText(curators.status);
                    orderdetailamount.setText(getResources().getString(R.string.rs) + " " + curators.amount);
                    txtSubTotal.setText("  " + getResources().getString(R.string.rs) + " " + curators.subtotal);
                    txtShippingCharge.setText("  " + getResources().getString(R.string.rs) + " " + curators.shipping + "  ");
                    txtDiscount.setText("  " + getResources().getString(R.string.rs) + " " + curators.discount);
                    txtBooksAlwayswallet.setText("- " + getResources().getString(R.string.rs) + " " + curators.BooksAlways_wallet);
                    txtSuperwallet.setText("- " + getResources().getString(R.string.rs) + " " + curators.super_wallet);
                    txtGrandTotal.setText("  " + getResources().getString(R.string.rs) + " " + curators.amount);

                    Log.e("my ", "discount" + curators.discount);
                    if (curators.discount.equalsIgnoreCase("")) {
                        linearDiscount.setVisibility(View.GONE);
                    } else {
                        linearDiscount.setVisibility(View.VISIBLE);
                    }
                    if (curators.BooksAlways_wallet.equalsIgnoreCase("0.00")) {
                        linearBooksAlwaysWallet.setVisibility(View.GONE);
                    } else {
                        linearBooksAlwaysWallet.setVisibility(View.VISIBLE);
                    }
                    /*if (curators.super_wallet.equalsIgnoreCase("0.00")) {
                        //linearSuperWallet.setVisibility(View.GONE);
                    } else {
                        //linearSuperWallet.setVisibility(View.VISIBLE);
                    }*/


                    for (Api_Model.address dataset : curators.address) {
                        txtName.setText(dataset.name);
                        tvmobilenumber.setText(dataset.phone);
                        tvemailaddress.setText(dataset.email);
                        tvorderaddress.setText(dataset.address);

                    }

                    Log.d("mydetailOrderlist", String.valueOf(mydetailOrderlist.size()));
                }
            }


        }

    }

    private void ShowRetryDialog(final String WhichAPILoad) {
        try {
            final Dialog dialog = new Dialog(OrderDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_internet_not_found);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

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
                    loading = ProgressDialog.show(OrderDetailsActivity.this, "", "Please wait...", false, false);

                    loadOrderdetails mGetNoticeListArrayForUpdates = new loadOrderdetails();
                    mGetNoticeListArrayForUpdates.execute();

                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            // Toast.makeText(MainCategoriesActivity.this, "BackWorking", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

    }


}
