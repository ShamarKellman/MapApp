package com.aitc.mapapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.volley.Utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private RelativeLayout relativeLayout;

    private TextView ipAddress;
    private TextView countryCode;
    private TextView latitude;
    private TextView longitude;
    private TextView longPressMe;

    private Button get;
    private Button goToMap;

    String ipData;
    String countyCodeData;
    Double latitudeData;
    Double longitudeData;

    private ProgressDialog progressDialog;

    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    private int numMessages = 0;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.frag_root_layout);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Data...");
        progressDialog.setCancelable(false);

        ipAddress = (TextView) rootView.findViewById(R.id.ip_address);
        countryCode = (TextView) rootView.findViewById(R.id.country_code);
        latitude = (TextView) rootView.findViewById(R.id.latitude);
        longitude = (TextView) rootView.findViewById(R.id.longitude);
        longPressMe = (TextView) rootView.findViewById(R.id.long_press_text);

        get = (Button) rootView.findViewById(R.id.get);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                hidepDialog();
            }
        });

        goToMap = (Button) rootView.findViewById(R.id.go_to_map);

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });

        /*Register long_press_text for context menu*/
        registerForContextMenu(longPressMe);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("CM Cool");
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cm_get_data:
                getData();
                return true;

            case R.id.cm_exit:
                getActivity().finish();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void goToMap() {
        if(ipAddress.getText().toString().isEmpty() || ipAddress.getText().toString() == "") {
            Toast.makeText(getActivity(), "Get Data First", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("ip", ipData);
            intent.putExtra("country_code", countyCodeData);
            intent.putExtra("latitude", latitudeData);
            intent.putExtra("longitude", longitudeData);
            startActivity(intent);
        }
    }

    public void getData() {

        showpDialog();

        String url = "http://www.telize.com/geoip";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json object response
                    // response will be a json object
                    ipData = response.getString("ip");
                    countyCodeData = response.getString("country_code");
                    latitudeData = response.getDouble("latitude");
                    longitudeData = response.getDouble("longitude");

                    ipAddress.setText(ipData);
                    countryCode.setText(countyCodeData);
                    latitude.setText(latitudeData.toString());
                    longitude.setText(longitudeData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }
                hidepDialog();
                displayNotification();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    protected void displayNotification() {
        Log.i("Start", "notification");

         /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setContentTitle("New Map Info");
        mBuilder.setContentText("IP: " + ipData + "\nCountry Code: " + countyCodeData);
        mBuilder.setTicker("New Map Info Alert!!!");
        mBuilder.setSmallIcon(R.drawable.map);

        /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

        /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(getActivity(), MapsActivity.class);
        resultIntent.putExtra("ip", ipData);
        resultIntent.putExtra("country_code", countyCodeData);
        resultIntent.putExtra("latitude", latitudeData);
        resultIntent.putExtra("longitude", longitudeData);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public void setBackgroundColor(int color) {
       relativeLayout.setBackgroundColor(color);
    }
}
