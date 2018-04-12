package com.projexe.cobaltvehiclesapp.model;

import android.support.annotation.NonNull;

/**
 *
 * @author Simon Hutton
 * @version 1.0
 */
public class AppModels {

    private AppModels() {
        // no instance - used as a static class
    }

    private static IModelContract model = null;

    public synchronized static IModelContract getInMemoryInstance (@NonNull IModelContract modelImplemention) {
        if (null == model) {
            model = modelImplemention;
        }
        return model;
    }
}
