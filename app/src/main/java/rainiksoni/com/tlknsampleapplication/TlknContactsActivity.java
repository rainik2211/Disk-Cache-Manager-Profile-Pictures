package rainiksoni.com.tlknsampleapplication;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import rainiksoni.com.tlknsampleapplication.models.ContactInfo;

/**
 * @author rainiksoni
 */
public class TlknContactsActivity extends AppCompatActivity implements IAvatarListener{

    private RecyclerView tlknContactsListView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private ArrayList<ContactInfo> contactsList;

    public static final String TAG = TlknContactsActivity.class.getSimpleName();

    ContactsViewRecyclerAdapter contactsViewRecyclerAdapter;

    /**
     * @return {@link ArrayList<String>} of contacts name in phonebook
     */
    public ArrayList<ContactInfo> getName() {
        ArrayList<ContactInfo> contactName = new ArrayList<ContactInfo>();
        int j = 0;
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String s1 = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            ContactInfo contactInfo = new ContactInfo(s1, AvatarController.urls[Utils.getSortedImage(j)]);
            j++;
            contactName.add(contactInfo);
        }
        cursor.close();
        return contactName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlkn_contacts);

        contactsList = getName();

        tlknContactsListView = (RecyclerView) findViewById(R.id.recycler_view);
        tlknContactsListView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        tlknContactsListView.setLayoutManager(staggeredGridLayoutManager);
        contactsViewRecyclerAdapter = new ContactsViewRecyclerAdapter(this, contactsList);
        tlknContactsListView.setAdapter(contactsViewRecyclerAdapter);

    }


    /**
     * {@code downloadBitmap}  downloads the image in byteArray format and
     * returns a converted Bitmap format
     *
     * @param url of the image that has to be downloaded
     * @return Bitmap of the image that has been downloaded
     * @throws IOException
     */
    private Bitmap downloadBitmap(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url.toString());
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length);
            Log.d(TAG, "## downloading image===" + bitmap);
            return bitmap;
        } else {
            throw new IOException("Download failed, HTTP response code "
                    + statusCode + " - " + statusLine.getReasonPhrase());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AvatarController.getInstance().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AvatarController.getInstance().register(this);
    }

    @Override
    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactsViewRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}

