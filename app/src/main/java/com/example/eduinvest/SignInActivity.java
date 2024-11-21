package com.example.eduinvest;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> launcher;
    private EditText txtSignInEmail, txtSignInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

        // Khởi tạo EditText
        txtSignInEmail = findViewById(R.id.txtEmail);
        txtSignInPassword = findViewById(R.id.txtPassword);

        // Thiết lập Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        getActivities();
    }

    private void getActivities() {
        CardView signInButton = findViewById(R.id.btnSignIn);
        signInButton.setOnClickListener(v -> signInUser());

        TextView txtRegister = findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        CardView googleSignInButton = findViewById(R.id.cardSignInWithGoogle);
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
        CardView facebookSignInButton = findViewById(R.id.cardSignInWithFacebook);
        facebookSignInButton.setOnClickListener(v -> {
           Toast.makeText(this, "Tính năng này đang được phát triển", Toast.LENGTH_SHORT).show();
        });


        // Đăng ký launcher cho kết quả hoạt động
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleResults(task);
                    }
                }
        );
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }

    // Kiểm tra kết nối mạng
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }

    // Đăng nhập người dùng với email và mật khẩu
    private void signInUser() {
        if (isNetworkAvailable()) {
            Toast.makeText(this, "Vui lòng kiểm tra kết nối mạng của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = txtSignInEmail.getText().toString().trim();
        String password = txtSignInPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtSignInEmail.setError("Email Không Hợp Lệ");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txtSignInPassword.setError("Mật Khẩu Không Được Để Trống");
            return;
        }

        // Perform sign-in with Firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Sign-in failed
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Đăng Nhập Thất Bại";
                        Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        if (isNetworkAvailable()) {
            Toast.makeText(this, "Vui lòng kiểm tra kết nối mạng của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent signInIntent = googleSignInClient.getSignInIntent();
        launcher.launch(signInIntent);
    }

    private void handleResults(Task<GoogleSignInAccount> task) {
        if (task.isSuccessful()) {
            GoogleSignInAccount account = task.getResult();
            firebaseAuthWithGoogle(account);
        } else {
            // Google sign-in failed
            Toast.makeText(this, task.getException() != null ? task.getException().getMessage() : "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lấy tài khoản hiện tại
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Người dùng đã đăng nhập thành công
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        // Sign-in failed
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Đăng Nhập Thất Bại";
                        Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
