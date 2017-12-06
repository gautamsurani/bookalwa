package adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booksalways.shopping.R;

import java.util.ArrayList;

import models.OrderdetailModel;


public class Orderdetailadapter extends RecyclerView.Adapter<Orderdetailadapter.DataObjectHolder>
{


    private ArrayList<OrderdetailModel> bean;
    private Activity context;
    LayoutInflater inflater;



    public Orderdetailadapter(Activity context, ArrayList<OrderdetailModel> bean) {

         this.context = context;
         this.bean = bean;
         this.inflater = (LayoutInflater.from(context));

    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder

    {

        TextView tvordertitle,productquanity,tvProductprice,txtOption;

        public DataObjectHolder(View itemView)
        {
            super(itemView);


            tvordertitle = (TextView) itemView.findViewById(R.id.tvordertitle);
            productquanity= (TextView) itemView.findViewById(R.id.productquanity);
            tvProductprice= (TextView) itemView.findViewById(R.id.tvProductprice);
            txtOption= (TextView) itemView.findViewById(R.id.txtOption);

           // tvgrandtotal= (TextView) itemView.findViewById(R.id.tvgrandtotal);
        }

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())  .inflate(R.layout.raw_orderdetail, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position)
    {
        OrderdetailModel mOrderdetailModel=bean.get(position);
        holder.tvordertitle.setText(bean.get(position).getName());
        holder.productquanity.setText(String.valueOf(mOrderdetailModel.getQuantity()));
        holder.txtOption.setText(String.valueOf(mOrderdetailModel.getColor()));
        holder.tvProductprice.setText(String.valueOf(context.getResources().getString(R.string.rs) + " " + mOrderdetailModel.getPrice()));
        double TotalCount= Double.parseDouble(mOrderdetailModel.getQuantity())*Double.parseDouble(mOrderdetailModel.getPrice());

       // holder.tvgrandtotal.setText(String.valueOf(context.getResources().getString(R.string.rs)+" "+TotalCount));
    }

    @Override
    public int getItemCount()
    {
        return bean.size();
    }

}