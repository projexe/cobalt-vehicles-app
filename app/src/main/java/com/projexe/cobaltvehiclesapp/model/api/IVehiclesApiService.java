package com.projexe.cobaltvehiclesapp.model.api;

import com.projexe.cobaltvehiclesapp.model.api.schema.VehiclesList;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IVehiclesApiService {

/**
 * Invokes Retrofit to sent request webserver and returns a response. Each call yields its own HTTP request and response pair.
 * Calls may be executed synchronously with execute, or asynchronously with enqueue.
 * In either case the call can be canceled at any time with cancel.
 * A call that is busy writing its request or reading its response may receive a IOException; this is working as designed.
 */
    @GET("/vehicles")
    Call<VehiclesList> getVehiclesList();

}
