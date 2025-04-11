package com.example.signuplogin_app.adapter;

import android.content.Context;
import android.util.Log;
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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foodList;
    private OnAddFoodClickListener listener;

    // Constructor nhận dữ liệu và listener từ MainActivity
    public FoodAdapter(List<Food> foodList,OnAddFoodClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;

    }

    public interface OnAddFoodClickListener{
        void onAddFoodClick(Food food);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);// Trả về FoodViewHolder với đúng listener
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bind(foodList.get(position));   // Gọi hàm bind để cập nhật dữ liệu cho ViewHolder
    }

    @Override
    public int getItemCount() {
        return foodList.size();  //tra ve so luong mon an trong danh sach
    }

    public class FoodViewHolder extends com.example.signuplogin_app.FoodViewHolder{
        private ImageView btn_addFood;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_addFood= itemView.findViewById(R.id.btn_addfood);
        }

        public void bind(Food food){
            super.bind(food);

            btn_addFood.setOnClickListener(v->{
                if(listener !=null){
                    listener.onAddFoodClick(food);
                }
            });
        }
    }

    public void updateList(List<Food> newList){
        foodList=newList;
        notifyDataSetChanged();
    }
}
