package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import co.com.ceiba.mobile.pruebadeingreso.Config.Constantes;
import co.com.ceiba.mobile.pruebadeingreso.Database.AppDatabase;
import co.com.ceiba.mobile.pruebadeingreso.Database.DTO.User;
import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.Services.Services;
import co.com.ceiba.mobile.pruebadeingreso.view.Adapter.Adapter_user;

public class MainActivity extends Activity {

    RecyclerView recyclerView;
    ArrayList<User> listUser;
    Adapter_user adapter_user;

    ProgressDialog progressDialog;
    EditText txtBuscar;
    TextView listEmpty;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearchResults);
        txtBuscar = (EditText) findViewById(R.id.editTextSearch);
        listEmpty = (TextView) findViewById(R.id.txtListEmpty);

        listEmpty.setVisibility(View.INVISIBLE);
        listUser = new ArrayList<>();

        db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, Constantes.BD_NAME)
                .allowMainThreadQueries()
                .build();

        int tamanio = db.userDao().count();
        if (tamanio <= 0) {
            llenarRecyclerUser();
        } else {
            llenarDatosRoom();
        }

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<User> filterUser = new ArrayList<>();
        for (User user : listUser) {
            if (user.getNombre().toLowerCase().contains(text.toLowerCase())) {
                filterUser.add(user);
            }
        }

        if (filterUser.size() == 0) {
            listEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            listEmpty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter_user.filterList(filterUser);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void llenarRecyclerUser() {
        ConnectivityManager con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
            progressDialog.setMessage("Cargando... Por favor espere!");
            progressDialog.show();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    final String resultado = Services.listarUsers();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(resultado);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    int id = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                                    String nombre = jsonArray.getJSONObject(i).getString("name");
                                    String correo = jsonArray.getJSONObject(i).getString("email");
                                    String telefono = jsonArray.getJSONObject(i).getString("phone");
                                    listUser.add(new User(id, nombre, correo, telefono));
                                    User users = new User();
                                    users.setId(id);
                                    users.setNombre(nombre);
                                    users.setEmail(correo);
                                    users.setTelefono(telefono);
                                    db.userDao().instarAll(users);
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                adapter_user = new Adapter_user(listUser, getApplicationContext());
                                adapter_user.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loadView(listUser.get(recyclerView.getChildAdapterPosition(view)).getId(),
                                                listUser.get(recyclerView.getChildAdapterPosition(view)).getNombre(), listUser.get(recyclerView.getChildAdapterPosition(view)).getEmail(),
                                                listUser.get(recyclerView.getChildAdapterPosition(view)).getTelefono());
                                    }
                                });
                                recyclerView.setAdapter(adapter_user);
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

    private void llenarDatosRoom() {
        progressDialog.setMessage("Cargando... Por favor espere!");
        progressDialog.show();
        listUser = (ArrayList<User>) db.userDao().getAllUsuarios();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter_user = new Adapter_user(listUser, getApplicationContext());
        adapter_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadView(listUser.get(recyclerView.getChildAdapterPosition(view)).getId(),
                        listUser.get(recyclerView.getChildAdapterPosition(view)).getNombre(), listUser.get(recyclerView.getChildAdapterPosition(view)).getEmail(),
                        listUser.get(recyclerView.getChildAdapterPosition(view)).getTelefono());
            }
        });
        recyclerView.setAdapter(adapter_user);
        progressDialog.hide();
    }

    private void loadView(int id, String nombre, String correo, String telefono) {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("nombre", nombre);
        intent.putExtra("correo", correo);
        intent.putExtra("telefono", telefono);
        startActivity(intent);
    }
}