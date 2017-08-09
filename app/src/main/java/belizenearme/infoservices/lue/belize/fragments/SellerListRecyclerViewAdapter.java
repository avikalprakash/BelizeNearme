package belizenearme.infoservices.lue.belize.fragments;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import belizenearme.infoservices.lue.belize.ChatActivity;
import belizenearme.infoservices.lue.belize.MainActivity;
import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.databind.SellerBuyer;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;


public class SellerListRecyclerViewAdapter extends RecyclerView.Adapter<SellerListRecyclerViewAdapter.ViewHolder> {

    private final List<SellerBuyer> mValues;

    public SellerListRecyclerViewAdapter(List<SellerBuyer> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        UtilityClass.getImage(holder.mView.getContext(),holder.mItem.getPhoto(),holder.userImg,R.drawable.user_default_image);
        holder.timeDateText.setText(UtilityClass.getTimeDifference(holder.mItem.getMessage_date()));
        holder.nameText.setText(holder.mItem.getFirst_name()+" "+holder.mItem.getLast_name());
        if(holder.mItem.getPhone().equals(""))
        holder.emailorContactText.setText(holder.mItem.getEmail());
        else holder.emailorContactText.setText(holder.mItem.getPhone());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(holder.mView.getContext(), ChatActivity.class);
                i.putExtra(AppConstants.RECIEVER_ID,holder.mItem.getId());
                holder.mView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircularImageView userImg;
        public final TextView timeDateText,notificationNoText,nameText,emailorContactText;
        public SellerBuyer mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            userImg=(CircularImageView)view.findViewById(R.id.userImg);
            timeDateText=(TextView)view.findViewById(R.id.timeDateText);
            notificationNoText=(TextView)view.findViewById(R.id.notificationNoText);
            nameText=(TextView)view.findViewById(R.id.nameText);
            emailorContactText=(TextView)view.findViewById(R.id.emailorContactText);
        }

       @Override
        public String toString() {
            return super.toString() + " '" + timeDateText.getText() + "'";
        }
    }
}
