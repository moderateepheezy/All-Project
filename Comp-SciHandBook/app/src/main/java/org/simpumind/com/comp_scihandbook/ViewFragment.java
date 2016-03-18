package org.simpumind.com.comp_scihandbook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {

    TextView textView;
    String file;
    public ViewFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String file){
        ViewFragment f = new ViewFragment();
        f.file = file;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(
                R.layout.splashs,
                container,
                false);

        textView =(TextView)view.findViewById(R.id.txtDetails);
        textView.setText(file);

        textView.setMovementMethod(new ScrollingMovementMethod());

        return  view;

    }


}
