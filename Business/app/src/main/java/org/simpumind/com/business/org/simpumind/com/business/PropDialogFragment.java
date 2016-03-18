package org.simpumind.com.business.org.simpumind.com.business;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.simpumind.com.business.MainActivity;
import org.simpumind.com.business.R;

import butterknife.Bind;

/**
 * Created by simpumind on 1/25/16.
 */
public class PropDialogFragment extends DialogFragment {

    //@Bind(R.id.fname)
    private EditText busName;
    //@Bind(R.id.lname)
    private EditText busAddress;
    //@Bind(R.id.uname)
    private EditText busPhone;
   // @Bind(R.id.email)
    private EditText busEmail;

    public static String businessName;

    public PropDialogFragment() { /*empty*/ }

    /** creates a new instance of PropDialogFragment */
    public static PropDialogFragment newInstance(String bus) {
        businessName = bus;
        return new PropDialogFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getting proper access to LayoutInflater is the trick. getLayoutInflater is a                   //Function
        LayoutInflater inflater = getActivity().getLayoutInflater();


       final View view = inflater.inflate(R.layout.content_add_business, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(getActivity().getString(R.string.title)).setNeutralButton(
                getActivity().getString(R.string.btn), null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                final TextView txt = (TextView)view.findViewById(R.id.txt);
                busName =(EditText)view.findViewById(R.id.fname);
                busAddress =(EditText)view.findViewById(R.id.lname);
                busPhone =(EditText)view.findViewById(R.id.uname);
                busEmail =(EditText)view.findViewById(R.id.email);
                Toast.makeText(getActivity(), "This is a click button", Toast.LENGTH_SHORT).show();
                if(busName.getText().toString().length() > 0
                        && busEmail.getText().toString().length() > 0
                        && busAddress.getText().toString().length() > 0
                        && busPhone.getText().toString().length() > 0){

                    DirectoryData dr = new DirectoryData();
                    //   dr.setACL(new ParseACL(ParseUser.getCurrentUser()));
                    ParseObject obj = ParseObject.create("DirectoryData");
                    obj.put("name", busName.getText().toString());
                    obj.put("address", busAddress.getText().toString());
                    obj.put("phone", busPhone.getText().toString());
                    obj.put("email", busEmail.getText().toString());
                    obj.put("business", businessName);
                    obj.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e  == null){
                                txt.setText("Bussiness Added!");
                              //  new MainActivity.RemoteDataTask.execute();
                            }else{
                                txt.setText("Not Saved!");

                            }
                        }
                    });


                    busPhone.setText("");
                    busAddress.setText("");
                    busEmail.setText("");
                    busName.setText("");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
