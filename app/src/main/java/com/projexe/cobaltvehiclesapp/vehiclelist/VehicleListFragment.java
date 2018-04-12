package com.projexe.cobaltvehiclesapp.vehiclelist;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projexe.cobaltvehiclesapp.R;
import com.projexe.cobaltvehiclesapp.model.utils.Injection;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying a list of vehicle data.
 * Implements interface for communicating with presenter which controls the presentation of the data
 *
 *
 */
public class VehicleListFragment extends Fragment implements IVehicleContract.View {

    private final String TAG = "VehicleListFragment";
    private final String INSTANCESTATE_DISPLAY_TRUE = "INSTANCESTATE_DISPLAY_TRUE";
    private final String INSTANCESTATE_VEHICLE_DETAILS = "INSTANCESTATE_VEHICLE_DETAILS";

    private int numberOfVehicleDetailsRows;
    private View fragmentView;
    private VehicleListAdapter vehicleListAdapter;
    private IVehicleContract.UserActionListener mPresenter;
    private ConstraintLayout vehicleFragmentLayout, vehicleDetailsLayout;
    private ConstraintSet vehicleConstraintSet = new ConstraintSet();  // set of constraints for display of individual vehicle
    private ConstraintSet listConstraintSet = new ConstraintSet();   // set of constraints for vehicle list

    private boolean isDisplayDetails;
    private List mDetailsList;

    public VehicleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Restore instance state
        if (savedInstanceState != null) {
            isDisplayDetails = savedInstanceState.getBoolean(INSTANCESTATE_DISPLAY_TRUE);
            if (isDisplayDetails) {
                mDetailsList = savedInstanceState.getCharSequenceArrayList(INSTANCESTATE_VEHICLE_DETAILS);
            }
        }

        fragmentView = inflater.inflate(R.layout.vehicle_display_fragment, container, false);


        final Resources res = getResources();
        final int detailPercentPortrait = res.getInteger(R.integer.portrait_screen_details_percentage);
        final int detailPercentLandscape = res.getInteger(R.integer.landscape_screen_details_percentage);
        final boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // set screen split guideline depending on whether vehicle details are displayed
        final Guideline guideLine = fragmentView.findViewById(R.id.screen_split_guideline);
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        if (isDisplayDetails) {
            if (isPortrait) {
                params.guidePercent = (float) detailPercentPortrait / 100;
            } else {
                params.guidePercent = (float) detailPercentLandscape / 100;
            }
        } else {
            params.guidePercent = 1f;
        }
        guideLine.setLayoutParams(params);

        vehicleListAdapter = new VehicleListAdapter(new ArrayList<List>(0), new VehicleListAdapter.VehicleItemListener() {
            @Override
            public void onVehicleClick(List items) {
                mPresenter.showVehicleDetails(items);
            }
        });

