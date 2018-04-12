package com.projexe.cobaltvehiclesapp.vehiclelist;

import java.util.List;

/**
 * Contract for the Vehicle App MVP This forms the interface between the View and the
 * Presenter layers
 *
 * interface "View" : the interface to the View layer. View (Fragment) will implement this interface.
 * In the Fragment the Presenter is instantiated and passed a reference to the Fragment.
 * All callbacks to the Fragment from the Presenter are made using the interface
 *
 * interface PresenterListener : interface implemented by the Presenter to enable calls to be made
 * from the View to the Presenter.
 *
 * @author Simon Hutton
 * @version 1.0
 * */

public interface IVehicleContract {

    interface View {

        // display the vehicle list on the view
        void displayVehicleList(List<List> displayList);

        // display a toast on the view
        void displayToast(String message);

        void displayVehicleDetailsView(List items);

        void clearVehicleDetailsView();
    }

    interface UserActionListener {

        // request to fetch vehicle list from server
        void fetchVehicleList();

        // request to show individual vehicle data
        void showVehicleDetails(List items);

        void clearVehicleDetails();
    }
}
