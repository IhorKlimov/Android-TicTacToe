package com.myhexaville.tictactoe.wifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.myhexaville.tictactoe.R;
import com.myhexaville.tictactoe.model.User;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Holder> {
    private Context context;
    private List<User> users;

    public Adapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.user_list_item,
                parent,
                false);

        return new Holder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = users.get(position);
        holder.binding.setUser(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
