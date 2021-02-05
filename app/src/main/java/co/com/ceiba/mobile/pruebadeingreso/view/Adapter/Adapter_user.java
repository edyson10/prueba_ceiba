package co.com.ceiba.mobile.pruebadeingreso.view.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.Database.DTO.User;
import co.com.ceiba.mobile.pruebadeingreso.R;

public class Adapter_user extends RecyclerView.Adapter<Adapter_user.ViewHolderUser> implements View.OnClickListener {

    ArrayList<User> listUser;
    private View.OnClickListener listener;
    private Context context;

    public Adapter_user(ArrayList<User> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter_user.ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, null, false);
        view.setOnClickListener(this);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new Adapter_user.ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_user.ViewHolderUser holder, int position) {
        holder.nombre.setText(listUser.get(position).getNombre());
        holder.correo.setText(listUser.get(position).getEmail());
        holder.telefono.setText(listUser.get(position).getTelefono());
    }

    @Override
    public int getItemCount() {
        return this.listUser.size();
    }

    public void filterList (ArrayList<User> filterUser) {
        listUser = filterUser;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (this.listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {

        TextView nombre, correo, telefono;

        public ViewHolderUser(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.name);
            correo = itemView.findViewById(R.id.phone);
            telefono = itemView.findViewById(R.id.email);
        }
    }
}
