package com.myhexaville.tictactoe;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.myhexaville.tictactoe.databinding.ActivityStartBinding;
import com.myhexaville.tictactoe.model.User;
import com.myhexaville.tictactoe.wifi.UserListActivity;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.myhexaville.tictactoe.Util.getCurrentUserId;
import static com.myhexaville.tictactoe.Util.savePushToken;

public class StartActivity extends AppCompatActivity {
    private static final String LOG_TAG = "StartActivity";
    private ActivityStartBinding binding;
    private boolean logginIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
    }

    public void startSingleMode(View view) {
        startActivity(new Intent(this, MainActivity.class)
                .putExtra("type", "single"));
    }

    public void startMultilayer(View view) {
        if (!arePlayServicesOk()) {
            return;
        }
        if (isAnonymous()) {
            binding.inputEmail.setVisibility(VISIBLE);
            binding.inputName.setVisibility(VISIBLE);
            binding.login.setVisibility(VISIBLE);
            binding.inputPassword.setVisibility(VISIBLE);
        } else {
//            startActivity(new Intent(this, MainActivity.class)
//                    .putExtra("type", "wifi"));

            startActivity(new Intent(this, UserListActivity.class));
        }
    }

    public void loginWithEmail(View view) {
        String email = binding.inputEmail.getText().toString();
        String name = binding.inputName.getText().toString();
        String password = binding.inputPassword.getText().toString();
        if (logginIn) {
            return;
        }
        if (isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter correct email", LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(name)) {
            Toast.makeText(this, "Enter correct name", LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password should have at least 6 characters", LENGTH_SHORT).show();
            return;
        }

        logginIn = true;
        showProgressDialog();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "loginWithEmail: ");
                        String uid = auth.getCurrentUser().getUid();

                        User user = new User(name);
                        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                .setValue(user);

                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        savePushToken(refreshedToken, uid);

//                        startActivity(new Intent(this, MainActivity.class)
//                                .putExtra("type", "wifi"));

                        startActivity(new Intent(this, UserListActivity.class));

                    } else {
                        Log.d(LOG_TAG, "loginWithEmail: unsuccessful");
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task1 -> {
                                    if (!isAnonymous()) {
                                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                                        savePushToken(refreshedToken, getCurrentUserId());

//                                        startActivity(new Intent(this, MainActivity.class)
//                                                .putExtra("type", "wifi"));

                                        startActivity(new Intent(this, UserListActivity.class));

                                    }
                                });
                    }
                });
    }

    private void showProgressDialog() {
        binding.progress.setVisibility(VISIBLE);
    }

    private void hideProgressDialog() {
        binding.progress.setVisibility(GONE);
    }

    private boolean isAnonymous() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null || currentUser.isAnonymous();
    }

    private boolean arePlayServicesOk() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int resultCode = googleAPI.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode, 5000).show();
            }
            return false;
        }

        return true;
    }
}
