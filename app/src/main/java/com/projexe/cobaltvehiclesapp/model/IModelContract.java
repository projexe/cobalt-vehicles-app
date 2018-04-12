package com.projexe.cobaltvehiclesapp.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Interface to the Business model.
 * @author Simon Hutton
 * @version 1.0
 */

public interface IModelContract {

    void fetchVehiclesFromApi(IApiResponse callbackOnApiResponseReceived);

    /**
     * Interface for API read callbacks
     */
    interface IApiResponse {
        void onApiSuccessReceived(@NonNull List<List> displayList);
        void onApiFailureReceived(int responseCode, String responseMessage);
        void onApiEmptyData(String responseMessage);
        void onApiOfflinePersistedData(List<List> displayList);
    }
}
