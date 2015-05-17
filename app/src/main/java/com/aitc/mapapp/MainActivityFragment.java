package com.aitc.mapapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private TextView ipAddress;
    private TextView countryCode;
    private TextView latitude;
    private TextView longitude;

    private Button get;
    private Button goToMap;

    String ipData;
    String countyCodeData;
    Double latitudeData;
    Double longitudeData;

    private ProgressDialog progressDialog;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Data...");
        progressDialog.setCancelable(false);

        ipAddress = (TextView) rootView.findViewById(R.id.ip_address);
        countryCode = (TextView) rootView.findViewById(R.id.country_code);
        latitude = (TextView) rootView.findViewById(R.id.latitude);
        longitude = (TextView) rootView.findViewById(R.id.longitude);

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
        return rootView;
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
}
