package org.coursera.capstone.client;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;

public class GetAddressTask extends AsyncTask<Location, Void, String> {

    
    Context localContext;
    TextView textView;
    public GetAddressTask(Context context) {

       
        super();

        localContext = context;
    }
    public GetAddressTask(Context context,TextView tv) {
		// TODO Auto-generated constructor stub
    	this(context);
    	textView=tv;
	}

    @Override
    protected String doInBackground(Location... params) {
        
        Geocoder geocoder = new Geocoder(localContext, Locale.ENGLISH);

        Location location = params[0];
        List <Address> addresses = null;

        try {

            addresses = geocoder.getFromLocation(location.getLatitude(),
                location.getLongitude(), 1
            );
           
            } catch (IOException exception1) {


                exception1.printStackTrace();
                return exception1.toString();
           
            } catch (IllegalArgumentException exception2) {
                exception2.printStackTrace();
                return exception2.toString();
            }
            
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressText = 
                        address.getMaxAddressLineIndex() > 0 ?
                                (address.getAddressLine(0)+" ") : "";
                       addressText+=address.getLocality()!=null?(address.getLocality()+" "):"";

                       addressText+= address.getCountryName()!=null?(address.getCountryName()+" "):"";

                return addressText;

           
            } else {
              return "nothing found";
            }
    }

    @Override
    protected void onPostExecute(String address) {	           
        textView.setText(address);
    }
}