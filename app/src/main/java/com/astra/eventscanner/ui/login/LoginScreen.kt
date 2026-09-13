package com.astra.eventscanner.ui.login

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.astra.eventscanner.BuildConfig
import com.astra.eventscanner.R
import com.astra.eventscanner.ui.components.NeoBrutalistButton
import com.astra.eventscanner.ui.theme.Black
import com.astra.eventscanner.ui.theme.Cream
import com.astra.eventscanner.ui.theme.PrimaryPurple
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import java.security.SecureRandom

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (loginSuccess) {
        onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Astra Logo",
            modifier = Modifier.size(120.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ASTRA EVENT SCANNER",
            color = Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(64.dp))

        if (error != null) {
            Text(
                text = error!!, 
                color = Color.Red, 
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            CircularProgressIndicator(color = PrimaryPurple)
        } else {
            NeoBrutalistButton(
                text = "SIGN IN WITH GOOGLE",
                onClick = {
                    val googleApiAvailability = com.google.android.gms.common.GoogleApiAvailability.getInstance()
                    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
                    
                    if (resultCode != com.google.android.gms.common.ConnectionResult.SUCCESS) {
                        if (googleApiAvailability.isUserResolvableError(resultCode)) {
                            viewModel.onError("UPDATE GOOGLE PLAY SERVICES")
                            googleApiAvailability.getErrorDialog(context as android.app.Activity, resultCode, 9000)?.show()
                        } else {
                            viewModel.onError("GOOGLE PLAY SERVICES NOT SUPPORTED")
                        }
                        return@NeoBrutalistButton
                    }

                    val credentialManager = CredentialManager.create(context)
                    
                    // Generate a nonce (required for backend validation & security)
                    val nonce = ByteArray(32).let {
                        SecureRandom().nextBytes(it)
                        Base64.encodeToString(it, Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING)
                    }

                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                        .setAutoSelectEnabled(false)
                        .setNonce(nonce)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    scope.launch {
                        try {
                            val result = credentialManager.getCredential(context, request)
                            val credential = result.credential
                            
                            when (credential) {
                                is GoogleIdTokenCredential -> {
                                    viewModel.onGoogleLogin(credential.idToken)
                                }
                                is CustomCredential -> {
                                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                        viewModel.onGoogleLogin(googleIdTokenCredential.idToken)
                                    } else {
                                        Log.e("Login", "Unexpected CustomCredential type: ${credential.type}")
                                        viewModel.onError("LOGIN FAILED: UNKNOWN TYPE")
                                    }
                                }
                                else -> {
                                    Log.e("Login", "Unexpected credential type: ${credential::class.java.name}")
                                    viewModel.onError("LOGIN FAILED: RETRY")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("Login", "Credential Manager Error", e)
                            val errorMessage = when {
                                e.message?.contains("no provider dependencies found") == true -> "SIGN IN TO GOOGLE ON DEVICE\nOR UPDATE PLAY SERVICES"
                                e.message?.contains("SERVICE_VERSION_UPDATE_REQUIRED") == true -> "UPDATE GOOGLE PLAY SERVICES"
                                e is androidx.credentials.exceptions.GetCredentialCancellationException -> "SIGN IN CANCELLED"
                                else -> "AUTH ERROR: ${e.message?.take(30) ?: "UNKNOWN"}"
                            }
                            viewModel.onError(errorMessage)
                        }
                    }
                },
                containerColor = PrimaryPurple,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "STAFF ACCESS REQUIRED",
            color = Black.copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
    }
}
