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
public class RespuestaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_cuestionario")
    private long idCuestionario;
    @Basic(optional = false)
    @Column(name = "numero_pregunta")
    private int numeroPregunta;
    @Basic(optional = false)
    @Column(name = "letra")
    private Character letra;

    public RespuestaPK() {
    }

    public RespuestaPK(long idCuestionario, int numeroPregunta, Character letra) {
        this.idCuestionario = idCuestionario;
        this.numeroPregunta = numeroPregunta;
        this.letra = letra;
    }

    public long getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(long idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public int getNumeroPregunta() {
        return numeroPregunta;
    }

    public void setNumeroPregunta(int numeroPregunta) {
        this.numeroPregunta = numeroPregunta;
    }

    public Character getLetra() {
        return letra;
    }

    public void setLetra(Character letra) {
        this.letra = letra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCuestionario;
        hash += (int) numeroPregunta;
        hash += (letra != null ? letra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RespuestaPK)) {
            return false;
        }
        RespuestaPK other = (RespuestaPK) object;
        if (this.idCuestionario != other.idCuestionario) {
            return false;
        }
        if (this.numeroPregunta != other.numeroPregunta) {
            return false;
        }
        if ((this.letra == null && other.letra != null) || (this.letra != null && !this.letra.equals(other.letra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.fei.qa.accesoadatos.entity.RespuestaPK[ idCuestionario=" + idCuestionario + ", numeroPregunta=" + numeroPregunta + ", letra=" + letra + " ]";
    }
    
}
