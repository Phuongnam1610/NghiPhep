package com.game.nghiphep;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.game.nghiphep.databinding.HomePageBinding;
import com.game.nghiphep.databinding.LoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    public static int key_user;
    public static String ID;
    LoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key_user = getIntent().getIntExtra("KEY_USER",0);
        binding = LoginBinding.inflate(getLayoutInflater());
        binding.register.setOnClickListener(view -> {
        startActivity(new Intent(this,RegisterActivity.class));
        });
        binding.btndangnhap.setOnClickListener(view -> {
            binding.btndangnhap.setVisibility(View.INVISIBLE);
            if(checkNull()==true){

                Login(binding.email.getText().toString(),binding.password.getText().toString(),key_user);

            }

        });
        if(key_user==1){
            binding.register.setVisibility(View.INVISIBLE);
        }
        binding.register.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        setContentView(binding.getRoot());
    }


    private boolean checkNull()
    {
        if(binding.email.getText().length()>0&&binding.password.getText().length()>0)
        {
            return true;
        }
        else
        {
            Toast.makeText(this, "khong duoc de trong email va password", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void Login(String email, String pass,int key_user)
    {
        FirebaseAuth mAuth;
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        getIDbyEmail(email, new Callback<String>() {
                            @Override
                            public void onResult(String result) {

                                if(result!=null){
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                ID=result;
                                startActivity(intent);
                                finish();}
                                else
                                {
                                    binding.btndangnhap.setVisibility(View.VISIBLE);

                                    Toast.makeText(LoginActivity.this, "Email không xác định!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },key_user);

                    } else {
                        binding.btndangnhap.setVisibility(View.VISIBLE);

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                });


    }
    public void getIDbyEmail(String email, final Callback<String> callback, int key_user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Tham chiếu đến collection bạn muốn truy vấn
        CollectionReference collectionRef;
        if(key_user==0){
            collectionRef  = db.collection("nhanvien");
        }
        else {
             collectionRef = db.collection("sep");
        }
        // Tạo truy vấn để lọc các tài liệu theo trường email
        Query query = collectionRef.whereEqualTo("email", email);
        // Thực hiện truy vấn
        query.get().addOnCompleteListener(task -> {
            String id = null; // Biến cuối cùng mới
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Lấy ra tên của tài liệu
                        id = document.getId();
                        // Sử dụng tên tài liệu theo nhu cầu của bạn
                        // ...
                        break;
                    }
                }
            } else {
                // Xử lý khi truy vấn thất bại
                // ...
            }

            // Gọi callback và truyền giá trị msnv
            callback.onResult(id);
        });
    }

    // Định nghĩa interface Callback để nhận kết quả từ phương thức getMSNVbyEmail
    public interface Callback<T> {
        void onResult(T result);
    }
}