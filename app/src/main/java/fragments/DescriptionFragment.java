package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.booksalways.shopping.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment {

    View view;

    public static HtmlTextView tvProductDesc;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_description, container, false);

        tvProductDesc = (HtmlTextView) view.findViewById(R.id.tvProductDesc);

        return view;
    }

}
