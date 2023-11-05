package com.game.nghiphep;

import static com.game.nghiphep.LoginActivity.ID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.game.nghiphep.databinding.DangkynghifragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DangKyNghiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DangKyNghiFragment extends Fragment {
    private Activity activity;

    // Phương thức setter để truyền tham chiếu đến activity
    public void setActivityReference(Activity activity) {
        this.activity = activity;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DangkynghifragmentBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String lich;


    public DangKyNghiFragment(Activity activity) {
        this.activity = activity;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DangKyNghiFragment newInstance(String param1, String param2, Activity activity) {
        DangKyNghiFragment fragment = new DangKyNghiFragment(activity);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    int selectedDay;
    int thu;
    int selectedMonth;
    int selectedYear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = DangkynghifragmentBinding.inflate(inflater, container, false);
        ((HomeActivity) getActivity()).enableProgressbar();

        getNhanVien(new FirestoreCallback() {

            @Override
            public void onCallback(Map<String, Object> data) {
                lich = (String) data.get("ngaydilam");
//                String[] numbers = lich.split(",");
//
//                for (String number : numbers) {
//                    lichlamviec.add(Integer.valueOf(number));
//                }
                String uilich = "";
                if (lich.contains("1")) {
                    uilich = lich.replace("1", "Chủ Nhật");
                }

                checkNghiExists();
                binding.lichlamviec.setText("Lịch làm việc: " + uilich);

            }

        },ID,activity);

        binding.calendar.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            selectedDay = i2;
            selectedMonth = i1;
            selectedYear = i;
            UIngaynghi();
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(i, i1, i2);

            Date ngaynghi = selectedDate.getTime();
            Date currentDate = new Date();
            thu = selectedDate.get(Calendar.DAY_OF_WEEK);

            if (!lich.contains(String.valueOf(thu))) {
                Toast.makeText(activity, "Bạn không đi làm ngày này!", Toast.LENGTH_SHORT).show();
                binding.btndangky.setVisibility(View.INVISIBLE);
            }
            else if(ngaynghi.before(currentDate)){
                Toast.makeText(activity, "Vui lòng chọn ngày đăng ký nghỉ lớn hơn ngày hiện tại", Toast.LENGTH_SHORT).show();
                binding.btndangky.setVisibility(View.INVISIBLE);
            }
            else {
                binding.btndangky.setVisibility(View.VISIBLE);

                // Xử lý sự kiện khi ngày hợp lệ được chọn
                Toast.makeText(activity, "Ngày hợp lệ được chọn!", Toast.LENGTH_SHORT).show();
                UIngaynghi();
            }


        });
        binding.btndangky.setOnClickListener(view -> {

            if (selectedDay == 0) {
                Toast.makeText(activity, "Vui lòng chọn ngày đăng ký nghỉ", Toast.LENGTH_SHORT).show();

            }

            else {
                GuiDonFragment fragment = GuiDonFragment.newInstance(thu,selectedDay,selectedMonth,selectedYear,activity);
                FragmentManager fragmentManager = ((HomeActivity)activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();
            }

        });
        return binding.getRoot();
    }

    public static void getNhanVien(FirestoreCallback callback, String msnv, Context context) {
        DocumentReference docRef;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        docRef = db.collection("nhanvien").document(msnv);
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
                        Toast.makeText(context, "Nhân viên không tồn tại!", Toast.LENGTH_SHORT).show();

                        // Tài liệu không tồn tại
                    }
                } else {
                    // Lỗi xảy ra khi lấy tài liệu
                }
            }
        });
    }
    public void checkNghiExists() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Lấy ngày hiện tại
        Date currentDate = new Date();

// Tạo truy vấn
        Query query = db.collection("donxinnghi").whereEqualTo("msnv1", ID)
                .whereGreaterThan("ngay", currentDate);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ((HomeActivity)activity).hideProgressbar();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Xử lý tài liệu tại đây
                            String documentId = documentSnapshot.getId();
                            String lido =documentSnapshot.getString("lido");
                            int trangthai=0;
                            Long longtr =documentSnapshot.getLong("trangthai");
                            if(null != longtr){
                                trangthai = longtr.intValue();
                            }
                            Timestamp timestamp = documentSnapshot.getTimestamp("ngay");
                            String ngaynghi="";
                            Date date;
                            if (timestamp != null) {
                                date = timestamp.toDate();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                ngaynghi = dateFormat.format(date);
                            }
                            DaCoDon fragment = DaCoDon.newInstance(documentId,lido,ngaynghi,trangthai,activity);
                            FragmentManager fragmentManager = ((HomeActivity)activity).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout, fragment);
                            fragmentTransaction.commit();
                            break;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Xảy ra lỗi khi lấy dữ liệu
                        Log.d("loi","loi"+e);


                    }
                });
    }

    public interface FirestoreCallback {
        void onCallback(Map<String, Object> data);
    }


    public void UIngaynghi() {
        String listnn = "";
        listnn = listnn + selectedDay + "/" + selectedMonth + "/" + selectedYear;
        binding.ngaynghi.setText("Ngày nghỉ: " + listnn);
    }




}