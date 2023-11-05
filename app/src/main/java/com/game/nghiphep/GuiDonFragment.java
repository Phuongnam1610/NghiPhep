package com.game.nghiphep;

import static com.game.nghiphep.LoginActivity.ID;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.game.nghiphep.databinding.GuidonfragmentBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuiDonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuiDonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;
    private Activity activity;
    public GuiDonFragment(Activity activity) {
        this.activity=activity;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Trang2.
     */
    // TODO: Rename and change types and number of parameters
    public static GuiDonFragment newInstance(int thu, int day, int month, int year, Activity activity) {
        GuiDonFragment fragment = new GuiDonFragment(activity);
        Bundle args = new Bundle();
        args.putInt("KEY_THU", thu);
        args.putInt("KEY_DAY", day);
        args.putInt("KEY_MONTH", month);
        args.putInt("KEY_YEAR", year);

        fragment.setArguments(args);
        return fragment;
    }
    int thu;
    Boolean chuaduyet;
    int selectedDay;
    int selectedMonth;
    int selectedYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedDay = getArguments().getInt("KEY_DAY");
            selectedMonth = getArguments().getInt("KEY_MONTH");
            selectedYear = getArguments().getInt("KEY_YEAR");
            thu= getArguments().getInt("KEY_THU");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GuidonfragmentBinding binding = GuidonfragmentBinding.inflate(inflater, container, false);
        binding.btnback.setOnClickListener(view -> activity.onBackPressed());
        binding.ngaynghi.setText("Ngày nghỉ: "+selectedDay+"/"+(selectedMonth+1)+"/"+selectedYear);
        binding.guiyeucau.setOnClickListener(view -> {
            String lido=binding.lido.getText().toString();

            if(lido.length()>0 )
            {
                guiHoTro(lido,0);

            }
            else {
                Toast.makeText(getContext().getApplicationContext(), "Không để trống li do va người hỗ trợ!", Toast.LENGTH_SHORT).show();

            }

        });
        return binding.getRoot();
    }

    private void guiHoTro(String lido, int trangthai) {
        // Tạo một đối tượng Calendar và đặt ngày, tháng, năm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
        calendar.set(Calendar.MONTH, selectedMonth);  // Trừ 1 vì tháng trong Calendar bắt đầu từ 0
        calendar.set(Calendar.YEAR, selectedYear);

// Lấy đối tượng Date từ đối tượng Calendar
        Date date = calendar.getTime();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("donxinnghi");
// Tạo một HashMap để lưu trữ các thuộc tính của tài liệu
        Map<String, Object> documentData = new HashMap<>();
        documentData.put("lido", lido);
        documentData.put("msnv1",ID);
        documentData.put("ngay", date);
        documentData.put("trangthai",0);

// Tạo một tài liệu mới với ID tự động và các thuộc tính đã định nghĩa
        collectionRef.add(documentData)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    replaceFragment(new DangKyNghiFragment(getActivity()));
                    Toast.makeText(getContext().getApplicationContext(), "Gui don thanh cong!", Toast.LENGTH_SHORT).show();
                    // Xử lý thành công, documentId là ID của tài liệu mới được tạo

                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                });


    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }




}