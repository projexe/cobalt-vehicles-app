package com.projexe.cobaltvehiclesapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.projexe.cobaltvehiclesapp.R;
import com.projexe.cobaltvehiclesapp.model.api.ApiUtils;
import com.projexe.cobaltvehiclesapp.model.api.IVehiclesApiService;
import com.projexe.cobaltvehiclesapp.model.api.schema.Vehicle;
import com.projexe.cobaltvehiclesapp.model.api.schema.VehiclesList;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * The model class. Implements the business rules for the app
 * Controls calls to the external actor, the "Vehicles" API ,
 * handles the data returned and makes callbacks to the presenter
 * @author Simon Hutton
 * @version 1.0
 */
public class ModelImplementation implements IModelContract {
    //Constants
    private final String TAG = "ModelImplementation";
    private final Context context;
    private IVehiclesApiService vehiclesApiService;

    /**
     * CONTRUCTOR : Needs to be passed the context of the calling View in order to be able to read
     * shared preferences
     * @param ctx view context
     */
    public ModelImplementation(Context ctx) {
        this.context = ctx;
    }


    /**
     * Get an instance of the Vehicle API, then call it to return the 'vehicles' list
     * Callback to report responses.
     * @param callback callback handler
     */
    @Override
    public void fetchVehiclesFromApi(IApiResponse callback) {

        // get a reference to the retrofit client
        if (vehiclesApiService == null) {
            vehiclesApiService = ApiUtils.getVehiclesApiService();
        }

        // Call the Retrofit service to get vehicles. 'enqueue' call is executed asynchronously (therefore
        // not using other asynchronous tools like AsynchTask or RxJava in this implementation)
        try {
            vehiclesApiService.getVehiclesList()
                    .enqueue(new Callback<VehiclesList>() {

                        @Override
                        public void onResponse(@NonNull Call<VehiclesList> call, @NonNull Response<VehiclesList> response) {

                            final List<Vehicle> vehicleList;
                            try {
                                vehicleList = response.body().getVehicles();
                            } catch (NullPointerException e) {
                                // report the failure to the calling process
                                final String message =  context.getString(R.string.error_mess_api_failure_response);
                                Log.d(TAG, message);
                                Crashlytics.logException(e);
                                callback.onApiFailureReceived(999, message);
                                return;
                            }

                            if (response.isSuccessful()) {
                                final int vehicleListSize = vehicleList.size();
                                Log.d(TAG, "API Vehicle data received successfully. " + vehicleListSize + " vehicles returned");

                                if (vehicleListSize == 0) {
                                    callback.onApiEmptyData(context.getString(R.string.message_no_vehicle_data_to_show));
                                } else {
                                    // extract the data we want to return to the presenter
                                    final ArrayList dataList = formatVehicleDisplayDataFromAPI(vehicleList);
                                    callback.onApiSuccessReceived(dataList);
                                }

                            } else {
                                final String message = context.getString(R.string.error_mess_api_unsuccessful_response) + response.code();
                                Log.d(TAG, message );
                                callback.onApiFailureReceived(response.code(), message);
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<VehiclesList> call, @NonNull Throwable retrofitError) {
                            if (retrofitError instanceof UnknownHostException) {
                                // Device offline error
                                callback.onApiFailureReceived(998, context.getString(R.string.error_mess_api_no_internet));
                            } else {
                                // report the failure to the calling process
                                final String message =  context.getString(R.string.error_mess_api_failure_response) + retrofitError.getLocalizedMessage();
                                Log.d(TAG, message);
                                Crashlytics.logException(retrofitError);
                                callback.onApiFailureReceived(999, message);
                            }
                        }
                    });
        } catch (NullPointerException e) {
            final String message = context.getString(R.string.error_mess_api_null_pointer) + e.getLocalizedMessage();
            Log.d(TAG, message);
            callback.onApiFailureReceived(997, message);
        }
    }


    /**
     * Take the vehicle list returned from the Api and format it for display by the View
     * @param vehicleList returned from the Weather API
     * @return ArrayList just containing items formatted for display by the View.
     */
    private ArrayList<List> formatVehicleDisplayDataFromAPI(List<Vehicle> vehicleList) {
        ArrayList<List> rowsToDisplay = new ArrayList<>();
        ArrayList<String> columnData;

        // iterate along the list returned from the api writing formatted values
        // to rowsToDisplay
        for (Vehicle item : vehicleList) {
            columnData = new ArrayList<>();
            columnData.add(item.getVrn());  // vehicle name

            //supplementary column data - not displayed on the list, but displayed if a row is
            // selected
            columnData.add(String.valueOf(item.getVehicleId()));
            columnData.add(item.getCountry());
            columnData.add(item.getColor());
            columnData.add(item.getType());
            columnData.add(String.valueOf(item.getDefault()));

            rowsToDisplay.add(columnData);
        }
        return rowsToDisplay;
    }

}
