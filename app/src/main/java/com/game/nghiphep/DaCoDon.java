package com.game.nghiphep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.nghiphep.databinding.DacodonBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DaCoDon#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DaCoDon extends Fragment {
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

    public DaCoDon(Activity activity) {
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
    public static DaCoDon newInstance(String documentid,String lido, String ngaynghi, int tt,Activity activity) {
        DaCoDon fragment = new DaCoDon(activity);
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

        DacodonBinding binding = DacodonBinding.inflate(inflater, container, false);
        binding.lido.setText(lido);
        binding.ngaynghi.setText("Ngày nghỉ: " + ngaynghi);
        if(trangthai==0){
            binding.trangthai.setText("CHƯA DUYỆT");}
        else if(trangthai==1)
        {
            binding.trangthai.setText("ĐƯỢC ĐỒNG Ý");
        }
        else {
            binding.trangthai.setText("BỊ TỪ CHỐI");
        }
            binding.guiyeucau.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Thông báo") // Thiết lập tiêu đề của AlertDialog
                        .setMessage("Bạn có chắc chắn hủy không") // Thiết lập nội dung của AlertDialog
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Xử lý khi nhấn nút OK
                                deleteDonXinNghi();

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý khi nhấn nút Cancel
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });

        return binding.getRoot();
    }

    private void deleteDonXinNghi() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("donxinnghi").document(documentid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Xóa thành công
                        replaceFragment(new DangKyNghiFragment(activity));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xóa thất bại
                    }
                });

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = ((HomeActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }
}