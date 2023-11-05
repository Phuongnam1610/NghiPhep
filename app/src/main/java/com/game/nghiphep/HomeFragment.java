package com.game.nghiphep;

import static com.game.nghiphep.LoginActivity.ID;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.game.nghiphep.Adapters.ItemnghiAdapter;
import com.game.nghiphep.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<DonXinNghi> donXinNghis = new ArrayList<>();
    private ItemnghiAdapter itemnghiAdapter;

    private Activity activity;


    // TODO: Rename and change types of parameters
    private String mParam1;
    FragmentHomeBinding binding;
    private String mParam2;

    public HomeFragment(Activity activity) {
        this.activity=activity;
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
    public static HomeFragment newInstance(String param1, String param2,Activity activity) {
        HomeFragment fragment = new HomeFragment(activity);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentHomeBinding.inflate(inflater,container,false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false);
        itemnghiAdapter=new ItemnghiAdapter(donXinNghis);
        binding.rcvnghi.setLayoutManager(linearLayoutManager2);
        binding.rcvnghi.setAdapter(itemnghiAdapter);
        getChuaDuyet();


        binding.btnlichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Date Select Listener.
                // Create DatePickerDialog (Spinner Mode):
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        binding.btnchuaduyet.setVisibility(View.VISIBLE);
                        ((HomeActivity) activity).enableProgressbar();

                        binding.tvchuaduyet.setText(i2 + "/" + (i1 + 1) + "/" + i);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, i); // Set năm
                        calendar.set(Calendar.MONTH, i1); // Set tháng (lưu ý: tháng bắt đầu từ 0)
                        calendar.set(Calendar.DAY_OF_MONTH, i2); // Set ngày

                        Date date = calendar.getTime(); // Chuyển đổi từ Calendar sang Date

                        getDonXinNghiTheoNgay(i2,i1,i);
                    }
                });
// Show
                datePickerDialog.show();


            }

        });
        binding.btnchuaduyet.setOnClickListener(view -> replaceFragment(new HomeFragment(activity)));

        return binding.getRoot();
    }

    private void getChuaDuyet(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Lấy ngày hiện tại
        Date currentDate = new Date();

// Tạo truy vấn
        Query query = db.collection("donxinnghi").whereEqualTo("trangthai", 0)
                .whereGreaterThan("ngay", currentDate);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ((HomeActivity)activity).hideProgressbar();


                        donXinNghis.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Xử lý tài liệu tại đây
                            String documentId = documentSnapshot.getId();
                            String lido =documentSnapshot.getString("lido");
                            String msnv1 = documentSnapshot.getString("msnv1");

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
                            DonXinNghi a = new DonXinNghi();
                            a.setLido(lido);
                            a.setDocumentID(documentId);
                            a.setMsnv1(msnv1);
                            a.setNgay(timestamp.toDate());
                            a.setTrangthai(0);
                            donXinNghis.add(a);
                        }
                        itemnghiAdapter.setClickListener(new ItemnghiAdapter.onClickItemListener() {
                            @Override
                            public void onClickItem(DonXinNghi donXinNghi, int position) {
                                Date date = donXinNghi.getNgay();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String ngaynghi = dateFormat.format(date);
                                replaceFragmentBackStack(DuyetDonFragment.newInstance(donXinNghi.getDocumentID(),donXinNghi.getLido(),ngaynghi,donXinNghi.getTrangthai(),activity));
                            }
                        });
                        itemnghiAdapter.notifyDataSetChanged();

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
    private void getDonXinNghiTheoNgay(int day, int month, int year){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Lấy ngày hiện tại

// Định nghĩa ngày bắt đầu và ngày kết thúc
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, month, day, 0, 0, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, month, day, 23, 59, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);

// Tạo truy vấn
        Query query = db.collection("donxinnghi")
                .whereGreaterThanOrEqualTo("ngay", startCalendar.getTime())
                .whereLessThanOrEqualTo("ngay", endCalendar.getTime());
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ((HomeActivity)activity).hideProgressbar();

                        donXinNghis.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Xử lý tài liệu tại đây
                            String documentId = documentSnapshot.getId();
                            String lido =documentSnapshot.getString("lido");
                            String msnv1 = documentSnapshot.getString("msnv1");
                            int trangthai=0;
                            Long longtr =documentSnapshot.getLong("trangthai");
                            if(null != longtr){
                                trangthai = longtr.intValue();
                            }
                            Timestamp timestamp = documentSnapshot.getTimestamp("ngay");
//                            String ngaynghi="";
//                            Date date;
//                            if (timestamp != null) {
//                                date = timestamp.toDate();
//                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                ngaynghi = dateFormat.format(date);
//                            }
                            DonXinNghi a = new DonXinNghi();
                            a.setLido(lido);
                            a.setDocumentID(documentId);
                            a.setMsnv1(msnv1);
                            a.setNgay(timestamp.toDate());
                            a.setTrangthai(0);
                            donXinNghis.add(a);
                        }

                        itemnghiAdapter.setClickListener(new ItemnghiAdapter.onClickItemListener() {
                            @Override
                            public void onClickItem(DonXinNghi donXinNghi, int position) {
                                                            String ngaynghi="";
                            Date ngay = donXinNghi.getNgay();

                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                ngaynghi = dateFormat.format(ngay);
                                replaceFragmentBackStack(NoiDungNghiFragment.newInstance(donXinNghi.getDocumentID(),donXinNghi.getLido(),ngaynghi,donXinNghi.getTrangthai(),activity));
                            }
                        });
                        itemnghiAdapter.notifyDataSetChanged();
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
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = ((HomeActivity)activity).getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }
    private void replaceFragmentBackStack(Fragment fragment) {

        FragmentManager fragmentManager = ((HomeActivity)activity).getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this); // Ẩn Fragment B
        fragmentTransaction.add(R.id.frame_layout, fragment); // Thêm Fragment A vào container
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}