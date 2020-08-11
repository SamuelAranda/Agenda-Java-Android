package com.example.agenda_v07;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda_v07.modelo.Contacto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.net.Uri.parse;


public class ContactoAdapter extends ArrayAdapter<Contacto> {

    AppCompatActivity appCompatActivity;
    ArrayList<Contacto> listaContactos;
    Uri imageUri;

    public ContactoAdapter(AppCompatActivity context, ArrayList<?> listaContactos) {
        super(context, R.layout.mini_contacto, (List<Contacto>) listaContactos);
        appCompatActivity = context;

        this.listaContactos = (ArrayList<Contacto>) listaContactos;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        View item = inflater.inflate(R.layout.mini_contacto, null);

        TextView tvId = (TextView) item.findViewById(R.id.tvId);
        tvId.setText("" + listaContactos.get(position).getId());


        String NombreApellido = listaContactos.get(position).getNombre() + " " +
                listaContactos.get(position).getApellido();

        TextView tpNombreApellido = (TextView)item.findViewById(R.id.txtNombreApellido);
        tpNombreApellido.setText(NombreApellido);

        ImageView imagen =(ImageView) item.findViewById(R.id.imgContacto);
        imagen.setImageResource(R.mipmap.personasdecontacto);

        imagen.setImageDrawable(resizeImage(this.appCompatActivity, Uri.parse(listaContactos.get(position).getFoto()), 50, 50));



        /*  try{
            Bitmap bitmap = null;
            File fichero = new File(listaContactos.get(position).getFoto());
            bitmap = BitmapFactory.decodeFile("/phonestorage/DCIM/Camera/IMG_20190519_145238.JPG");

            imagen.setImageBitmap(bitmap);
        }catch (Exception e){

        }*/

        return(item);
    }

    public Drawable resizeImage(Context ctx, Uri uri, int w, int h) {

        try {
            Bitmap bitmap;
            bitmap = MediaStore.Images.Media.getBitmap(this.appCompatActivity.getContentResolver(), uri);
            // cargamos la imagen de origen
      // Bitmap BitmapOrg = BitmapFactory.decodeResource(ctx.getResources(),resId);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // para poder manipular la imagen
        // debemos crear una matriz

        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);

        // si queremos poder mostrar nuestra imagen tenemos que crear un
        // objeto drawable y así asignarlo a un botón, imageview...
        return new BitmapDrawable(resizedBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
