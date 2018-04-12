package com.projexe.cobaltvehiclesapp;

import com.projexe.cobaltvehiclesapp.model.IModelContract;
import com.projexe.cobaltvehiclesapp.model.api.schema.Vehicle;
import com.projexe.cobaltvehiclesapp.vehiclelist.IVehicleContract;
import com.projexe.cobaltvehiclesapp.vehiclelist.VehicleListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for the implementation of {@link VehicleListPresenter}.
 * @author Simon Hutton
 * @version 1.0
 */
public class VehicleListPresenterTest {

    private VehicleListPresenter mVehicleListPresenter;

    @Mock
    private IModelContract mModelInterface;
    @Mock
    private IVehicleContract.View mViewInterface;
    @Mock
    private List<Vehicle> mVehicleData;
    @Mock
    private List<List> mDisplayList;

    // Create argument captors for callbacks
    @Captor
    private ArgumentCaptor<IModelContract.IApiResponse> mModelAPICallbackCaptor;

    @Before
    public void setupVehiclesListPresenter() {
        // inject annotated mocks
        MockitoAnnotations.initMocks(this);
        // Get a reference to the test class with the mock interfaces
        mVehicleListPresenter = new VehicleListPresenter(mViewInterface, mModelInterface);
    }

    @Test
    public void fetchVehiclesFromAPI_displayVehicles() {
        String message = "Anyold message";
        // Ask to fetch Vehicles from API
        mVehicleListPresenter.fetchVehicleList();
        // verify that any of the ImodelContract.IApiResponse interface methods is called
        Mockito.verify(mModelInterface).fetchVehiclesFromApi(any(IModelContract.IApiResponse.class));
        // Capture the method callse
        Mockito.verify(mModelInterface).fetchVehiclesFromApi(mModelAPICallbackCaptor.capture());

        System.out.println("1st print " + mModelAPICallbackCaptor.toString());
        System.out.println("2nd print " + mModelAPICallbackCaptor.getValue());

        //TEST THE 4 AVAILABLE CALLBACKS
        mModelAPICallbackCaptor.getValue().onApiEmptyData(message);
        // Ask view to display toast
        Mockito.verify(mViewInterface).displayToast(message);

        // Successful API read
        mModelAPICallbackCaptor.getValue().onApiSuccessReceived(mDisplayList);
        // Ask view to display empty dataset
        Mockito.verify(mViewInterface).displayVehicleList(mDisplayList);

//        // Receive failure from Model
//        mModelAPICallbackCaptor.getValue().onApiFailureReceived(5, message);
//        // sent failure to View
//        Mockito.verify(mViewInterface).displayToast(message);

        // Receive failure from Model
        mModelAPICallbackCaptor.getValue().onApiOfflinePersistedData(mDisplayList);
        // Display persisted data
        Mockito.verify(mViewInterface).displayVehicleList(mDisplayList);
    }

    @Test
    public void receivedEmptyData_displayEmptyDataMessage() {
        // When the presenter has received an empty dataset
        String messageToDisplay = "Any response message";
        mVehicleListPresenter.onApiEmptyData(messageToDisplay);
        // Ask view to display toast
        verify(mViewInterface).displayToast(messageToDisplay);
    }
    @Test

    public void receivedValidData_displayVehicleList() {
        // When the presenter has received an empty dataset
        List<List> displayData = new ArrayList<List>();
        mVehicleListPresenter.onApiSuccessReceived(displayData);
        // Ask view to display empty dataset
        verify(mViewInterface).displayVehicleList(displayData);
    }


    @Test
    public void receivedFailureFromAPI_DisplayFailMessage() {
        String message ="any message";
        // Receive failure from Model
        mVehicleListPresenter.onApiFailureReceived(5, message);
        // sent failure to View
        verify(mViewInterface).displayToast(message);
    }

    @Test
    public void receivedOfflineFailureFromAPI_DisplayPersistedData() {
        // Receive failure from Model
        mVehicleListPresenter.onApiOfflinePersistedData(mDisplayList);
        // Display persisted data
        //verify(mViewInterface).displayVehicleList(mDisplayList);
    }
}
