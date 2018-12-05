/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.accesoadatos.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Carlos Onorio
 */
@Entity
@Table(name = "cuestionario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuestionario.findAll", query = "SELECT c FROM Cuestionario c")
    , @NamedQuery(name = "Cuestionario.findById", query = "SELECT c FROM Cuestionario c WHERE c.id = :id")
    , @NamedQuery(name = "Cuestionario.findByNombre", query = "SELECT c FROM Cuestionario c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Cuestionario.findByVecesJugado", query = "SELECT c FROM Cuestionario c WHERE c.vecesJugado = :vecesJugado")
    , @NamedQuery(name = "Cuestionario.findByUltimoGanador", query = "SELECT c FROM Cuestionario c WHERE c.ultimoGanador = :ultimoGanador")
    , @NamedQuery(name = "Cuestionario.getCuestionario", query = "SELECT c FROM Cuestionario c WHERE c.autor = :autor AND c.nombre = :nombreCuestionario")
    , @NamedQuery(name = "Cuestionario.getCuestionariosPorAutor", query = "SELECT c FROM Cuestionario c WHERE c.autor = :autor")
    , @NamedQuery(name = "Cuestionario.eliminarCuestionario", query = "DELETE FROM Cuestionario c WHERE c.id = :id")})
public class Cuestionario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "veces_jugado")
    private int vecesJugado;
    @Column(name = "ultimo_ganador")
    private String ultimoGanador;
    @JoinColumn(name = "autor", referencedColumnName = "nombre")
    @ManyToOne(optional = false)
    private Usuario autor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuestionario")
    private Collection<Pregunta> preguntaCollection;

    public Cuestionario() {
    }

    public Cuestionario(Long id) {
        this.id = id;
    }

    public Cuestionario(Long id, String nombre, int vecesJugado) {
        this.id = id;
        this.nombre = nombre;
        this.vecesJugado = vecesJugado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVecesJugado() {
        return vecesJugado;
    }

    public void setVecesJugado(int vecesJugado) {
        this.vecesJugado = vecesJugado;
    }

    public String getUltimoGanador() {
        return ultimoGanador;
    }

    public void setUltimoGanador(String ultimoGanador) {
        this.ultimoGanador = ultimoGanador;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    @XmlTransient
    public Collection<Pregunta> getPreguntaCollection() {
        return preguntaCollection;
    }

    public void setPreguntaCollection(Collection<Pregunta> preguntaCollection) {
        this.preguntaCollection = preguntaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuestionario)) {
            return false;
        }
        Cuestionario other = (Cuestionario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.fei.qa.accesoadatos.entity.Cuestionario[ id=" + id + " ]";
    }
    
}
