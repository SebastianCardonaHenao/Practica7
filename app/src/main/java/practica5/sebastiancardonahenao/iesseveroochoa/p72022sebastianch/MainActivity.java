package practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comprobarAutenticacion();
        bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(e -> startSignIn());

    }

    private void comprobarAutenticacion() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Primero, verificamos la existencia de una sesión.
        if (auth.getCurrentUser() != null) {
            finish();// Cerramos la actividad.
            // Abrimos la actividad que contiene el inicio de la funcionalidad de la app.
            startActivity(new Intent(this, InicioAppActivity.class));
        } else {//en otro caso iniciamos FirebaseUI
            startSignIn();
        }
    }


// ...

    // Iniciamos la actividad de inicio de sesión
    private void startSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_launcher_foreground)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(true)
                .build();
        signInLauncher.launch(signInIntent);
    }



    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            (result) -> {
                onSignInResult(result);
            });

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            startActivity(new Intent(this, InicioAppActivity.class));
            // Successfully signed in
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e("ERROR DESCONOCIDO", "Sign-in error: ", response.getError());
        }
    }

    private void showSnackbar (int texto){
        Snackbar.make(findViewById(android.R.id.content).getRootView(), getString(texto), Snackbar.LENGTH_LONG).show();
    }

}