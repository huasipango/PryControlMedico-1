package com.medico.app.web.models.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name="DOSIS")
public class Dosis implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IDDOSIS")
	private Integer iddosis;

    @JoinColumn(name="IDDETALLERECETA", referencedColumnName = "IDDETALLERECETA")//claves foraneas
    @ManyToOne
    private DetalleReceta detalleReceta;

    @Transient
    private String descripcionDetalleReceta;

	@Column(name = "FECHAHORA")
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private Date fechaHora;
	
	@Column(name = "NUMERO")
	@Min(value = 1)
	private Integer numero;
	
	@Size(max = 255)
	@Column(name = "DESCRIPCION")
	private String descripcion;//justificacion de por qué se interrumpe el tratamiento
	
	@Column(name = "ESTADO")
	@Min(value = 0)
	private Integer estado;

	public Dosis() {
		super();
	}

	public Dosis(Integer iddosis) {
		super();
		this.iddosis = iddosis;
	}

	public Integer getIddosis() {
		return iddosis;
	}

	public void setIddosis(Integer iddosis) {
		this.iddosis = iddosis;
	}

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

    public DetalleReceta getDetalleReceta() {
        return detalleReceta;
    }

    public void setDetalleReceta(DetalleReceta detalleReceta) {
        this.detalleReceta = detalleReceta;
    }

    public String getDescripcionDetalleReceta() {
        switch(this.estado) {
            case 0:
                return "Tomada";
            case 1:
                return "Pendiente";
            case 2:
                return "Por tomar";
        }
        return "";
    }
}
