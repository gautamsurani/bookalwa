package adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.booksalways.shopping.BooksAlways;
import com.booksalways.shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

import dbhelper.LocalBooksAlwaysDB;
import models.ProductlistModel;
import utils.SmallBang;


public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Typeface fonts1;
    private List<ProductlistModel> items = new ArrayList<>();

    private Context ctx;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;
    private BooksAlways application;
    private OnItemClickListener mOnItemClickListener;
    LocalBooksAlwaysDB mLocalBooksAlwaysDB;
    ArrayList<String> ArrForvIEW = new ArrayList<>();
    public static int viewFormatProductList;

    public interface OnItemClickListener {
        void onItemClick(int position, View view, int i);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    private static class GridHolder extends RecyclerView.ViewHolder {

        ImageView imagFav, imgMainImg, imgSoldOut;
        TextView txtName, txtMrp1, txtMrp2;
        LinearLayout lv_parent;


        SmallBang mSmallBang;
        public customclass.Font_RobotoLight reply_count;

        GridHolder(View v) {
            super(v);

            imagFav = (ImageView) v.findViewById(R.id.imagFav);
            imgMainImg = (ImageView) v.findViewById(R.id.imgMainImg);

            imgSoldOut = (ImageView) v.findViewById(R.id.imgSoldOut);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtMrp1 = (TextView) v.findViewById(R.id.txtMrp1);
            txtMrp2 = (TextView) v.findViewById(R.id.txtMrp2);
            lv_parent = (LinearLayout) v.findViewById(R.id.lv_parent);
        }

    }

    private static class LinearHolder extends RecyclerView.ViewHolder {

        ImageView imagFav, imgMainImg, imgSoldOut;
        TextView txtName, txtMrp1, txtMrp2,txtextra,txtstock;
        LinearLayout lv_parent,msgLinear;


        SmallBang mSmallBang;
        public customclass.Font_RobotoLight reply_count;

        LinearHolder(View v) {
            super(v);

            imagFav = (ImageView) v.findViewById(R.id.imagFav);
            imgMainImg = (ImageView) v.findViewById(R.id.imgMainImg);

            imgSoldOut = (ImageView) v.findViewById(R.id.imgSoldOut);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtMrp1 = (TextView) v.findViewById(R.id.txtMrp1);
            txtMrp2 = (TextView) v.findViewById(R.id.txtMrp2);
            txtextra = (TextView) v.findViewById(R.id.txtextra);
            txtstock = (TextView) v.findViewById(R.id.txtstock);
            lv_parent = (LinearLayout) v.findViewById(R.id.lv_parent);
            msgLinear = (LinearLayout) v.findViewById(R.id.msgLinear);
        }

    }


    public ProductListAdapter(Context ctx, List<ProductlistModel> items, LocalBooksAlwaysDB AdpDb, ArrayList<String> Arr) {
        this.ctx = ctx;
        this.items = items;
        this.mLocalBooksAlwaysDB = AdpDb;
        application = (BooksAlways) ctx.getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();
        ArrForvIEW = Arr;


    }

    public DisplayImageOptions getImageOptions() {

        final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        return imageOptions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view;

        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productlist_linear, parent, false);
            return new LinearHolder(view);

        }else if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productlist, parent, false);
            return new GridHolder(view);
        }

        return null;

       /* View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productlist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;*/
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final ProductlistModel p = items.get(position);


        if (holder instanceof GridHolder) {

            final GridHolder holder1 = (GridHolder) holder;

            boolean baa = ArrForvIEW.contains(p.getProductID());
            if (baa) {
                holder1.imagFav.setImageResource(R.mipmap.fav_selcted);
            } else {
                holder1.imagFav.setImageResource(R.mipmap.fav_nonselcted);

            }


            if (p.getSold_out().equalsIgnoreCase("Yes")) {
                holder1.imgSoldOut.setVisibility(View.VISIBLE);
            } else {
                holder1.imgSoldOut.setVisibility(View.GONE);
            }

            imageLoader.displayImage(p.getImage().toString(), holder1.imgMainImg, options);

            holder1.txtName.setText(p.getName());
            holder1.txtMrp1.setText(ctx.getResources().getString(R.string.rs) + " " + p.getPrice());
            holder1.txtMrp1.setTypeface(holder1.txtMrp1.getTypeface(),Typeface.BOLD);

            holder1.txtMrp2.setText(ctx.getResources().getString(R.string.rs)+" " +p.getMrp());
            holder1.txtMrp2.setPaintFlags(holder1.txtMrp2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder1.mSmallBang = SmallBang.attach2Window((Activity) ctx);
            // setAnimation(holder.itemView, position);
            holder1.imagFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if (mOnItemClickListener != null) {
                       // ArrayList<String> WishLocalArr = new ArrayList<String>();
                        ArrForvIEW.clear();

                        Cursor c = mLocalBooksAlwaysDB.ShowTableWishList();
                        if (c.getCount() > 0) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                ArrForvIEW.add(c.getString(1));
                            }

                            boolean b = ArrForvIEW.contains(p.getProductID());
                            if (b) {
                                holder1.imagFav.setImageResource(R.mipmap.fav_nonselcted);
                                mLocalBooksAlwaysDB.DeleteWishListByID(p.getProductID());
                                ArrForvIEW.remove(p.getProductID());
                            } else {
                                holder1.imagFav.setImageResource(R.mipmap.fav_selcted);
                                holder1.mSmallBang.bang(view);
                                mLocalBooksAlwaysDB.InsertWishListData(p.getProductID(), p.getName(),
                                        p.getImage(), p.getPrice(), p.getMrp());
                                ArrForvIEW.add(p.getProductID());
                            }
                        } else {
                            holder1.imagFav.setImageResource(R.mipmap.fav_selcted);
                            holder1.mSmallBang.bang(view);
                            mLocalBooksAlwaysDB.InsertWishListData(p.getProductID(), p.getName(),
                                    p.getImage(), p.getPrice(), p.getMrp());
                            ArrForvIEW.add(p.getProductID());
                        }

                        notifyMAIN(holder.getAdapterPosition());
                        mOnItemClickListener.onItemClick(position, view, 0);
                    }




                }
            });
            holder1.lv_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, view, 1);
                    }
                }
            });

        }else if (holder instanceof LinearHolder){

            final LinearHolder holder2 = (LinearHolder) holder;

            boolean baa = ArrForvIEW.contains(p.getProductID());
            if (baa) {
                holder2.imagFav.setImageResource(R.mipmap.fav_selcted);
            } else {
                holder2.imagFav.setImageResource(R.mipmap.fav_nonselcted);

            }


            if (p.getSold_out().equalsIgnoreCase("Yes")) {
                holder2.imgSoldOut.setVisibility(View.VISIBLE);
                holder2.txtstock.setVisibility(View.GONE);
            } else {
                holder2.imgSoldOut.setVisibility(View.GONE);
                holder2.txtstock.setText("In Stock");
                holder2.txtstock.setVisibility(View.VISIBLE);
            }

            imageLoader.displayImage(p.getImage().toString(), holder2.imgMainImg, options);

            holder2.txtName.setText(p.getName());

            if (p.getQty_message().isEmpty()){
                holder2.msgLinear.setVisibility(View.GONE);
            }else {
                holder2.msgLinear.setVisibility(View.VISIBLE);
                holder2.txtextra.setText(p.getQty_message());
            }


            holder2.txtMrp1.setText(ctx.getResources().getString(R.string.rs) + " " + p.getPrice());
            holder2.txtMrp1.setTypeface(holder2.txtMrp1.getTypeface(),Typeface.BOLD);

            holder2.txtMrp2.setText(ctx.getResources().getString(R.string.rs)+" " +p.getMrp());
            holder2.txtMrp2.setPaintFlags(holder2.txtMrp2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder2.mSmallBang = SmallBang.attach2Window((Activity) ctx);
            // setAnimation(holder.itemView, position);
            holder2.imagFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        ArrForvIEW.clear();

                        Cursor c = mLocalBooksAlwaysDB.ShowTableWishList();
                        if (c.getCount() > 0) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                ArrForvIEW.add(c.getString(1));
                            }

                            boolean b = ArrForvIEW.contains(p.getProductID());
                            if (b) {
                                holder2.imagFav.setImageResource(R.mipmap.fav_nonselcted);
                                mLocalBooksAlwaysDB.DeleteWishListByID(p.getProductID());
                                ArrForvIEW.remove(p.getProductID());
                            } else {
                                holder2.imagFav.setImageResource(R.mipmap.fav_selcted);
                                holder2.mSmallBang.bang(view);
                                mLocalBooksAlwaysDB.InsertWishListData(p.getProductID(), p.getName(),
                                        p.getImage(), p.getPrice(), p.getMrp());
                                ArrForvIEW.add(p.getProductID());
                            }
                        } else {
                            holder2.imagFav.setImageResource(R.mipmap.fav_selcted);
                            holder2.mSmallBang.bang(view);
                            mLocalBooksAlwaysDB.InsertWishListData(p.getProductID(), p.getName(),
                                    p.getImage(), p.getPrice(), p.getMrp());
                            ArrForvIEW.add(p.getProductID());
                        }

                        mOnItemClickListener.onItemClick(position, view, 0);
                    }
                }
            });
            holder2.lv_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, view, 1);
                    }
                }
            });


        }




    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void notifyMAIN(int mainPos) {
        notifyItemChanged(mainPos);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size()==0){
            return 0;
        }else if (viewFormatProductList==0){
            return 0;
        }else if (viewFormatProductList==1){
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}