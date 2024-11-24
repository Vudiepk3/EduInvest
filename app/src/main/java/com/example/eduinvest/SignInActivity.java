package com.example.eduinvest;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth; // Khởi tạo Firebase Authentication để xử lý đăng nhập
    private GoogleSignInClient googleSignInClient; // Khởi tạo GoogleSignInClient để xử lý đăng nhập bằng Google
    private ActivityResultLauncher<Intent> launcher; // Dùng để nhận kết quả từ Google Sign-In
    private EditText txtSignInEmail, txtSignInPassword; // EditText dùng để nhập email và mật khẩu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Khởi tạo Firebase Authentication
        auth = FirebaseAuth.getInstance();
        txtSignInEmail = findViewById(R.id.txtEmail); // Gán EditText email
        txtSignInPassword = findViewById(R.id.txtPassword); // Gán EditText mật khẩu

        // Cấu hình Google SignIn Client với các yêu cầu cần thiết
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // ID ứng dụng OAuth 2.0 của bạn
                .requestEmail() // Yêu cầu email của người dùng
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso); // Tạo đối tượng GoogleSignInClient

        setupListeners(); // Gọi hàm thiết lập sự kiện cho các nút bấm
    }

    // Thiết lập các sự kiện cho các nút bấm
    private void setupListeners() {
        findViewById(R.id.btnSignIn).setOnClickListener(v -> signInUser()); // Xử lý sự kiện đăng nhập bằng email
        findViewById(R.id.cardSignInWithGoogle).setOnClickListener(v -> signInWithGoogle()); // Xử lý sự kiện đăng nhập bằng Google

        // Khởi tạo ActivityResultLauncher để nhận kết quả từ Google Sign-In
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleResults(task); // Xử lý kết quả từ Google Sign-In
            }
        });
    }

    // Kiểm tra kết nối mạng của thiết bị
    private boolean isNetworkUnavailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return capabilities == null || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    // Xử lý đăng nhập bằng email và mật khẩu
    private void signInUser() {
        if (isNetworkUnavailable()) {
            Toast.makeText(this, "Vui lòng kiểm tra kết nối mạng của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy email và mật khẩu từ các ô nhập liệu
        String email = txtSignInEmail.getText().toString().trim();
        String password = txtSignInPassword.getText().toString().trim();

        // Kiểm tra định dạng email
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtSignInEmail.setError("Email Không Hợp Lệ");
            return;
        }

        // Kiểm tra mật khẩu có bị bỏ trống không
        if (TextUtils.isEmpty(password)) {
            txtSignInPassword.setError("Mật Khẩu Không Được Để Trống");
            return;
        }

        // Thực hiện đăng nhập bằng email và mật khẩu
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Nếu đăng nhập thành công, chuyển sang màn hình chính
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish(); // Kết thúc Activity hiện tại
                    } else {
                        // Nếu đăng nhập thất bại, hiển thị thông báo lỗi
                        Toast.makeText(this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Xử lý đăng nhập bằng Google
    private void signInWithGoogle() {
        if (isNetworkUnavailable()) {
            Toast.makeText(this, "Vui lòng kiểm tra kết nối mạng của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khởi tạo intent đăng nhập Google
        Intent signInIntent = googleSignInClient.getSignInIntent();
        launcher.launch(signInIntent); // Chạy intent để đăng nhập bằng Google
    }

    // Xử lý kết quả trả về từ Google Sign-In
    private void handleResults(Task<GoogleSignInAccount> task) {
        if (task.isSuccessful()) {
            // Nếu đăng nhập Google thành công, thực hiện đăng nhập với Firebase
            GoogleSignInAccount account = task.getResult();
            firebaseAuthWithGoogle(account);
        } else {
            // Nếu đăng nhập Google thất bại, hiển thị thông báo lỗi
            Toast.makeText(this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
        }
    }

    // Đăng nhập với Firebase sử dụng tài khoản Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null); // Tạo credential từ Google
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Nếu đăng nhập Firebase thành công, chuyển sang màn hình chính
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish(); // Kết thúc Activity hiện tại
                    } else {
                        // Nếu đăng nhập Firebase thất bại, hiển thị thông báo lỗi
                        Toast.makeText(this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
