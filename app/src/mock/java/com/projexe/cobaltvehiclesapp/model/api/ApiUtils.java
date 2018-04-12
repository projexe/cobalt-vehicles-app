package com.projexe.cobaltvehiclesapp.model.api;


/**
 * Static Class (or mimics a static class as Java doesnt have top-level static classes)
 * Wraps the Retrofit.Builder for the generation of a Retrofit client to access the Open
 * Vehicles api and parse returning JSON
 *
 * NOTE : THIS IS THE MOCK ApiUtils and has a DUMMY_URL as the API address
 * @author  Simon Hutton
 * @version 1.0
 */

public class ApiUtils {

    private static final String MOCK_URL = "http://private-MOCK-vehicles5.apiary-mock.com/";

    public static IVehiclesApiService getVehiclesApiService() {
        return RetrofitClient.getClient(MOCK_URL).create(IVehiclesApiService.class);
    }
}