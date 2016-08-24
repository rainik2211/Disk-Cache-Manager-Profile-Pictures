package rainiksoni.com.tlknsampleapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import rainiksoni.com.tlknsampleapplication.models.ContactInfo;

/**
 * Created by rainiksoni on 21/08/16.
 */

public class ContactsViewRecyclerAdapter extends RecyclerView.Adapter<ContactsViewHolder> {

    private Context context;
    private ArrayList<ContactInfo> contactList;

    //type of view that has to be loaded
    public static final int ITEM_TYPE_LARGE_LEFT_ALIGNED = 0;

    public static final int ITEM_TYPE_MEDIUM_RIGHT_ALIGNED = 1;

    public static final int ITEM_TYPE_SMALL_CENTRE_ALIGNED = 2;

    public static final int ITEM_TYPE_SMALL_LEFT_BOTTOM_ALIGNED = 3;


    public ContactsViewRecyclerAdapter(Context context, ArrayList contactsList){
        this.context = context;
        this.contactList = contactsList;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = null;
        if (viewType == ITEM_TYPE_LARGE_LEFT_ALIGNED){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_large_left_aligned, null);
        } else if (viewType == ITEM_TYPE_MEDIUM_RIGHT_ALIGNED){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_medium_right_aligned, null);
        } else if (viewType == ITEM_TYPE_SMALL_CENTRE_ALIGNED){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_small_center_aligned, null);
        } else {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_small_left_bottom_aligned, null);
        }
        ContactsViewHolder holder = new ContactsViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {

        Bitmap contactAvatar = AvatarController.getInstance().getBitmap(contactList.get(position).getContactImageKey());
        Log.d(ContactsViewRecyclerAdapter.class.getSimpleName(), "## ContactsViewRecyclerAdapter contactAvatar : "+contactAvatar);

        holder.contactTextView.setText((String)contactList.get(position).getContactName());
        if (contactAvatar!=null){
            holder.contactImage.setImageBitmap(contactAvatar);
        } else {
            holder.contactImage.setImageResource(R.drawable.avimg);
        }

    }

    @Override
    public int getItemCount() {
        return this.contactList.size();
    }

    /**
     * Four different types of view
     * {@code ITEM_TYPE_LARGE_LEFT_ALIGNED,
     *        ITEM_TYPE_MEDIUM_RIGHT_ALIGNED,
     *        ITEM_TYPE_SMALL_CENTRE_ALIGNED
     *        ITEM_TYPE_SMALL_LEFT_BOTTOM_ALIGNED}
     * are inflated on the basis of % value of current View's position
     *
     * @param position current position of the view
     * @return int itemType that has to be implemented
     */
    @Override
    public int getItemViewType(int position) {
        if (position % 3 == 0){
            return ITEM_TYPE_LARGE_LEFT_ALIGNED;
        } else if (position % 3 == 1){
           return ITEM_TYPE_MEDIUM_RIGHT_ALIGNED;
        } else if (position % 3 == 2){
            return ITEM_TYPE_SMALL_CENTRE_ALIGNED;
        } else {
            return ITEM_TYPE_SMALL_LEFT_BOTTOM_ALIGNED;
        }
    }
}
