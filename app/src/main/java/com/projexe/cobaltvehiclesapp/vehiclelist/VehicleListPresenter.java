package com.projexe.cobaltvehiclesapp.vehiclelist;

import android.support.annotation.NonNull;

import com.projexe.cobaltvehiclesapp.model.IModelContract;

import java.util.List;

/**
 **This is the Presenter layer for the the WeatherList View (using the Model View Presenter architecture).
 *
 * Implements IVehicleContract.UserActionListener interface enabling View to communicate with it.
 * Passed View reference in Constructor enabling this Presenter to communicate with the View
 * Passed Injection of the Model/Repository Injection in the constructor to enable communication with the Model
 * All the presentation logic for the Vehicle list goes in here
 * @author Simon Hutton
 * @version 1.0
 */

public class VehicleListPresenter implements IVehicleContract.UserActionListener,
                                             IModelContract.IApiResponse{

    private final IVehicleContract.View mView;
    private final IModelContract mModel;

    /**
     * Constructor. Sets up callbacks to Repository and View.
     * @param view VIEW reference
     * @param model MODEL reference
     */
    public VehicleListPresenter(@NonNull IVehicleContract.View view,
                                @NonNull IModelContract model) {
        this.mModel = model;
        this.mView = view;
    }


    /**
     *  Fetch the vehicle data from the Vehicles API via the data model.
     */
    @Override
    public void fetchVehicleList() {
        // onApiResponseReceiced will callback to onApiResponseCallbackreceived handler in this class
        mModel.fetchVehiclesFromApi(this);
    }

    /**
     * Display the vehicle data
     * @param items list of vehicle data
     */
    @Override
    public void showVehicleDetails(List items) {
        mView.displayVehicleDetailsView(items);
    }

    /**
     * Clear the vehicle details
     */
    @Override
    public void clearVehicleDetails() {
        mView.clearVehicleDetailsView();

    }

    /**
     * Callback handler for a successful response received from the Vehicles API*
     * @param displayList List of data to be displayed
     */
    @Override
    public void onApiSuccessReceived(@NonNull List<List> displayList) {
        mView.displayVehicleList(displayList);
    }

    /**
     * Callback handler for a failure response received from the Vehicles API
     * @param responseCode code associated with the failure
     * @param responseMessage message associated with the failure
     */
    @Override
    public void onApiFailureReceived(int responseCode, String responseMessage) {
        mView.displayToast(responseMessage);
    }

    /**
     * Callback handler for a successful response received from the Vehicles API,
     * but no city data returned
     * @param responseMessage message associated with the callback
     */
    @Override
    public void onApiEmptyData(String responseMessage) {
        mView.displayToast(responseMessage);
    }

    /**
     * Callback handler for an unsuccessful response from the Vehicles API, because the device
     * has no internet access. The displaylist has therefore been populated from persisted data
     * @param displayList List of data to be displayed
     */
    @Override
    public void onApiOfflinePersistedData(List<List> displayList) {
        // TODO: 09/04/2018 Handle persistance of data for when app is offline
    }

}
