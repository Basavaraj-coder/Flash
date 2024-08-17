package com.grocery.flash.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.flash.Data.InternetApiItems
//import com.example.flash.network.FlashApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.grocery.flash.Data.InternetApiItems
import com.grocery.flash.network.FlashApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//class FlashViewModel:ViewModel() instead of this to access context we need to chnge constructor as
//here application is the base class for maintaining entire applications state
//(application: Application) : AndroidViewModel(application)
class FlashViewModel:ViewModel() {
    //THis is viewModel
    private val database = Firebase.database //for writing/saving into Fb realTimeDb

    //val myRef = database.getReference("Cart") here all the records were saving under same node
    // in real apps records should be stored under there unique Id ,So we need below code
    private val myRef = database.getReference("User/${firebaseAuth.currentUser?.uid}/cart")


    private val _user =
        MutableStateFlow<FirebaseUser?>(null) // null becoz no user has authenticated yet
    val user: StateFlow<FirebaseUser?> get() = _user.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phon_number: MutableStateFlow<String> get() = _phoneNumber

    private val _uiState = MutableStateFlow(FlashUiState())
    val uiState: StateFlow<FlashUiState> = _uiState.asStateFlow()

    private val _isVisible = MutableStateFlow<Boolean>(true) //
    val isVisible = _isVisible

    private lateinit var downloadDatafromInternetJob: Job
    private var showFlashScreenJob: Job

    var itemUiState: ItemUiState by mutableStateOf(ItemUiState.loading) //for recomposition
        private set

    private val _cartItem = MutableStateFlow<List<InternetApiItems>>(emptyList()) //list of items-

    // - that will add to cart, now to access it we need readOnly variable
    val cartItem: StateFlow<List<InternetApiItems>> = _cartItem.asStateFlow()

    //val cartItem:StateFlow<List<InternetApiItems>> get() = _cartItem.asSateFlow()
//    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("Cart")
//
//    //but here rightnow in viewModel we connect directly access Context
//    //inorder to access the context of app, need to change the constructor of class
//    private val context = application.applicationContext // here now we can use context directly
//    private val cartItemsKey = stringPreferencesKey("cart_items")

    private val _otp = MutableStateFlow("")
    val otp: MutableStateFlow<String> get() = _otp

    private val _verificationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String> get() = _verificationId


    //myRef.setValue("Hello, World!")

    fun setVerificationId(verificationId: String) {
        _verificationId.value = verificationId
    }

    fun setOtp(otp: String) {
        try {
            if (otp.length > 6) {
                throw Exception("only otp 6 digits accepted, error in flashViewModel file fun setOtp()")
            } else {
                _otp.value = otp
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    fun setUserCred(userCred: FirebaseUser) { //for saving users unique credentials after otp verification
        _user.value = userCred
    }

    fun clearUserCred() {
        _user.value = null
        _phoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""
    }

    //    fun updateClickText(updatedText : String){
//        _uiState.update {
//            it.copy(
//                clickStatus = updatedText
//            )
//        }
//    }
    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }
//
//    private suspend fun saveCartItemToDataStore() {
//        context.datastore.edit { preferences ->
//            preferences[cartItemsKey] = Json.encodeToString(_cartItem.value)
//        }
//    }
//
//    private suspend fun loadCartItemsFromDataStore() {
//        val fulldata = context.datastore.data.first()
//        val cartItemJson = fulldata[cartItemsKey]
//        if (!cartItemJson.isNullOrBlank()) {
//            _cartItem.value = Json.decodeFromString(cartItemJson)
//        }
//    }

    fun addToCart(item: InternetApiItems) {
        _cartItem.value = _cartItem.value + item
    }

    fun removeFromCart(Olditem: InternetApiItems) {
//        _cartItem.value = _cartItem.value - item
//        viewModelScope.launch {
//            saveCartItemToDataStore()
//        //above code was to remove from DataStore persistence
//
//        }
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    var itemRemoved = false
                    val item = childSnapshot.getValue(InternetApiItems::class.java)

                    item?.let {
                        if (Olditem.itemCategory == it.itemCategory &&
                            Olditem.itemImage == it.itemImage &&
                            Olditem.itemName == it.itemName &&
                            Olditem.itemPrice == it.itemPrice &&
                            Olditem.itemQty == it.itemQty
                        ){
                            childSnapshot.ref.removeValue() // this will remove a specific value from
                        //-- Db after condition check
                            itemRemoved = true
                        }
                    }
                    if(itemRemoved == true){
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }

    fun addToFirebase(item: InternetApiItems) {
        myRef.push().setValue(item) // now here push() it assigns some unique key to each record
    }

    private fun fillCartItems() {
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                _cartItem.value = emptyList()
                for (childSnapshot in dataSnapshot.children) {
                    val item = childSnapshot.getValue(InternetApiItems::class.java)
                    item?.let {
                        val newItem = it
                        addToCart(newItem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    sealed interface ItemUiState {
        data class Success(val item: List<InternetApiItems>) : ItemUiState
        data object loading : ItemUiState
        data object error : ItemUiState
    }

    fun updateSelectedCategory(updatedCategory: Int) {
        _uiState.update {
            it.copy(
                selectedCategory = updatedCategory
            )
        }
    }

    private suspend fun toggelVisibility() {
        _isVisible.value = false
    }

    init {
        this.showFlashScreenJob = viewModelScope.launch(Dispatchers.Default) {
            //this block of code launches coroutine using viewModelScope
            //now successfully implemented coroutine scope
            delay(3000)
            //here screen pauses for 3000ms without blocking main thread
            // this is used for downloading data/loading data
            toggelVisibility()

            //example of this is splash screen
        }
        getFlashItems() // while showing splash screen at same time downloading items from api
        //using different coroutine

        //items from Db gets loaded while app is started
        fillCartItems()
    }

    fun getFlashItems() { //to display the data items from api after clicking on categories
        try {

            downloadDatafromInternetJob = viewModelScope.launch(Dispatchers.IO) {
                try {
                    val listItems = FlashApi.retrofitService.getItems()
                    itemUiState = ItemUiState.Success(listItems) // all downloaded data from api
                    //loadCartItemsFromDataStore() //loads data from dataStore
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    itemUiState = ItemUiState.error
                    toggelVisibility() // need to set the value as false of _isvisible,
                    // -- otherwise screen will stuck
                    showFlashScreenJob.cancel()
                }
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    //now loaded 2 coroutines here one for loading splash screen and other for getting items
// from server

}

/*
notes
viewModel with backing property = UIState
collectAsState = recompostion
UDF = reduce code complexity
 */