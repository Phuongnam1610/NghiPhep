package com.game.nghiphep.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.nghiphep.DonXinNghi;
import com.game.nghiphep.HomeActivity;
import com.game.nghiphep.databinding.ItemrcvnghiBinding;

import java.util.List;

public class ItemnghiAdapter extends RecyclerView.Adapter<ItemnghiAdapter.ItemViewHolder> {

    // Các biến thành viên và phương thức cần thiết
    private List<DonXinNghi> Donxinnghis;

    public ItemnghiAdapter(List<DonXinNghi> Donxinnghis) {
        this.Donxinnghis = Donxinnghis;
    }

    private onClickItemListener onClickItemListener;

    public void setClickListener(onClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Khởi tạo ViewHolder với item_layout.xml
        ItemrcvnghiBinding itemrcvnghiBinding = ItemrcvnghiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(itemrcvnghiBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DonXinNghi donXinNghi = Donxinnghis.get(position);
        // Cập nhật giao diện cho mục tại vị trí position
        holder.itemrcvnghiBinding.msnv.setText(donXinNghi.getMsnv1());
        if (donXinNghi.getTrangthai() == 0) {
            holder.itemrcvnghiBinding.trangthai.setText("Chưa Được Duyệt");
        } else if (donXinNghi.getTrangthai() == 1) {
            holder.itemrcvnghiBinding.trangthai.setText("Đồng Ý");
        } else {
            holder.itemrcvnghiBinding.trangthai.setText("Chưa Được Duyệt");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        if (onClickItemListener != null) {
            onClickItemListener.onClickItem(donXinNghi, holder.getAdapterPosition());
        }
    }
    });

}

    @Override
    public int getItemCount() {
        // Trả về số lượng mục trong danh sách
        return Donxinnghis.size();
    }

public static class ItemViewHolder extends RecyclerView.ViewHolder {
    ItemrcvnghiBinding itemrcvnghiBinding;
    // Các thành phần giao diện trong item_layout.xml

    public ItemViewHolder(ItemrcvnghiBinding itemrcvnghiBinding) {

        super(itemrcvnghiBinding.getRoot());
        this.itemrcvnghiBinding = itemrcvnghiBinding;
        // Liên kết các thành phần giao diện với mã Java
    }

}

public interface onClickItemListener {
    void onClickItem(DonXinNghi donXinNghi, int position);
}

}
