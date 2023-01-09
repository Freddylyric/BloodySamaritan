package ke.co.freddylyric.bloodysamaritan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipientRegistrationActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextInputEditText registerFulName, registerIdNumber, registerPhoneNumber, registerEmail, registerPassword;
    private Spinner bloodGroupSpinner, regionSpinner;
    private Button registerButton;
    private Uri resultUri;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_registration);


        TextView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipientRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        profile_image = findViewById(R.id.profile_image);
        registerFulName = findViewById(R.id.registerFulName);
        registerIdNumber = findViewById(R.id.registerIdNumber);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        registerButton = findViewById(R.id.registerButton);
        regionSpinner = findViewById(R.id.regionSpinner);
        loader = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();

        //pick profile image from gallery
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verify input email and remove white spaces
                final String email = registerEmail.getText().toString().trim();
                //verify password
                final String password =registerPassword.getText().toString().trim();
                final String fullName = registerFulName.getText().toString().trim();
                final String idNumber = registerIdNumber.getText().toString().trim();
                final String phoneNumber = registerPhoneNumber.getText().toString().trim();
                final String bloodGroup = bloodGroupSpinner.getSelectedItem().toString();
                final String region = regionSpinner.getSelectedItem().toString();

                //check to ensure values are not empty
                if (TextUtils.isEmpty(email)){
                    registerEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    registerPassword.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(fullName)){
                    registerFulName.setError("FullName is required");
                    return;
                }
                if (TextUtils.isEmpty(idNumber)){
                    registerIdNumber.setError("ID is required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    registerPhoneNumber.setError("Phone is required");
                    return;
                }
                if (bloodGroup.equals("Select your Blood Group")){
                    Toast.makeText(RecipientRegistrationActivity.this, "Select Blood Group", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (region.equals("Select your Region")){
                    Toast.makeText(RecipientRegistrationActivity.this, "Select Region", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    loader.setMessage("Registering you...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RecipientRegistrationActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users")
                                        .child(currentUserId);


                                //creating hash keys
                                HashMap<String, Object> userInfo = new HashMap<String, Object>();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", fullName);
                                userInfo.put("email", email);
                                userInfo.put("idnumber", idNumber);
                                userInfo.put("phoneNumber", phoneNumber);
                                userInfo.put("bloodGroup", bloodGroup);
                                userInfo.put("region", region);
                                userInfo.put("type", "recipient");
                                userInfo.put("search", "recipient" + bloodGroup);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener((OnCompleteListener<Void>) task1 -> {
                                    if (task1.isSuccessful()){
                                        Toast.makeText(RecipientRegistrationActivity.this, "Information Received Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RecipientRegistrationActivity.this, Objects.requireNonNull(task1.getException()).toString(), Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                    //loader.dismiss();
                                });


                                //save profile image to database
                                if (resultUri !=null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile images").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    } catch (IOException e){
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    assert bitmap != null;
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RecipientRegistrationActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference()
                                                    !=null){
                                                Task <Uri> result =taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map<String, Object> newImageMap = new HashMap<>();
                                                        newImageMap.put("profilepictureurl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener((OnCompleteListener<Void>) task12 -> {
                                                            if (task12.isSuccessful()){
                                                                Toast.makeText(RecipientRegistrationActivity.this, "Image URL added to database successfully", Toast.LENGTH_SHORT).show();
                                                            }else{
                                                                Toast.makeText(RecipientRegistrationActivity.this, Objects.requireNonNull(task12.getException()).toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });


                                    Intent intent = new Intent(RecipientRegistrationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }

                            }
                        }
                    });


                }
            }
        });


    }
    //set the profile picture picked from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data !=null){
            resultUri = data.getData();
            profile_image.setImageURI(resultUri);
        }
    }
}

