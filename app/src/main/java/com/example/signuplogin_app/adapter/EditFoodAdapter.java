package com.example.signuplogin_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.signuplogin_app.Food;
import com.example.signuplogin_app.R;

import java.util.List;

public class EditFoodAdapter extends RecyclerView.Adapter<EditFoodAdapter.EditFoodViewHolder> {
    private List<Food> foodList;
    private OnFoodEditClickListener listener;
    // Constructor nhận dữ liệu và listener
    public EditFoodAdapter(List<Food> foodList, OnFoodEditClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    // Interface để truyền các sự kiện (sửa/xóa)
    public interface OnFoodEditClickListener {
        void onEditFoodClick(Food food);
        void onDeleteFoodClick(Food food);
    }

    @NonNull
    @Override
    public EditFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editfood_item, parent, false);
        return new EditFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditFoodViewHolder holder, int position) {
        holder.bind(foodList.get(position));  // Bind dữ liệu vào ViewHolder
    }

    @Override
    public int getItemCount() {
        return foodList.size();  // Trả về số lượng món ăn trong danh sách
    }

    public class EditFoodViewHolder extends com.example.signuplogin_app.FoodViewHolder {

        private TextView btnEditFood, btnDeleteFood;

        public EditFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEditFood = itemView.findViewById(R.id.btn_editfood);
            btnDeleteFood = itemView.findViewById(R.id.btn_deletefood);
        }

        public void bind(Food food) {
            super.bind(food);
            // Xử lý sự kiện khi nhấn sửa
            btnEditFood.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditFoodClick(food);
                }
            });

            // Xử lý sự kiện khi nhấn xóa
            btnDeleteFood.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteFoodClick(food);
                }
            });
        }
    }

    public void updateList(List<Food> newList){
        foodList=newList;
        notifyDataSetChanged();
    }
}
