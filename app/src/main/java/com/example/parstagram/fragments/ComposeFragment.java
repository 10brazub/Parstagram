package com.example.parstagram.fragments;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.parstagram.MainActivity;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.ParseFile;
import com.parse.ParseUser;
import java.io.File;

public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 57;
    private ImageView ivPicture;
    private EditText etDescription;
    private Button btnCapture;
    private Button btnSubmit;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivPicture = view.findViewById(R.id.ivPicture);
        etDescription = view.findViewById(R.id.etDescription);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnCapture.setOnClickListener(v -> launchCamera());

        btnSubmit.setOnClickListener(v -> {
            String description = etDescription.getText().toString();

            if (description.isEmpty()) {
                Toast.makeText(getContext(), "Description cant be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile == null || ivPicture.getDrawable() == null) {
                Toast.makeText(getContext(), "No image provided", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseUser currentUser = ParseUser.getCurrentUser();
            savePost(description, currentUser, photoFile);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void launchCamera() {
        // creates Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Creates a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Starts the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP
                // Load the taken image into a preview
                ivPicture.setImageBitmap(takenImage);
            }
            else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setKeyDescription(description);
        post.setKeyImage(new ParseFile(photoFile));
        post.setKeyUser(currentUser);
        post.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error while saving", e);
                Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "Post was saved successfully");
            etDescription.setText("");
            ivPicture.setImageResource(0);
        });
    }
}