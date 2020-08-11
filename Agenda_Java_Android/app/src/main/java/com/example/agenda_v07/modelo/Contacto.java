package com.example.agenda_v07.modelo;

import java.util.ArrayList;

public class Contacto {

    private int id;
    private String nombre;
    private String apellido;
    private ArrayList<String> arTelefono;
    private Direccion direccion;
    private ArrayList<String> correo;
    private String foto;

    public Contacto(String nombre, String apellido, String telefono, String foto){

        this.arTelefono = new ArrayList<String>();

        this.nombre = nombre;
        this.apellido = apellido;
        this.arTelefono.add(telefono);
        this.foto = foto;

    }

    public Contacto(){

        this.arTelefono = new ArrayList<String>();
        this.correo = new ArrayList<String>();

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public ArrayList<String> getArTelefono() {
        return arTelefono;
    }

    public void setArTelefono(String telefono) {
        this.arTelefono.add(telefono);
    }

    public void setArTelefono(ArrayList<String> arTelefono){
        this.arTelefono = arTelefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ArrayList<String> getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo.add(correo);
    }

    public void setCorreo(ArrayList<String> correos) {
        this.correo = correos;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
