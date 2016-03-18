package org.simpumind.com.yctalumniconnect;

/**
 * Created by simpumind on 12/23/15.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReminisceAdapter extends RecyclerView.Adapter<ReminisceAdapter.ContactViewHolder> {

    private List<ReminisceInfo> contactList;

    public ReminisceAdapter(List<ReminisceInfo> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ReminisceInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.meaning);
        contactViewHolder.vSurname.setText(ci.pronunciation);
        contactViewHolder.vTitle.setText(ci.slang);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}
