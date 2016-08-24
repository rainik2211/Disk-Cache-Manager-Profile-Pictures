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
    private String contactImageKey;

    public ContactInfo(String contact){
        contactName = contact;
    }

    public ContactInfo(String contact, String imageUrl, String imageKey){
        contactName = contact;
        contactImageUrl = imageUrl;
        contactImageKey = imageKey;
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

    public String getContactImageKey() {
        return contactImageKey;
    }

    public void setContactImageKey(String contactImageKey) {
        this.contactImageKey = contactImageKey;
    }
}
