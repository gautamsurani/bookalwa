package com.booksalways.shopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import customclass.MyRoundImageview;
import fragments.Dashboard;
import utils.Tools;
import utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    BooksAlways application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    String StrUserId, StrUserName, StrUserEmail, StrUserPhone, StrUserImage;

    String StrCartCounter;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefBooksAlways", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        prefCartCounter = MainActivity.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");
        Log.e("StrCartCounter", StrCartCounter);
        StrUserId = prefLogin.getString("userID", null);
        StrUserName = prefLogin.getString("name", null);
        StrUserEmail = prefLogin.getString("email", null);
        StrUserPhone = prefLogin.getString("phone", null);
        StrUserImage = prefLogin.getString("userimage", null);
        Log.e("StrUserImage", StrUserImage + "");
        application = (BooksAlways) getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();
        initToolbar();
        initDrawerMenu();
        Tools.systemBarLolipop(this);

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        fragment = new Dashboard();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        TextView txtCartCounter = (TextView) toolbar.findViewById(R.id.txtCartCounter);
        txtCartCounter.setText(StrCartCounter);
        textView.setText("BooksAlways");
        RelativeLayout relMyCart = (RelativeLayout) toolbar.findViewById(R.id.relMyCart);

        relMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCart.class);
                intent.putExtra("CartPage", "main");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

        setSupportActionBar(toolbar);
    }

    public void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = getLayoutInflater().inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(headerView);

        RelativeLayout linearUserDetail = (RelativeLayout) headerView.findViewById(R.id.linearUserDetail);
        LinearLayout lenearHeaderHome = (LinearLayout) headerView.findViewById(R.id.lenearHeaderHome);
        LinearLayout lenearHeaderCategory = (LinearLayout) headerView.findViewById(R.id.lenearHeaderCategory);
        LinearLayout lenearHeaderNotification = (LinearLayout) headerView.findViewById(R.id.lenearHeaderNotification);
        LinearLayout lenearHeaderMyyAccount = (LinearLayout) headerView.findViewById(R.id.lenearHeaderMyyAccount);
        LinearLayout lenearHeaderMyyWallet = (LinearLayout) headerView.findViewById(R.id.lenearHeaderMyyWallet);
        LinearLayout lenearHeaderMyCart = (LinearLayout) headerView.findViewById(R.id.lenearHeaderMyCart);
        LinearLayout lenearHeaderMyWishlist = (LinearLayout) headerView.findViewById(R.id.lenearHeaderMyWishlist);
        LinearLayout lenearHeaderMyOrder = (LinearLayout) headerView.findViewById(R.id.lenearHeaderMyOrder);
        LinearLayout lenearHeaderSelleUs = (LinearLayout) headerView.findViewById(R.id.lenearHeaderSelleUs);
        LinearLayout lenearHeaderContactUS = (LinearLayout) headerView.findViewById(R.id.lenearHeaderContactUS);
        LinearLayout lenearHeaderRfererYourFriend = (LinearLayout) headerView.findViewById(R.id.lenearHeaderRfererYourFriend);
        LinearLayout lenearHeaderAboutBooksAlways = (LinearLayout) headerView.findViewById(R.id.lenearHeaderAboutBooksAlways);
        LinearLayout lenearHeaderHelp = (LinearLayout) headerView.findViewById(R.id.lenearHeaderHelp);
        LinearLayout lenearHeaderTareUs = (LinearLayout) headerView.findViewById(R.id.lenearHeaderTareUs);
        LinearLayout lenearHeaderLogout = (LinearLayout) headerView.findViewById(R.id.lenearHeaderLogout);

        TextView txtUserNameHeader = (TextView) headerView.findViewById(R.id.txtUserNameHeader);
        TextView txtUserMobileHeader = (TextView) headerView.findViewById(R.id.txtUserMobileHeader);
        MyRoundImageview myroundimage = (MyRoundImageview) headerView.findViewById(R.id.myroundimage);


        imageLoader.displayImage(StrUserImage, myroundimage, options);
        txtUserNameHeader.setText(StrUserName);
        txtUserMobileHeader.setText(StrUserPhone);

        linearUserDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                drawer.closeDrawer(Gravity.LEFT);

            }
        });
        lenearHeaderHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        lenearHeaderCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                //  Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                Intent intent = new Intent(MainActivity.this, NewCategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lenearHeaderNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, Updates.class);

                intent.putExtra("PageTypeForPush", "NotPush");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lenearHeaderMyyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lenearHeaderMyyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, WalletHistory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        lenearHeaderMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, MyCart.class);
                intent.putExtra("CartPage", "main");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        lenearHeaderSelleUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, SellUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lenearHeaderMyWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, MyWishList.class);
                intent.putExtra("WishPage", "main");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


               /* try {
                    Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                    Intent viewIntent =
                            new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
*/

            }
        });

        lenearHeaderMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, MyOrder.class);
                intent.putExtra("PageType", "MainActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        lenearHeaderRfererYourFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, ReferActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lenearHeaderAboutBooksAlways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lenearHeaderHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        lenearHeaderContactUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lenearHeaderTareUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });
        lenearHeaderLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage(getResources().getString(R.string.str_common_LogOut));

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        try {
                            LoginManager.getInstance().logOut();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SharedPreferences prefThisLogin = getSharedPreferences("LogInPrefBooksAlways", 0); // 0 - for private mode
                        SharedPreferences.Editor editorThisLogin = prefThisLogin.edit();
                        editorThisLogin.putString("userID", null);
                        editorThisLogin.commit();
                        editorThisLogin.clear();
                        SharedPreferences prefThisCounter = getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
                        SharedPreferences.Editor editorThisCounter = prefThisCounter.edit();
                        editorThisCounter.putString("CartCounter", "0");
                        editorThisCounter.commit();
                        editorThisCounter.clear();
                        Intent i = new Intent(getApplicationContext(), RateUsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        editorLogin.putString("userID", null);
                        editorLogin.commit();
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int which) {
                        arg0.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.showToastShort("Tab back again to exit.", MainActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public DisplayImageOptions getImageOptions() {

        final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        return imageOptions;
    }
}
