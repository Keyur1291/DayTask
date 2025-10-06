package com.example.daytask.presentation.authentication.domain

import android.content.Context
import android.widget.Toast
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.daytask.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleAuth {

    companion object {
        fun googleSignIn(
            context: Context,
            scope: CoroutineScope,
            login: (AuthCredential) -> Unit
        ) {

            val credentialManager = CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()

            scope.launch {
                try {

                    val result = credentialManager.getCredential(context, request)

                    when(result.credential) {

                        is CustomCredential -> {
                            if(result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
                                val user = Firebase.auth.signInWithCredential(authCredential).await().user
                                user?.let {
                                    login(authCredential)
                                }
                            }
                        }
                    }

                } catch (e: NoCredentialException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                } catch (e: GetCredentialException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun getCredentialOptions(context: Context): CredentialOption {

            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()
        }
    }
}