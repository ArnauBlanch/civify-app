package com.civify.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class GeocoderAdapter extends AsyncTask<String, Void, Address> {

    private static final String TAG = GeocoderAdapter.class.getSimpleName();

    private static final String GOOGLE_API_LONG_NAME = "long_name";
    private static final String GOOGLE_API_SHORT_NAME = "short_name";

    private static final int STREET_NUMBER_INDEX = 1;
    private static final int ADDRESS_LINE_INDEX = 1;
    private static final int LOCALITY_INDEX = 2;
    private static final int SUB_ADMIN_AREA_INDEX = 3;
    private static final int ADMIN_AREA_INDEX = 4;
    private static final int COUNTRY_INDEX = 5;
    private static final int POSTAL_CODE_INDEX = 6;

    private final Context mContext;
    private final LocalityCallback mCallback;
    private final Location mLocation;
    private String mErrorMessage;
    private Exception mError;

    private GeocoderAdapter(@NonNull Context context,
            @NonNull Location location, @NonNull LocalityCallback callback) {
        mContext = context;
        mLocation = location;
        mCallback = callback;
    }

    public static void getLocality(@NonNull Context context,
            @NonNull Location location, @NonNull LocalityCallback callback) {
        new GeocoderAdapter(context, location, callback).execute();
    }

    @Nullable
    @Override
    protected Address doInBackground(String... strings) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        Address result = null;
        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();
        try {
            List<Address> geocodedAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (geocodedAddresses != null && !geocodedAddresses.isEmpty()) {
                result = geocodedAddresses.get(0);
            }
        } catch (IOException e) {
            Throwable cause = e.getCause();
            Log.i(TAG, "Error " + (cause != null ? cause.getClass().getSimpleName()
                    : '(' + e.getClass().getSimpleName() + ':' + e.getMessage() + ')')
                    + " getting locality with Geocoder. "
                    + "Trying with HTTP/GET on Google Maps API.");
            result = geolocateFromGoogleApis(latitude, longitude);
        }
        return result;
    }

    private Address geolocateFromGoogleApis(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latitude + ',' + longitude + "&sensor=true&language="
                + Locale.getDefault().getLanguage();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        ResponseBody responseBody = null;
        try {
            // Synchronous call because we're already on a background thread behind UI
            response = client.newCall(request).execute();
            responseBody = response.body();
            String jsonData = responseBody.string();
            Log.d(TAG, jsonData);
            if (response.isSuccessful()) {
                return getAddressFromGoogleApis(jsonData);
            }
            mErrorMessage = "Unexpected code getting locality with HTTP/GET "
                    + "on Google Maps API: "
                    + response.code();
        } catch (IOException | JSONException e) {
            mErrorMessage = "Error getting locality with HTTP/GET on Google Maps API";
            mError = e;
        } finally {
            if (response != null) response.close();
            if (responseBody != null) responseBody.close();
        }
        return null;
    }

    private Address getAddressFromGoogleApis(String jsonData) throws JSONException {
        Address address = new Address(Locale.getDefault());
        JSONObject addressObject = new JSONObject(jsonData);
        JSONObject results = addressObject.getJSONArray("results").getJSONObject(0);
        JSONArray addressComponents = results.getJSONArray("address_components");
        JSONObject location = results.getJSONObject("geometry").getJSONObject("location");
        address.setLatitude(location.getDouble("lat"));
        address.setLongitude(location.getDouble("lng"));
        // Street Address, Street Number
        address.setAddressLine(0, addressComponents.getJSONObject(ADDRESS_LINE_INDEX)
                .getString(GOOGLE_API_LONG_NAME) + ", "
                + addressComponents.getJSONObject(STREET_NUMBER_INDEX)
                .getString(GOOGLE_API_SHORT_NAME));

        address.setLocality(addressComponents.getJSONObject(LOCALITY_INDEX)
                .getString(GOOGLE_API_LONG_NAME));
        address.setAdminArea(addressComponents.getJSONObject(ADMIN_AREA_INDEX)
                .getString(GOOGLE_API_LONG_NAME));
        address.setSubAdminArea(
                addressComponents.getJSONObject(SUB_ADMIN_AREA_INDEX)
                        .getString(GOOGLE_API_LONG_NAME));
        JSONObject countryObject = addressComponents.getJSONObject(COUNTRY_INDEX);
        address.setCountryName(countryObject.getString(GOOGLE_API_LONG_NAME));
        address.setCountryCode(countryObject.getString(GOOGLE_API_SHORT_NAME));
        address.setPostalCode(
                addressComponents.getJSONObject(POSTAL_CODE_INDEX)
                        .getString(GOOGLE_API_SHORT_NAME));
        return address;
    }

    @Override
    protected void onPostExecute(Address address) {
        if (address == null) {
            if (mErrorMessage != null) {
                if (mError != null) Log.e(TAG, mErrorMessage, mError);
                else Log.e(TAG, mErrorMessage);
            } else {
                Log.w(TAG, "No matches found or service unavailable.");
            }
            mCallback.onLocalityError();
        } else mCallback.onLocalityResponse(formatAddress(address));
    }

    @NonNull
    private String formatAddress(@NonNull Address address) {
        String addressText = "";
        String streetAndNumber = address.getAddressLine(0);
        if (!(streetAndNumber == null || streetAndNumber.isEmpty())) {
            addressText += streetAndNumber;
        }
        String locality = address.getLocality();
        if (locality != null) {
            if (!(addressText.isEmpty() || addressText.trim().endsWith(","))) {
                addressText += ", ";
            }
            addressText += locality;
        }
        return addressText;
    }
}
