package com.example.agenda_v07;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda_v07.BBDD.BBDD_Create;
import com.example.agenda_v07.modelo.Contacto;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ContactoAdapter adaptador;
    private BBDD_Create manager;
    private ListView lvContactos;
    private ArrayList<Contacto> listaContactos;
    private int id;
    private String filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        filtro = "";

        Toast.makeText(getApplication(), this.getFilesDir().getAbsolutePath(), Toast.LENGTH_LONG).show();

        actualizarContenido();
        listViewContactos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        busquedas(menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem optionMenu) {

        int id = optionMenu.getItemId();

        if (id == R.id.btnMenuNuevo) {
            btnNuevo(null);
            return true;
        }

        if (id == R.id.agendacompleta) {
            filtro = "";
            actualizarContenido();
            listViewContactos();
            return true;
        }

        return super.onOptionsItemSelected(optionMenu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarContenido();
        listViewContactos();

    }

    private void actualizarContenido() {

        manager = new BBDD_Create(this);
        Cursor cursorContacto = null;


        if (filtro.equals("")){
            cursorContacto = manager.getListaContactos();
        } else {
            cursorContacto = manager.getFiltroContactos(filtro);
        }

        Contacto contacto;
        listaContactos = new ArrayList<Contacto>();

        while (cursorContacto.moveToNext()) {
            id = cursorContacto.getInt(0);

            contacto = new Contacto();
            contacto.setNombre(cursorContacto.getString(1));
            contacto.setApellido(cursorContacto.getString(2));
            contacto.setFoto(cursorContacto.getString(3));
            contacto.setId(id);

            listaContactos.add(contacto);

        }

        cursorContacto.close();

    }

    public void listViewContactos() {

        adaptador = new ContactoAdapter(this, listaContactos);
        lvContactos = (ListView) findViewById(R.id.lvContactos);
        lvContactos.setAdapter(adaptador);

        lvContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long lid) {
                int item = position;

                TextView itemView = (TextView) view.findViewById(R.id.tvId);

                id = Integer.parseInt(itemView.getText().toString());

                Toast.makeText(getApplicationContext(), "" + id, Toast.LENGTH_SHORT).show();

                pulsarContacto(view, (int) id);

            }

        });

        lvContactos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView nameView = (TextView) view.findViewById(R.id.txtNombreApellido);
                TextView itemView = (TextView) view.findViewById(R.id.tvId);

                id = Integer.parseInt(itemView.getText().toString());

                borrarContacto(nameView.getText().toString(), (int) id);

                return true;
            }
        });
    }

    public void btnNuevo(View view) {

        Intent i = new Intent(this, NuevoContacto.class);

        id = 0;

        i.putExtra("id", id);

        startActivity(i);

    }

    public void pulsarContacto(View view, int id) {

        Intent i = new Intent(this, NuevoContacto.class);

        i.putExtra("id", id);

        startActivity(i);

    }

    public void borrarContacto(String nombre, final int id) {
        final BBDD_Create actions = new BBDD_Create(this);

        AlertDialog.Builder borrando = new AlertDialog.Builder(this);

        borrando.setTitle("Borrar contacto");

        borrando.setMessage("Se borrara definitivamente el contacto " + nombre);

        borrando.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actions.deleteContacto(id);
                Toast.makeText(getApplicationContext(), "Se ha borrado correctamente",
                        Toast.LENGTH_SHORT).show();
                actualizarContenido();
                listViewContactos();
            }
        });
        borrando.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = borrando.create();
        alertDialog.show();

    }

    private void busquedas(Menu menu){

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                filtro = newText;

                actualizarContenido();
                listViewContactos();
                //Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                filtro = query;

                actualizarContenido();
                listViewContactos();
                //Toast.makeText(getApplicationContext(), query + " hello", Toast.LENGTH_LONG).show();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

    }
}