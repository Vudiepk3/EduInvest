package com.example.eduinvest.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.example.eduinvest.R;
import com.example.eduinvest.constants.Base;
import com.example.eduinvest.firebase.FireBaseClass;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditProfileAdapter extends BottomSheetDialogFragment {

    private final Context context;
    private ImageView imageView;
    private Uri imageUri = null;
    private String name;
    private EditText editName;
    private final FireBaseClass fireBaseClass;

    public static final int PICK_IMAGE_REQUEST = 1;

    public EditProfileAdapter(Context context) {
        this.context = context;
        this.fireBaseClass = new FireBaseClass(); // Tạo instance của FireBaseClass
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_profile, container, false);

        imageView = view.findViewById(R.id.editImage);
        editName = view.findViewById(R.id.etUserName);
        CardView saveButton = view.findViewById(R.id.btnSave);

        // Set profile image from Firebase
        fireBaseClass.setProfileImage("profile_pictures/" + FireBaseClass.getCurrentUserId(), imageView);

        // When the user clicks the image, open the gallery
        imageView.setOnClickListener(v -> openGallery());

        // When the user clicks the save button, update name and profile image on Firebase
        saveButton.setOnClickListener(v -> {
            name = editName.getText().toString();
            fireBaseClass.updateProfile(name, imageUri);
            Base.showToast(context, "Đã Cập Nhật Thông Tin Thành Công");
            dismiss();
        });

        return view;
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Lấy URI của hình ảnh đã chọn
            imageUri = data.getData();
            if (imageUri != null) {
                imageView.setImageURI(imageUri);
            }
        }
    }
}
