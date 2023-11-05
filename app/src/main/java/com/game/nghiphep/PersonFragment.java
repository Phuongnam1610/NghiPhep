package com.game.nghiphep;

import static com.game.nghiphep.LoginActivity.ID;
import static com.game.nghiphep.LoginActivity.key_user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.game.nghiphep.databinding.FragmentHomeBinding;
import com.game.nghiphep.databinding.FragmentPersonBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Person.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPersonBinding binding = FragmentPersonBinding.inflate(inflater, container, false);
        binding.btndangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class ));
                getActivity().finish();
            }
        });
        getInfo(new FirestoreCallback() {
            @Override
            public void onCallback(Map<String, Object> data) {
                String name=(String) data.get("hoten");
                String chucvu = (String) data.get("chucvu");
                String lienhe = (String) data.get("lienhe");
                String ngaysinh="";
                String ngayvaolam = "";
                Timestamp tstampns = (Timestamp) data.get("ngaysinh");
                if (tstampns != null) {
// Chuyển đổi Timestamp thành Date
                    Date date = tstampns.toDate();

// Định dạng Date thành chuỗi theo ý muốn
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String dateString = sdf.format(date);
                    ngaysinh = (dateString);
                }

                Timestamp tstampnvl = (Timestamp) data.get("ngayvaolam");
                if (tstampnvl != null) {
// Chuyển đổi Timestamp thành Date
                    Date date = tstampnvl.toDate();

// Định dạng Date thành chuỗi theo ý muốn
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String dateString = sdf.format(date);
                    ngayvaolam = (dateString);
                }
                if (chucvu != null) {
                    binding.tvchucvu.setText(chucvu);
                }
                if (lienhe != null) {
                    binding.tvlienhe.setText(lienhe);
                }
                if (ngaysinh != null) {
                    binding.tvngaysinh.setText(ngaysinh);
                }
                if (ngayvaolam != null) {
                    binding.tvngayvaolam.setText(ngayvaolam);
                }
                if (name != null) {
                    binding.tvname.setText(name);
                }

            }
        });
        return (binding.getRoot());
    }


    private void getInfo(FirestoreCallback callback) {
        DocumentReference docRef;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (key_user == 0) {
            docRef = db.collection("nhanvien").document(ID);
        } else {
            docRef = db.collection("sep").document(ID);


        }

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        // Lấy toàn bộ thông tin từ tài liệu vào biến data
                        // ...
                        callback.onCallback(data);


                    } else {
                        // Tài liệu không tồn tại
                    }
                } else {
                    // Lỗi xảy ra khi lấy tài liệu
                }
            }
        });
    }

    public interface FirestoreCallback {
        void onCallback(Map<String, Object> data);
    }
}