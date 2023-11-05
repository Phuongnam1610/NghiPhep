package com.game.nghiphep;

import static com.game.nghiphep.LoginActivity.ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.game.nghiphep.databinding.DialogmsnvBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dialogmsnv#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dialogmsnv extends DialogFragment {
    private DataTransferListener dataTransferListener;

    public void setDataTransferListener(DataTransferListener listener) {
        this.dataTransferListener = listener;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String thu;
    private String msnvdadangky;
    private String mParam2;



    public dialogmsnv() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment dialogmsnv.
     */
    // TODO: Rename and change types and number of parameters
    public static dialogmsnv newInstance(String thu,String msnvdadangky) {
        dialogmsnv fragment = new dialogmsnv();
        Bundle args = new Bundle();
        args.putString("KEY_THU", thu);
        args.putString("KEY_MSNVDADANGKY", msnvdadangky);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            thu = getArguments().getString("KEY_THU");
            msnvdadangky=getArguments().getString("KEY_MSNVDADANGKY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DialogmsnvBinding binding = DialogmsnvBinding.inflate(inflater);
        if(msnvdadangky!=null)
        {
            if(msnvdadangky.length()>0)
            {   binding.msnv.setText(msnvdadangky);
                binding.msnv.setEnabled(false);
                binding.btndangky.setVisibility(View.GONE);
                binding.huydangky.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            binding.btndangky.setVisibility(View.VISIBLE);
            binding.huydangky.setVisibility(View.GONE);

        }
        binding.btndangky.setOnClickListener(view ->
        { String msnv=binding.msnv.getText().toString();
            if(msnv!=null)
            {
                getLichLamViec(new FirestoreCallback() {
                    @Override
                    public void onCallback(Map<String, Object> data) {
                        String ngaydilam=(String) data.get("ngaydilam");
                        if(ngaydilam!=null)
                        {
                            if(ngaydilam.contains(thu)){
                                Toast.makeText(getContext().getApplicationContext(), "Người hỗ trợ đi làm ngày này, Vui lòng đổi người hỗ trợ!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (dataTransferListener != null) {
                                    dataTransferListener.onDataTransfer(msnv,true);
                                }
                                Toast.makeText(getContext().getApplicationContext(), "Đăng ký người hỗ trợ thành công!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }

                    }
                },msnv);

            }

        });
        binding.huydangky.setOnClickListener(view ->{
            if (dataTransferListener != null) {
                dataTransferListener.onDataTransfer("",true);
            }
            Toast.makeText(getContext().getApplicationContext(), "Hủy đăng ký thành công!", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return binding.getRoot();
    }

    public void getLichLamViec(FirestoreCallback callback,String msnv)
    {
        DocumentReference docRef;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        docRef= db.collection("nhanvien").document(msnv);
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
                        Toast.makeText(getContext().getApplicationContext(), "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Lỗi xảy ra khi lấy tài liệu
                    Toast.makeText(getContext().getApplicationContext(), "Lỗi mạng!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public interface FirestoreCallback {
        void onCallback(Map<String, Object> data);
    }

    public interface DataTransferListener {
        void onDataTransfer(String data,boolean dangky);
    }
}