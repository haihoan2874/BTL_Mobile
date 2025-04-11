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
import com.example.signuplogin_app.CartItem;
import com.example.signuplogin_app.Food;
import com.example.signuplogin_app.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;

    public CartAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class CartViewHolder extends com.example.signuplogin_app.FoodViewHolder {
        private TextView txt_quantity;
        private ImageView btn_addQuantity, btn_removeQuantity;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_addQuantity=itemView.findViewById(R.id.btn_addQuantity);
            btn_removeQuantity=itemView.findViewById(R.id.btn_removeQuantity);
            txt_quantity=itemView.findViewById(R.id.txt_quantity);
        }

        public void bind(CartItem cartItem) {
            txtName.setText(cartItem.getFood().getNamefood());
            txtPrice.setText(String.valueOf(cartItem.getFood().getPricefood())+"đ");
            txt_quantity.setText(String.valueOf(cartItem.getQuantity()));

            Glide.with(itemView.getContext())
                    .load(cartItem.getFood().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .into(imgFood);


            btn_addQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItem.setQuantity(cartItem.getQuantity()+1);
                    txt_quantity.setText(String.valueOf(cartItem.getQuantity()));
                    notifyItemChanged(getAdapterPosition());

                    if(cartUpdateListener !=null){
                        cartUpdateListener.onCartUpdate();
                    }
                }
            });

            btn_removeQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        CartItem currentItem = cartItemList.get(currentPosition);
                        int quantity = currentItem.getQuantity();
                        if (quantity > 1) {
                            currentItem.setQuantity(quantity - 1);
                            notifyItemChanged(currentPosition);
                        } else {
                            cartItemList.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            notifyItemRangeChanged(currentPosition, cartItemList.size());
                        }

                        if (cartUpdateListener != null) {
                            cartUpdateListener.onCartUpdate();
                        }
                    }
                }
            });
        }
    }

    private CartUpdateListener cartUpdateListener;
    public interface CartUpdateListener{
        void onCartUpdate();
    }
    public CartAdapter(List<CartItem> cartItemList,CartUpdateListener listener){
        this.cartItemList=cartItemList;
        this.cartUpdateListener=listener;
    }
}

