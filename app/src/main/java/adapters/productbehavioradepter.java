package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.booksalways.shopping.R;

import java.util.ArrayList;

import models.DetailsListModel;


public class productbehavioradepter extends RecyclerView.Adapter<productbehavioradepter.ViewHolder> {
    Context context;
    ArrayList<DetailsListModel> listData;
    LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;

    public productbehavioradepter(Context context, ArrayList<DetailsListModel> bean) {
        this.listData = bean;
        this.context = context;
        this.inflater = (LayoutInflater.from(context));
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view, int i);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_details_attribute, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DetailsListModel bean = listData.get(position);
        holder.Atribute.setText(bean.getAttribute());
        holder.Value.setText(bean.getValue());
        holder.productitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, view, 1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return listData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Atribute, Value;
        LinearLayout productitems;

        public ViewHolder(View v) {
            super(v);
            Atribute = (TextView) v.findViewById(R.id.Atributes);
            Value = (TextView) v.findViewById(R.id.value);
            productitems = (LinearLayout) v.findViewById(R.id.productitems);
        }
    }


}







