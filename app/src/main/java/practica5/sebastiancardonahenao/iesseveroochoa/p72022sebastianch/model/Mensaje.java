package practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable {

    public String usuario;
    public String body;
    //@ServerTimestamp permite que sea el servidor el que asigne la hora al
    //crear el documento
    @ServerTimestamp
    Date fechaCreacion;

    public Mensaje() {
    }

    public Mensaje(String usuario, String body) {
        this.usuario = usuario;
        this.body = body;
    }

    public Mensaje(String usuario, String body, Date fechaCreacion) {
        this.usuario = usuario;
        this.body = body;
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
