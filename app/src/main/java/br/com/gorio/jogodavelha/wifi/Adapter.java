package br.com.gorio.jogodavelha.wifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import br.com.gorio.jogodavelha.R;
import br.com.gorio.jogodavelha.model.User;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Holder> implements Filterable {
    private Context context;
    private List<User> users;
    private List<User> userListFiltered;

    public Adapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.userListFiltered = users;
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
        User user = userListFiltered.get(position);
        holder.binding.setUser(user);
    }

    @Override
    public int getItemCount() {
        return userListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    userListFiltered = users;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User row : users) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    userListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userListFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
