package com.game.nghiphep;

import static android.content.ContentValues.TAG;

import static com.game.nghiphep.LoginActivity.ID;
import static com.game.nghiphep.LoginActivity.key_user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.game.nghiphep.databinding.HomeActivityBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    HomeActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = HomeActivityBinding.inflate(getLayoutInflater());


        if (key_user == 0) {
            replaceFragment(new DangKyNghiFragment(HomeActivity.this));
            getNoti();

        } else {
            replaceFragment(new HomeFragment(HomeActivity.this));
            getNotiSep();


        }
        binding.btnavi.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.home:
                    if (key_user == 0) {
                        replaceFragment(new DangKyNghiFragment(HomeActivity.this));
                    } else {
                        replaceFragment(new HomeFragment(HomeActivity.this));

                    }
                    break;

                case R.id.person:
                    replaceFragment(new PersonFragment());
                    break;

            }
            return true;

        });
        setContentView(binding.getRoot());
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }


    public void enableProgressbar(){
        binding.progress.setVisibility(View.VISIBLE);
    }
    public void hideProgressbar()
    {
        binding.progress.setVisibility(View.GONE);

    }

    public void getNoti(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("donxinnghi");

        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Lỗi khi lắng nghe thay đổi", error);
                    return;
                }

                for (DocumentChange dc : snapshot.getDocumentChanges()) {
                    DocumentSnapshot document = dc.getDocument();
                    String msnv1 =document.getString("msnv1");
                    if(msnv1!=null)
                    {   if(msnv1.equals(ID)) {
                        replaceFragment(new DangKyNghiFragment(HomeActivity.this));
                    }
                        }
                    switch (dc.getType()) {
                        case ADDED:
                            // Xử lý khi có document mới được thêm vào collection
                            String documentId = document.getId();
                            // ...
                            break;
                        case MODIFIED:
                            // Xử lý khi có document trong collection bị chỉnh sửa
                            String modifiedDocumentId = document.getId();

                            // ...
                            break;
                        case REMOVED:
                            // Xử lý khi có document trong collection bị xóa
                            String removedDocumentId = document.getId();

                            // ...
                            break;
                    }
                }
            }
        });

    }
    public void getNotiSep(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("donxinnghi");

        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Lỗi khi lắng nghe thay đổi", error);
                    return;
                }
                FragmentManager fragmentManager = getSupportFragmentManager(); // Nếu bạn đang sử dụng androidx.appcompat.app.AppCompatActivity
// hoặc
                FrameLayout fragmentContainer = findViewById(R.id.frame_layout);
                Fragment currentFragment = fragmentManager.findFragmentById(fragmentContainer.getId());

                if (currentFragment instanceof HomeFragment) {
                    // Fragment hiện tại là YourFragmentClass
                    replaceFragment(new HomeFragment(HomeActivity.this));
                } else if (currentFragment instanceof DuyetDonFragment) {
                    // Fragment hiện tại là AnotherFragmentClass
                } else {
                    // Không có Fragment nào hiện tại được hiển thị trong FrameLayout
                }

                for (DocumentChange dc : snapshot.getDocumentChanges()) {
                    DocumentSnapshot document = dc.getDocument();
                    String msnv1 =document.getString("msnv1");
                    if(msnv1!=null)
                    {   if(msnv1.equals(ID)) {
                    }
                    }
                    switch (dc.getType()) {
                        case ADDED:
                            // Xử lý khi có document mới được thêm vào collection
                            String documentId = document.getId();
                            // ...
                            break;
                        case MODIFIED:
                            // Xử lý khi có document trong collection bị chỉnh sửa
                            String modifiedDocumentId = document.getId();

                            // ...
                            break;
                        case REMOVED:
                            // Xử lý khi có document trong collection bị xóa
                            String removedDocumentId = document.getId();

                            // ...
                            break;
                    }
                }
            }
        });

    }





}