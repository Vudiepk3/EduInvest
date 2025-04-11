package com.example.eduinvest.firebase;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.eduinvest.R;
import com.example.eduinvest.constants.Constants;
import com.example.eduinvest.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBaseClass {
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();

    // Singleton pattern (optional)
    public static FireBaseClass getInstance() {
        return new FireBaseClass();
    }

    // Đăng ký người dùng mới vào Firestore
    public void registerUser(UserModel userInfo) {
        mFireStore.collection(Constants.USER_COLLECTION)
                .document(getCurrentUserId())
                .set(userInfo, SetOptions.merge());
    }

    // Lấy thông tin người dùng từ Firestore
    public static void getUserInfo(UserInfoCallback callback) {
        mFireStore.collection(Constants.USER_COLLECTION)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserModel userInfo = documentSnapshot.toObject(UserModel.class);
                    callback.onUserInfoFetched(userInfo);
                })
                .addOnFailureListener(e -> callback.onUserInfoFetched(null));
    }

    // Cập nhật hồ sơ người dùng
    public void updateProfile(String name, Uri imgUri) {
        String userId = getCurrentUserId();
        if (!name.isEmpty()) {
            mFireStore.collection(Constants.USER_COLLECTION)
                    .document(userId)
                    .update("name", name);
        }
        if (imgUri != null) {
            uploadImage(imgUri);
        }
    }

    // Đặt hình ảnh hồ sơ vào ImageView
    public void setProfileImage(String imageRef, ImageView view) {
        if (!imageRef.isEmpty()) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = storageRef.child(imageRef);
            final long ONE_MEGABYTE = 1024 * 1024;
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(byteArray -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                view.setImageBitmap(bmp);
            }).addOnFailureListener(e -> {
                Log.e("ImageLoad", "Failed to load image", e);
                // Set a default image if loading fails
                view.setImageResource(R.drawable.img_user); // replace with your default image
            });
        }
    }

    // Tải lên hình ảnh hồ sơ của người dùng
    private void uploadImage(Uri imgUri) {
        String userId = getCurrentUserId();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageRef.child("profile_pictures/" + userId);
        profilePicRef.putFile(imgUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mFireStore.collection(Constants.USER_COLLECTION)
                        .document(userId)
                        .update("image", "profile_pictures/" + userId);
            } else {
                Log.e("ImageUpload", "Unsuccessful");
            }
        });
    }

    // Lấy ID người dùng hiện tại
    public static String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }

    // Interface để callback thông tin người dùng
    public interface UserInfoCallback {
        void onUserInfoFetched(UserModel userInfo);
    }
}
