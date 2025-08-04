package app.jetpack.jetpackfinal.ui.home.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.AuthUiState
import app.jetpack.jetpackfinal.ui.home.ui.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import app.jetpack.jetpackfinal.R
import app.jetpack.jetpackfinal.utils.GOOGLE_APIKEY


@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("GoogleSignIn", "Result: ${result.resultCode}")

        if (result.resultCode == Activity.RESULT_OK) {
            GoogleSignIn.getSignedInAccountFromIntent(result.data).let { task ->
                try {
                    task.result.idToken?.let { token ->
                        Log.e("Token", "$token")
                        viewModel.handleCompleteSignInFlow(token)
                    }
                    if(task.result.idToken==null){
                        Log.e("Token","Null")
                    } else {

                    }

                } catch (e: Exception) {
                    Log.e("Error IDTOKEN", "${e.message}")
                }
            }
        }
        else{
            Log.e("Task Token", "${result}")
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = uiState.value) {
                is AuthUiState.Loading -> CircularProgressIndicator()
                is AuthUiState.Success -> {
//                    LaunchedEffect(Unit) {
//                        navController.navigate("home_screen") {
//                            popUpTo("login_screen") { inclusive = true }
//                        }
//                    }
                    Text("Success", fontSize = 34.sp)
                }
                is AuthUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomGoogleSignInButton(
                        onClick = { startGoogleSignIn(context, launcher) }
                    )
                }
                is AuthUiState.Initial ->{
                    CustomGoogleSignInButton(
                        onClick = { startGoogleSignIn(context, launcher) }
                    )
                }
                else -> {
                    Text("Error State")
                }
            }
        }
    }
}

private fun startGoogleSignIn(
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(GOOGLE_APIKEY)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
}

@Composable
fun CustomGoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google), // Add Google icon to drawable
                contentDescription = "Google Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign in with Google")
        }
    }
}