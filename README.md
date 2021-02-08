## Architecture
<img src="https://github.com/dev-juri/CardInfoFinder/blob/master/images/mvvm.png"/>

## Project Structure/Approach
- ui (contains the Activity)
     * Attached a TextWatcher object to the textfield, on the afterTextChanged method, i checked if the length of the input equals 16(length of card number), hence call the function to get the card details from the viewmodel, which requires a parameter of type Long.
     * Implemented several observers to LiveData in the viewModel to:
          -- Toggle the visibility of the progress bar depending on the state of data (whether LOADING, SUCCESS or ERROR)
          -- Update UI with the card details fetched
     * Launch Camera to snap picture
     * Used Firebase ML Kit to extract the text, the convert to Long and fill in to the textfield.
- viewmodel (contains the activity's view model)
- utils (contains all helpers and utility functions used in the app)
   * Result.kt (a sealed class use to get the state of the request, am request is wrapped with this class and it should output either Success with the data, Error with the error message or Loading)
   * Utils.kt (contains an extension function to map the NetworkResponse data which the retrofit request returns to the actual Domain data which will be displayed to the UI)
- repository
   * Repository.kt (an interface that contains an abstracted function to make requests... This makes the repository easy to test. The only function to return a return type of Result)
   * RepositoryImpl.kt (implements the Repository interface and overrides the functions, here is where the actual request is made and response is mapped to the domain model using the extension function in Utils.kt)
- network (contains all retrofit related code, from its instance to the service (an interface which contains the @GET function.)


