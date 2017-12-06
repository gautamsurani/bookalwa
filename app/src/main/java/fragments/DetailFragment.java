package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booksalways.shopping.R;

import java.util.ArrayList;

import adapters.productbehavioradepter;
import models.DetailsListModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    public static RecyclerView rvProductDetail;

    public static TextView tvIsEmpty;

    View view;


    public static ArrayList<DetailsListModel> mDetailList = new ArrayList<DetailsListModel>();

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail, container, false);

        rvProductDetail = (RecyclerView) view.findViewById(R.id.rvProductDetail);
        tvIsEmpty = (TextView) view.findViewById(R.id.tvIsEmpty);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvProductDetail.setLayoutManager(llm);


        return view;
    }

}
