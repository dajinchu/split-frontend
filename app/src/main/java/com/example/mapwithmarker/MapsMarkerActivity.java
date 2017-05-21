package com.example.mapwithmarker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    List<String> symbols = Arrays.asList("a"," "," ");
    private ListView listview;
    private GoogleMap mymap;
    private BitmapDescriptor icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_main);
        listview = ((ListView)findViewById(R.id.symbolList));
        listview.setAdapter(new SymbolListAdapter(this, symbols));
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.setPadding(0,100,0,0);
        icon = BitmapDescriptorFactory.fromResource(R.drawable.beacon1);

        mymap = googleMap;

        mymap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney")
                .icon(icon));
        mymap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        new GetBeaconTask().execute();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // get content height
        int contentWidth = listview.getChildAt(0).getWidth()+30;

        // set listview height
        ViewGroup.LayoutParams lp = listview.getLayoutParams();
        lp.width = contentWidth;
        listview.setLayoutParams(lp);
    }

    class GetBeaconTask extends AsyncTask<Void,JSONObject,JSONObject>
    {


        protected void onPreExecute() {
            //display progress dialog.

        }
        protected JSONObject doInBackground(Void... params) {

            URL urll = null;
            InputStream is = null;
            JSONObject json = null;
            HttpURLConnection conn = null;
            try {
                urll = new URL("http://878b80f3.ngrok.io/games/5921490a4f3e8b2c523355c7");
                conn = (HttpURLConnection) urll.openConnection();
                is = conn.getInputStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = new JSONObject(sb.toString());
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;
        }



        protected void onPostExecute(JSONObject result) {
            // dismiss progress dialog and update ui
            try {
                Log.d("MapsActivity","reading beacons");
                JSONArray beacons = result.getJSONArray("beacons");
                for(int i =0; i < beacons.length(); i ++){
                    JSONObject beacon = beacons.getJSONObject(i);
                    LatLng tmp = new LatLng((double) beacon.get("lat"), (double) beacon.get("lng"));
                    mymap.addMarker(new MarkerOptions().position(tmp)
                            .title("beacon!")
                            .icon(icon));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
