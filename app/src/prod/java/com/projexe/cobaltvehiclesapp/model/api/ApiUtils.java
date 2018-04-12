package com.projexe.cobaltvehiclesapp.model.api;


/**
 * Static Class (or mimics a static class as Java doesnt have top-level static classes)
 * Wraps the Retrofit.Builder for the generation of a Retrofit client to access the Open
 * Vehicles api and parse returning JSON
 * @author  Simon Hutton
 * @version 1.0
 */

public class ApiUtils {

    private static final String BASE_URL = "http://private-6d86b9-vehicles5.apiary-mock.com/";

    public static IVehiclesApiService getVehiclesApiService() {
        return RetrofitClient.getClient(BASE_URL).create(IVehiclesApiService.class);
    }
}