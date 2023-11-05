package com.game.nghiphep;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.game.nghiphep.databinding.LoginBinding;
import com.game.nghiphep.databinding.RegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    RegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterBinding.inflate(getLayoutInflater());
        binding.register.setOnClickListener(view -> {
            if (checkNull() == true) {

                registerUser(binding.email.getText().toString(), binding.password.getText().toString(), binding.msnv.getText().toString());
            }


        });
        binding.login.setOnClickListener(view -> {
            finish();
        });
        setContentView(binding.getRoot());
    }

    private boolean checkNull() {
        String email = binding.email.getText().toString();
        if (binding.msnv.getText().length() > 0 && email.length() > 0 && binding.password.getText().length() > 0) {
            EmailValidator validator = new EmailValidator();
            if (validator.validate(email)) {
                return true;
            } else {
                Toast.makeText(this, "Vui lòng nhập đúng định dạng email!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Vui long khong duoc de trong", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void registerUser(String email, String password, String msnv) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionName = "nhanvien";
        String documentId = msnv;

        DocumentReference documentRef = db.collection(collectionName).document(documentId);
        documentRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String docemail = document.getString("email");
                            if (docemail == null) {
                                putEmail(db, email, password, msnv);
                            } else {
                                if (docemail.length() > 0) {
                                    Toast.makeText(this, docemail, Toast.LENGTH_SHORT).show();
                                } else {
                                    putEmail(db, email, password, msnv);
                                }
                            }

                            // Tài liệu tồn tại trong collection
                            // Thực hiện các hành động tương ứng
                        } else {
                            Toast.makeText(this, "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();


                            // Tài liệu không tồn tại trong collection
                            // Thực hiện các hành động tương ứng
                        }
                    } else {
                        Toast.makeText(this, "Lỗi bất định, kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();

                        // Xảy ra lỗi khi kiểm tra tài liệu
                    }
                });

    }

    private void putEmail(FirebaseFirestore db, String email, String password, String msnv) {

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            Map<String, Object> additionalFields = new HashMap<>();
                            additionalFields.put("email", email);
                            // Thêm các trường dữ liệu tùy chỉnh vào tài liệu người dùng trong Firestore
                            db.collection("nhanvien").document(msnv)
                                    .set(additionalFields, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                        // Đăng ký thành công và thêm dữ liệu tùy chỉnh thành công
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xảy ra lỗi khi thêm dữ liệu tùy chỉnh
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(RegisterActivity.this, "Email đã sử dụng!", Toast.LENGTH_SHORT).show();

                                // Xử lý khi địa chỉ email đã được sử dụng bởi tài khoản khác
                                // ...
                            } catch (Exception e) {
                                // Xử lý các ngoại lệ khác
                                // ...
                                Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
    }
}