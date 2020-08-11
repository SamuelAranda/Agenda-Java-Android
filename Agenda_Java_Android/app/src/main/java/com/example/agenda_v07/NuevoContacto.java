package com.example.agenda_v07;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda_v07.BBDD.BBDD_Create;
import com.example.agenda_v07.modelo.Contacto;
import com.example.agenda_v07.modelo.Direccion;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class NuevoContacto extends Activity {

    private TextView nombre;
    private TextView apellido;
    private TextView tvTelefono;
    private TextView tvcorreo;
    private ImageView imagen;
    private Uri imageUri;

    private TextView tvCalle;
    private TextView tvCiudad;
    private TextView tvNumero;
    private TextView tvOtrosDatos;

    private ListView lvNumerosAgregados;
    private ListView lvCorreosAgregados;

    private Button introducir;
    private Button borrar;
    private Button abrirMapa;

    private CheckBox chTelefono;

    private ArrayList<String> arrayTelefonos;
    private ArrayList<String> arrayCorreos;

    private Contacto contacto;
    private Direccion direccion;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_contacto);

        nombre = (TextView) findViewById(R.id.etNombre);
        apellido = (TextView) findViewById(R.id.etApellido);
        tvTelefono = (TextView) findViewById(R.id.etTelefono);
        tvcorreo = (TextView) findViewById(R.id.etCorreo);
        imagen = (ImageView) findViewById(R.id.imageView);

        tvCalle = (TextView) findViewById(R.id.etCalle);
        tvCiudad = (TextView) findViewById(R.id.etCiudad);
        tvNumero = (TextView) findViewById(R.id.etNumero);
        tvOtrosDatos = (TextView) findViewById(R.id.etDatosAdicionales);

        introducir = (Button) findViewById(R.id.btModificar);
        borrar = (Button) findViewById(R.id.btBorrar);
        abrirMapa = (Button) findViewById(R.id.btnIrDireccion);

        lvNumerosAgregados = (ListView) findViewById(R.id.lvTelefonasAgregados);
        lvCorreosAgregados = (ListView) findViewById(R.id.lvCorreoAgregados);

        chTelefono = (CheckBox) findViewById(R.id.cbTelefono1);

        arrayTelefonos = new ArrayList<String>();
        arrayCorreos = new ArrayList<String>();

        contacto = new Contacto();
        direccion = new Direccion();

        imagen.setImageResource(R.mipmap.personasdecontacto);

        Bundle bId = this.getIntent().getExtras();
        id = bId.getInt("id");

        if (id != 0) {
            abrirMapa.setEnabled(true);
            borrar.setEnabled(true);

            introducir.setText("Modificar");
            try {
                mostrarDatosContacto(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery,100);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data.getData();
            Bitmap bitmap;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imagen.setImageBitmap(bitmap);

                Toast.makeText(getApplicationContext(), imageUri.toString(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //CARGAR/INTRODUCIR/MODIFICAR CONTACTOS//
    public void introducirContacto(){

        try{
            direccion = new Direccion();
            direccion.setCalle(tvCalle.getText().toString());
            direccion.setCiudad((tvCiudad.getText().toString()));
            direccion.setNumero(tvNumero.getText().toString());
            direccion.setOtros(tvOtrosDatos.getText().toString());

        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "No ha introdcuido ninguna dirección",
                    Toast.LENGTH_LONG).show();
        }

        try {

            contacto.setNombre(nombre.getText().toString());
            contacto.setApellido(apellido.getText().toString());
            contacto.setFoto(imageUri.toString());
            contacto.setArTelefono(arrayTelefonos);
            contacto.setCorreo(arrayCorreos);
            contacto.setDireccion(direccion);

            if (nombre.getText().length() == 0
                    || apellido.getText().length() == 0
                    || contacto.getArTelefono().size() == 0) {
                Toast.makeText(getApplicationContext(), "Debe rellenar nombre, apellido y " +
                        "al menos un telefono", Toast.LENGTH_LONG).show();
            } else {

                BBDD_Create manager = new BBDD_Create(this);
                manager.createContacto(contacto);

                Toast.makeText(getApplicationContext(),
                        "Se ha añadido correctamente el contacto",
                        Toast.LENGTH_SHORT).show();

                this.finish();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Error catastrofico",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarDatosContacto(int id) throws IOException {

        BBDD_Create actions = new BBDD_Create(this);

        Cursor cursorContacto = actions.getContacto(id);
        cursorContacto.moveToFirst();

        nombre.setText(cursorContacto.getString(1));
        apellido.setText(cursorContacto.getString(2));

        String uri = cursorContacto.getString(3);

        imageUri = imageUri.parse(uri);

        Toast.makeText(getApplicationContext(), imageUri.toString(), Toast.LENGTH_LONG).show();

        if(imageUri==null){
            imagen.setImageResource(R.mipmap.personasdecontacto);
        }
        else {
            try {
                Bitmap bitmap;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        // imagen.setImageURI(cursorContacto.getString(3));

       /* if(imageUri==null){
            imagen.setImageResource(R.mipmap.hombre);
        }
        else{
            try {
                Bitmap bitmap;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //imagen.setImageURI(imageUri);
        }*/


        Cursor cursorTelefono = actions.getTelefono(id);
        while (cursorTelefono.moveToNext()){

            arrayTelefonos.add(cursorTelefono.getString(2) + " " +
                    cursorTelefono.getString(3));

        }
        vistaListaTelefono();


        Cursor cursorCorreo = actions.getCorreo(id);
        while (cursorCorreo.moveToNext()){

            arrayCorreos.add(cursorCorreo.getString(2));
        }
        vistaListaCorreo();


        Cursor cursorDireccion = actions.getDireccion(id);
        cursorDireccion.moveToFirst();

        try {
            tvCalle.setText(cursorDireccion.getString(1));
            tvNumero.setText(cursorDireccion.getString(2));
            tvOtrosDatos.setText(cursorDireccion.getString(4));
            tvCiudad.setText(cursorDireccion.getString(3));

            cursorCorreo.close();

        } catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    "Este contacto aún no tiene direccion completa", Toast.LENGTH_LONG).show();
        }

        cursorContacto.close();
        cursorTelefono.close();
        cursorDireccion.close();

    }

    public void modificarContacto(){

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(NuevoContacto.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Está seguro de querer modificar? Los cambios serán permanentes");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogo1, int idDialog) {
                BBDD_Create manager = new BBDD_Create(getBaseContext());
                manager.deleteContacto(id);

                introducirContacto();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        dialogo1.show();

    }



    //GENERAR LISTVIEWS//
    private void vistaListaTelefono(){

        final ArrayAdapter<String> tlfAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayTelefonos);

        lvNumerosAgregados.setAdapter(tlfAdapter);

        //borrar al mantener pulsado//
        lvNumerosAgregados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicion, long l) {

                borrarElemento(arrayTelefonos, posicion, tlfAdapter);
                return true;
            }
        });

        //llamar al pulsar//
            lvNumerosAgregados.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                    String numero = arrayTelefonos.get(position).split(" ")[0].toString();
                    llamarDial(numero);

                }

            });

        Utility.setListViewHeightBasedOnChildren(lvNumerosAgregados);
    }

    private void vistaListaCorreo(){

        final ArrayAdapter<String> corAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayCorreos);

        lvCorreosAgregados.setAdapter(corAdapter);

        //borrar al mantener pulsado//
        lvCorreosAgregados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicion, long l) {

                borrarElemento(arrayCorreos, posicion, corAdapter);
                return true;
            }
        });


        //enviar correo al pulsar//
        lvCorreosAgregados.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                    String correo = arrayCorreos.get(position);
                    enviarEmail(correo);
                }
            });

        Utility.setListViewHeightBasedOnChildren(lvCorreosAgregados);
    }



    //BOTONES//
    public void btnIntroducirNumero(View v){

        if (tvTelefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),
                    "No has añadido ningún numemro", Toast.LENGTH_SHORT).show();
        }else{
            String movil = "Movil";
            if (chTelefono.isChecked()) {movil = "Fijo";}

            arrayTelefonos.add(tvTelefono.getText().toString() + " " + movil);
            vistaListaTelefono();

            tvTelefono.setText("");
        }
    }

    public void btnIntroducirCorreo(View v){


        if (tvcorreo.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),
                    "No has añadido ningún correo", Toast.LENGTH_SHORT).show();
        }else {
            arrayCorreos.add(tvcorreo.getText().toString());
            vistaListaCorreo();

            tvcorreo.setText("");
        }

    }

    public void btnIntroducirContacto(View v){
        if (id != 0){
            modificarContacto();
        }
        else {
            introducirContacto();
        }

    }




    //BORRAR ELEMENTOS DE LAS LISTAS//
    private void borrarElemento(final ArrayList<?> array, final int posicion,
                                final ArrayAdapter<String> adapter) {

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(NuevoContacto.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Está seguro de querer eliminar?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogo1, int id) {
                array.remove(posicion);
                adapter.notifyDataSetChanged();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        dialogo1.show();

    }




    //EJECUTABLES//
    public void llamarDial(String numero){

        Uri number = Uri.parse("tel:" + numero);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
        return;
    }

    public void enviarEmail(String email) {
        Intent emailintent = new Intent(Intent.ACTION_SENDTO);
        emailintent.setData(Uri.parse("mailto:" + email));

        if (emailintent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailintent);
        }
    }

    public void verEnMapa (View view) {

        String dir = tvCalle.getText().toString() + ", " +
                tvNumero.getText().toString() + ", " +
                tvCiudad.getText().toString();

        Intent verMapa = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + dir);
        verMapa.setData(geoLocation);
        if (verMapa.resolveActivity(getPackageManager()) != null) {
            startActivity(verMapa);
        }
    }
}