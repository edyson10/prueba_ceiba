package co.com.ceiba.mobile.pruebadeingreso.view.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.Database.DTO.Post;
import co.com.ceiba.mobile.pruebadeingreso.R;

public class Adapter_posts extends RecyclerView.Adapter<Adapter_posts.ViewHolderUser> implements View.OnClickListener {

    ArrayList<Post> listPosts;
    private View.OnClickListener listener;
    private Context context;

    public Adapter_posts(ArrayList<Post> listPosts, Context context) {
        this.listPosts = listPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_posts.ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, null, false);
        view.setOnClickListener(this);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new Adapter_posts.ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_posts.ViewHolderUser holder, int position) {
        holder.titulo.setText(listPosts.get(position).getTitulo());
        holder.cuerpo.setText(listPosts.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return this.listPosts.size();
    }

    @Override
    public void onClick(View view) {
        if (this.listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {

        TextView titulo, cuerpo;

        public ViewHolderUser(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.title);
            cuerpo = itemView.findViewById(R.id.body);
        }
    }
}
