package rainiksoni.com.tlknsampleapplication.models;

import android.graphics.drawable.Drawable;

/**
 * Created by rainiksoni on 21/08/16.
 *
 * Class used for setting and getting {@code contactImage, contactName}
 * In turn makes the object of {@link ContactInfo} one can access the data
 */

public class ContactInfo {

    private String contactName;
    private String contactImageUrl;

    public ContactInfo(String contact){
        contactName = contact;
    }

    public ContactInfo(String contact, String imageUrl){
        contactName = contact;
        contactImageUrl = imageUrl;
    }


    public String getContactName(){
        return this.contactName;
    }

    public void setName(String name){
        contactName = name;
    }

    public String getContactImageUrl() {
        return contactImageUrl;
    }

    public void setContactImageUrl(String contactImageUrl) {
        this.contactImageUrl = contactImageUrl;
    }
}
