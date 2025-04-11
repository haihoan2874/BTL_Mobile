package com.example.signuplogin_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.signuplogin_app.FoodCount;
import com.example.signuplogin_app.R;

import java.util.List;

public class TopFoodAdapter extends RecyclerView.Adapter<TopFoodAdapter.TopFoodViewHolder> {

    private final List<FoodCount> foodList;

    public TopFoodAdapter(List<FoodCount> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public TopFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_food, parent, false);
        return new TopFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopFoodViewHolder holder, int position) {
        FoodCount food = foodList.get(position);
        holder.tvName.setText(food.getName());
        holder.tvQuantity.setText("Số lượng: " + food.getCount());
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    static class TopFoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity;

        public TopFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvQuantity = itemView.findViewById(R.id.tv_food_quantity);
        }
    }
}
