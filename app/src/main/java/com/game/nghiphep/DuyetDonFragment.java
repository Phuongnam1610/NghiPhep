package com.game.nghiphep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.game.nghiphep.databinding.DacodonBinding;
import com.game.nghiphep.databinding.DuyetdonBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DuyetDonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DuyetDonFragment extends Fragment {
    private Activity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String lido;
    private String documentid;
    private String ngaynghi;
    private int trangthai;

    public DuyetDonFragment(Activity activity) {
        this.activity=activity;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * \
     *
     * @return A new instance of fragment DaCoDon.
     */
    // TODO: Rename and change types and number of parameters
    public static DuyetDonFragment newInstance(String documentid, String lido, String ngaynghi,  int tt, Activity activity) {
        DuyetDonFragment fragment = new DuyetDonFragment(activity);
        Bundle args = new Bundle();
        args.putString("KEY_LIDO", lido);
        args.putString("KEY_DOCUMENTID",documentid);
        args.putString("KEY_NGAYNGHI", ngaynghi);
        args.putInt("KEY_TRANGTHAI", tt);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lido = getArguments().getString("KEY_LIDO");
            ngaynghi = getArguments().getString("KEY_NGAYNGHI");
            documentid=getArguments().getString("KEY_DOCUMENTID");
            trangthai = getArguments().getInt("KEY_TRANGTHAI");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DuyetdonBinding binding = DuyetdonBinding.inflate(inflater, container, false);
        binding.lido.setText(lido);
        binding.btnback.setOnClickListener(v->activity.onBackPressed());
        binding.ngaynghi.setText(ngaynghi);
        if(trangthai==0){
            binding.trangthai.setText("CHƯA ĐƯỢC DUYỆT");

        }
        else if(trangthai==1)
        {
            binding.trangthai.setText("ĐỒNG Ý");

        }
        else {
            binding.trangthai.setText("BỊ TỪ CHỐI");
        }
        binding.btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateTT(documentid,1);
            }
        });
        binding.btntuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateTT(documentid,2);
            }
        });
        return binding.getRoot();
    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = ((HomeActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    private void UpdateTT(String documentID, int trangthai)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
// Xác định đường dẫn đến tài liệu
        DocumentReference docRef = db.collection("donxinnghi").document(documentID);

// Cập nhật trường trong tài liệu
        docRef.update("trangthai", trangthai)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cập nhật thành công
                        replaceFragment(new HomeFragment(activity));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                         if (e instanceof FirebaseFirestoreException) {
                            FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) e;
                            if (firestoreException.getCode() == FirebaseFirestoreException.Code.NOT_FOUND) {
                                // Xử lý khi không tìm thấy tài liệu để cập nhật
                                Toast.makeText(activity, "Đơn xin nghỉ đã bị thu hồi", Toast.LENGTH_SHORT).show();
                                replaceFragment(new HomeFragment(activity          ));
                                // Ví dụ: Thêm mã xử lí tại đây
                            }
                        }
                        // Xử lý lỗi
                    }
                });
    }
}