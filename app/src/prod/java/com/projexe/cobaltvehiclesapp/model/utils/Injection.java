package com.projexe.cobaltvehiclesapp.model.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.projexe.cobaltvehiclesapp.model.IModelContract;
import com.projexe.cobaltvehiclesapp.model.ModelImplementation;
import com.projexe.cobaltvehiclesapp.model.AppModels;

/**
 * Simple injection class for injecting the "CobaltVehiclesApp" model i.e. the business functionality
 * class into an app build.
 * Future implementations could use Dagger2 for this purpose
 * This is the PRODUCTION version and will build into the 'prod' build variant
 * Enables injection of ***PRODUCTION*** implementations for {@link IModelContract} at compile time.
 * @author Simon Hutton
 * @version 1.0
 */
public class Injection {

    public static IModelContract provideModel(@NonNull Context ctx) {
        return AppModels.getInMemoryInstance(new ModelImplementation(ctx));  //NOTE - THIS IS THE PRODUCTION MODEL
    }
}
