package com.example.agenda_v07.BBDD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.agenda_v07.modelo.Contacto;

public class BBDD_Create extends SQLiteOpenHelper {

    public BBDD_Create(Context context) {
        super(context, "Agenda_v3", null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = 1");

        ///CREAR TABLA DE CONTACTOS//
        db.execSQL("CREATE TABLE contacto("+
                "contacto_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL," +
                "foto TEXT)");

        db.execSQL("INSERT INTO contacto (nombre,apellido,foto) " +
                "VALUES ('Pene','Pepo','m')");

        db.execSQL("INSERT INTO contacto (nombre,apellido,foto) " +
                "VALUES ('Pena','Pepi','f')");

        ///CREAR TABLA DE TELEFONOS//
        db.execSQL("CREATE TABLE telefono("+
                "telefono_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "contacto_id INTEGER NOT NULL, " +
                "numero TEXT NOT NULL," +
                "movil TEXT NOT NULL)");

        db.execSQL("INSERT INTO telefono (contacto_id,numero,movil) " +
                "VALUES (1,'1111111','Movil')");

        db.execSQL("INSERT INTO telefono (contacto_id,numero,movil) " +
                "VALUES (1,'222222','Movil')");

        db.execSQL("INSERT INTO telefono (contacto_id,numero,movil) " +
                "VALUES (2,'123456','Fijo')");

        db.execSQL("INSERT INTO telefono (contacto_id,numero,movil) " +
                "VALUES (2,'654321','Fijo')");

        ///CREAR TABLA DE CORREOS//
        db.execSQL("CREATE TABLE correo("+
                "correo_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "contacto_id INTEGER NOT NULL," +
                "correo TEXT NOT NULL)");

        db.execSQL("INSERT INTO correo (contacto_id,correo) " +
                "VALUES (1,'Pepo@correo.es')");

        db.execSQL("INSERT INTO correo (contacto_id,correo) " +
                "VALUES (1,'Pepo2@correo.es')");

        db.execSQL("INSERT INTO correo (contacto_id,correo) " +
                "VALUES (2,'Pepa@correo.es')");

        db.execSQL("INSERT INTO correo (contacto_id,correo) " +
                "VALUES (2,'Pepa2@correo.es')");

        ///CREAR TABLA DE DIRECCION//
        db.execSQL("CREATE TABLE direccion("+
                "direccion_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "contacto_id INTEGER NOT NULL," +
                "calle TEXT NOT NULL," +
                "numero TEXT," +
                "ciudad TEXT," +
                "otros TEXT)");

        db.execSQL("INSERT INTO direccion (contacto_id, calle,numero, ciudad, otros) " +
                "VALUES (1,'Isidro fernandez', 1, 'Madrid', 'Exterior Derecha')");

        db.execSQL("INSERT INTO direccion (contacto_id, calle, numero, ciudad ,otros) " +
                "VALUES (2,'Gran via', 10, 'Madrid', 'Mimimi, Madrid central')");

    }

    public void createContacto(Contacto contacto) {

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO contacto (nombre, apellido, foto) " +
                "VALUES ('" + contacto.getNombre() + "', '" +
                contacto.getApellido() + "', '" +
                contacto.getFoto() + "')");

        setTelefono(contacto);
        setCorreo(contacto);
        setDireccion(contacto);

        db.close();
    }



    public Cursor getListaContactos(){
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT contacto_id, nombre, apellido, foto " +
                        "FROM contacto " +
                        "ORDER BY contacto_id"
                , null);

    }

    public Cursor getFiltroContactos(String string){
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT contacto_id, nombre, apellido, foto " +
                "FROM contacto " +
                "WHERE nombre LIKE '%" + string +"%' OR apellido LIKE '%" + string + "%'" , null);

    }



    public Cursor getContacto(int contacto_id){
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT contacto_id, nombre, apellido, foto " +
                "FROM contacto " +
                "WHERE contacto_id = " + contacto_id, null);

    }

    public Cursor getTelefono(int contacto_id){
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT telefono_id, contacto_id, numero, movil " +
                "FROM telefono " +
                "WHERE contacto_id = " + contacto_id, null);

    }

    public Cursor getCorreo(int contacto_id){
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT correo_id, contacto_id, correo " +
                "FROM correo " +
                "WHERE contacto_id = " + contacto_id, null);

    }

    public Cursor getDireccion(int contacto_id){
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT contacto_id, calle, numero, ciudad, otros " +
                "FROM direccion " +
                "WHERE contacto_id = " + contacto_id, null);
    }

    public int getUlimoId(){

        Cursor idContacto = getListaContactos();
        idContacto.moveToLast();

        return idContacto.getInt(0);
    }



    private void setTelefono(Contacto contacto){
        SQLiteDatabase db = getWritableDatabase();

        for (int i = 0; i < contacto.getArTelefono().size(); i++) {

            db.execSQL("INSERT INTO telefono (contacto_id, numero, movil) " +
                    "VALUES (" + getUlimoId() + ", '" +
                    contacto.getArTelefono().get(i).split(" ")[0] + "', '" +
                    contacto.getArTelefono().get(i).split(" ")[1] + "')");
        }
        db.close();
    }

    private void setCorreo(Contacto contacto){
        SQLiteDatabase db = getWritableDatabase();

        for (int i = 0; i < contacto.getCorreo().size(); i++) {

            db.execSQL("INSERT INTO correo (contacto_id, correo) " +
                    "VALUES (" + getUlimoId() + ", '" +
                    contacto.getCorreo().get(i) + "')");
        }
        db.close();
    }

    private void setDireccion(Contacto contacto){
        SQLiteDatabase db = getWritableDatabase();

            db.execSQL("INSERT INTO direccion (contacto_id, calle, numero, ciudad ,otros) " +
                    "VALUES (" + getUlimoId() + ", '" +
                    contacto.getDireccion().getCalle() + "', '" +
                    contacto.getDireccion().getNumero() + "', '" +
                    contacto.getDireccion().getCiudad() + "', '" +
                    contacto.getDireccion().getOtros() + "')");

        db.close();
    }



    public void deleteContacto(int contacto_id){
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("DELETE FROM contacto " +
                "WHERE contacto_id = " + contacto_id);

        db.execSQL("DELETE FROM telefono " +
                "WHERE contacto_id = " + contacto_id);

        db.execSQL("DELETE FROM correo " +
                "WHERE contacto_id = " + contacto_id);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacto");

        onCreate(db);

    }

}