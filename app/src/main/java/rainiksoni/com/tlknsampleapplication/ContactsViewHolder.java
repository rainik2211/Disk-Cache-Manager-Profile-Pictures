package rainiksoni.com.tlknsampleapplication;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import rainiksoni.com.tlknsampleapplication.UIWidget.RoundedImageView;

/**
 * Created by rainiksoni on 21/08/16.
 */

public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView contactTextView;
    public CircularImageView contactImage;
    public LinearLayout rootLayout;


    public ContactsViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        contactTextView = (TextView) itemView.findViewById(R.id.country_name);
        contactImage = (CircularImageView) itemView.findViewById(R.id.countpho);
        rootLayout = (LinearLayout) itemView.findViewById(R.id.root_layout);



//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contactImage.getLayoutParams();
//        params.gravity = Gravity.RIGHT;
//        countryPhoto.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
