package practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Conferencia {


    Boolean encurso;
    String id;
    String nombre;
    String direccion;
    String telefono;
    String horario;
    String sala;
    String ponente;
    GeoPoint localizacion;
    Date fecha;
    int plazas;

    public Conferencia() {
    }

    public Conferencia(String id,String nombre, String direccion, String telefono, String horario, String sala, GeoPoint localizacion, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horario = horario;
        this.sala = sala;
        this.localizacion = localizacion;
        this.fecha = fecha;
    }

    public String getPonente() {
        return ponente;
    }

    public void setPonente(String ponente) {
        this.ponente = ponente;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public Boolean getEncurso() {
        return encurso;
    }

    public void setEncurso(Boolean encurso) {
        this.encurso = encurso;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public void setLocalizacion(GeoPoint localizacion) {
        this.localizacion = localizacion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public GeoPoint getLocalizacion() {
        return localizacion;
    }

    public Uri getUriTelefono(){
        return Uri.parse("tel:"+telefono);
    }

    public String getSala() {
        return sala;
    }

    public String getHorario() {
        return horario;
    }

    public String getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Uri getUriLocalizacion() {
        return Uri.parse("geo:"+localizacion.getLatitude()+","+
                localizacion.getLongitude());
    }

    @Override
    public String toString() {
        return nombre;
    }
}
