/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.accesoadatos.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Carlos Onorio
 */
@Embeddable
public class PreguntaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_cuestionario")
    private long idCuestionario;
    @Basic(optional = false)
    @Column(name = "numero")
    private int numero;

    public PreguntaPK() {
    }

    public PreguntaPK(long idCuestionario, int numero) {
        this.idCuestionario = idCuestionario;
        this.numero = numero;
    }

    public long getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(long idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCuestionario;
        hash += (int) numero;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreguntaPK)) {
            return false;
        }
        PreguntaPK other = (PreguntaPK) object;
        if (this.idCuestionario != other.idCuestionario) {
            return false;
        }
        if (this.numero != other.numero) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.fei.qa.accesoadatos.entity.PreguntaPK[ idCuestionario=" + idCuestionario + ", numero=" + numero + " ]";
    }
    
}
