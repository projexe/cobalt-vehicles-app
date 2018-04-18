# cobalt-vehicles-app

Demo app written for Cobalt by Simon Hutton

IMPORTANT : PLEASE ENSURE YOU BUILD THE CORRECT VARIANT (prodDebug) FOR TESTING. TO CONFIRM YOU ARE RUNNING THE CORRECT BUILD VARIANT, SEE THE 'VERSION' DETAILS ON THE SETTINGS SCREEN.

Development notes : Consideration was given in the design phase to implementing seperate fragments for the Vehicle Details and the Vehicle List, but bearing in mind that the API does not accept parameters, the selected vehicle details must be obtained and displayed from data already obtained by the List functionality. 

Therefore if 2 fragments were implemented, the data would have to be passed back from the List fragment to the model via the presenter and then sent to a new Details fragment presenter to be displayed on that fragment.

This wouldn't fit my architecture pattern as there is no further API reads or business logic required to obtain the Vehicle details. Therefore both List and Details are handled by the single fragment. 

Note that the MOCK build uses a dummy API call and is used to test the model for API errors

As Retrofit is using an asynchronous call, there was no need to use RxJava for the asynchronous call

Crashlytics is included for crash reporting

LeakCanary is included for memory leak identification





