package com.example.composesampleapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.composesampleapp.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val USERS = "users"

@HiltViewModel
class IGViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    fun onSignUp(userName: String, email: String, pass: String) {
        inProgress.value = true

        db.collection(USERS).whereEqualTo("username", userName).get()
            .addOnSuccessListener { documents ->
                if(documents.size() > 0){
                   handleException(customMessage = "User Name Already Exists")
                   inProgress.value = false
                }else {
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                signedIn.value = true
                            }else {
                                handleException(task.exception)
                            }
                            inProgress.value = false
                        }
                }
            }
            .addOnFailureListener {
                
            }

    }

    fun handleException(exception: Exception? = null, customMessage: String? = "") {

    }

}