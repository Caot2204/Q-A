/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.accesoadatos.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos Onorio
 */
@Entity
@Table(name = "respuesta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Respuesta.findAll", query = "SELECT r FROM Respuesta r")
    , @NamedQuery(name = "Respuesta.findByIdCuestionario", query = "SELECT r FROM Respuesta r WHERE r.respuestaPK.idCuestionario = :idCuestionario")
    , @NamedQuery(name = "Respuesta.findByNumeroPregunta", query = "SELECT r FROM Respuesta r WHERE r.respuestaPK.numeroPregunta = :numeroPregunta")
    , @NamedQuery(name = "Respuesta.findByLetra", query = "SELECT r FROM Respuesta r WHERE r.respuestaPK.letra = :letra")
    , @NamedQuery(name = "Respuesta.findByDescripcion", query = "SELECT r FROM Respuesta r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "Respuesta.findByCorrecta", query = "SELECT r FROM Respuesta r WHERE r.correcta = :correcta")
    , @NamedQuery(name = "Respuesta.getRespuestasDePregunta", query = "SELECT r FROM Respuesta r WHERE r.respuestaPK.idCuestionario = :idCuestionario AND r.respuestaPK.numeroPregunta = :numeroPregunta")})
public class Respuesta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RespuestaPK respuestaPK;
    @Column(name = "descripcion")
    private String descripcion;
    @Lob
    @Column(name = "imagen")
    private byte[] imagen;
    @Basic(optional = false)
    @Column(name = "correcta")
    private boolean correcta;

    public Respuesta() {
    }

    public Respuesta(RespuestaPK respuestaPK) {
        this.respuestaPK = respuestaPK;
    }

    public Respuesta(RespuestaPK respuestaPK, boolean correcta) {
        this.respuestaPK = respuestaPK;
        this.correcta = correcta;
    }

    public Respuesta(long idCuestionario, int numeroPregunta, Character letra) {
        this.respuestaPK = new RespuestaPK(idCuestionario, numeroPregunta, letra);
    }

    public RespuestaPK getRespuestaPK() {
        return respuestaPK;
    }

    public void setRespuestaPK(RespuestaPK respuestaPK) {
        this.respuestaPK = respuestaPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public boolean getCorrecta() {
        return correcta;
    }

    public void setCorrecta(boolean correcta) {
        this.correcta = correcta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (respuestaPK != null ? respuestaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Respuesta)) {
            return false;
        }
        Respuesta other = (Respuesta) object;
        if ((this.respuestaPK == null && other.respuestaPK != null) || (this.respuestaPK != null && !this.respuestaPK.equals(other.respuestaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.fei.qa.accesoadatos.entity.Respuesta[ respuestaPK=" + respuestaPK + " ]";
    }

}