        final ImageButton clearVehicleButton;
        clearVehicleButton = fragmentView.findViewById(R.id.button_cancel_vehicle_display);
        clearVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clearVehicleDetails();
            }
        });


        // dynamically build the Vehicle Details view. This will allow this fragment to handle any
        // change in the number of vehicle details fields in the future
        numberOfVehicleDetailsRows = res.getInteger(R.integer.vehicle_detail_row_count);
        for (int i = 0 ; i < numberOfVehicleDetailsRows ; i++ ) {
            addDetailsRow();
        }

        // Initialise constraint sets for screen split transitions

        vehicleFragmentLayout = fragmentView.findViewById(R.id.vehicle_display_fragment_layout);
        vehicleConstraintSet.clone(vehicleFragmentLayout);
        listConstraintSet.clone(vehicleFragmentLayout);

        // layout will transition from the vehicle_data.xml percentage to full screen
        if (isPortrait) {
            listConstraintSet.setGuidelinePercent(R.id.screen_split_guideline, 1f);
            vehicleConstraintSet.setGuidelinePercent(R.id.screen_split_guideline, (float) detailPercentPortrait/100);
        } else {
            listConstraintSet.setGuidelinePercent(R.id.screen_split_guideline, 1f);
            vehicleConstraintSet.setGuidelinePercent(R.id.screen_split_guideline, (float) detailPercentLandscape/100);
        }

        // initialise recycler view for showing list
        final RecyclerView recyclerView;
        recyclerView = fragmentView.findViewById(R.id.vehicle_list);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(vehicleListAdapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        /* Initialise the Presenter. Used to call methods on the presenter. Constructor is injected
           with a reference to the Business Model which is provided by Injection class.
           Future enhancement would be to replace with Dagger2 Library call
           Note any reference in the Model to the application context (say for Shared Preference calls)
           will need to be passed here */

        mPresenter = new VehicleListPresenter(this, Injection.provideModel(getActivity().getApplicationContext()));
        mPresenter.fetchVehicleList();

        if (isDisplayDetails) {
            this.displayVehicleDetailsView(mDetailsList);
        }
        return fragmentView;
    }

    @Override
    public void displayVehicleList(List<List> displayList) {
        doScreenTransition();
        vehicleListAdapter.updateVehicles(displayList);
    }

    /**
     * Display a toast message
     * @param message to be displayed
     */
    @Override
    public void displayToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Save instance state data for reloading on the next onCreate
     * @param outState bundle of state information
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INSTANCESTATE_DISPLAY_TRUE, isDisplayDetails);
        if (isDisplayDetails) {
            ArrayList arr = new ArrayList();
            arr = (ArrayList) mDetailsList;
            outState.putCharSequenceArrayList(INSTANCESTATE_VEHICLE_DETAILS, arr);
        }
    }

    /**
     * Display seperate view containing individual vehicle details
     * @param items
     */
    @Override
    public void displayVehicleDetailsView(List items) {

        mDetailsList = items;
        isDisplayDetails = true;

        if (items.size() > numberOfVehicleDetailsRows) {
            displayToast(getString(R.string.too_much_vehicle_data));
            return;
        }

        final Resources res = getResources();
        final String[] row_names = res.getStringArray(R.array.vehicle_detail_row_titles);
        final LinearLayout layout = fragmentView.findViewById(R.id.vehicle_details_row_bed);

        ConstraintLayout dataRow;
        int numberOfDetailsRows = layout.getChildCount();
        for (int i = 0 ; i < numberOfDetailsRows ; i++) {
            dataRow = (ConstraintLayout) layout.getChildAt(i);
            ((TextView) dataRow.getChildAt(0)).setText (row_names[i]);
            ((TextView) dataRow.getChildAt(1)).setText (items.get(i).toString());
        }
        doScreenTransition();
    }

    /**
     * Clear the vehicle details screen
     */
    @Override
    public void clearVehicleDetailsView() {
        isDisplayDetails = false;
        doScreenTransition();
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.fetchVehicleList();
    }

    /**
     * Perform screen transition. Resizes the list and the details parts of the screen depending
     * on the parameters set in the vehicle_data.xml resources
     */
    private void doScreenTransition() {
        TransitionManager.beginDelayedTransition(vehicleFragmentLayout);
        if (isDisplayDetails) {
            vehicleConstraintSet.applyTo(vehicleFragmentLayout);
        } else {
            listConstraintSet.applyTo(vehicleFragmentLayout);
        }
    }

    /**
     * Add a single vehicle_details_row to the vehicle_details_row_bed, thus dynamically building the
     * vehicle details view
     */
    private void addDetailsRow() {
        final View vehicleDetailsRow;
        final LinearLayout vehicleDetailsBed= fragmentView.findViewById(R.id.vehicle_details_row_bed);
        // create a details row by inflating the vehicle_details_row layout into the vehicleDetailsBed layout
        vehicleDetailsRow = getLayoutInflater().inflate(R.layout.vehicle_details_row, vehicleDetailsBed, false);
        // Add the details row to the view
        vehicleDetailsBed.addView(vehicleDetailsRow);
    }
}
