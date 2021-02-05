package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.Database.DTO.Post;
import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.Services.Services;
import co.com.ceiba.mobile.pruebadeingreso.view.Adapter.Adapter_posts;

public class PostActivity extends Activity {

    RecyclerView recyclerView;
    ArrayList<Post> listPost;
    Adapter_posts adapter_posts;

    ProgressDialog progressDialog;

    TextView nombre, telefono, correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        progressDialog = new ProgressDialog(PostActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPostsResults);
        nombre = (TextView) findViewById(R.id.name);
        correo = (TextView) findViewById(R.id.email);
        telefono = (TextView) findViewById(R.id.phone);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        String name = bundle.getString("nombre");
        String email = bundle.getString("correo");
        String phone = bundle.getString("telefono");

        nombre.setText(name);
        correo.setText(email);
        telefono.setText(phone);

        listPost = new ArrayList<>();
        llenarRecyclerUser(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void llenarRecyclerUser(final int id) {
        ConnectivityManager con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
            progressDialog.setMessage("Cargando... Por favor espere!");
            progressDialog.show();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    final String resultado = Services.listarPost(id);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(resultado);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String titulo = jsonArray.getJSONObject(i).getString("title");
                                    String body = jsonArray.getJSONObject(i).getString("body");
                                    listPost.add(new Post(titulo, body));
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
                                adapter_posts = new Adapter_posts(listPost, getApplicationContext());
                                recyclerView.setAdapter(adapter_posts);
                                progressDialog.hide();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.hide();
                            }
                        }
                    });
                }
            };
            thread.start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("¡Error!");
            builder.setMessage("No hay conexión a internet. \nVerifique e intente de nuevo.");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
