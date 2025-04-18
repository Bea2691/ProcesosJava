package PetnovaHN;

import java.math.BigDecimal;

/**
 * Medicamentos generated by hbm2java
 */
public class Medicamentos implements java.io.Serializable {

	private Integer idmedicamento;
	private Grupomedicamentos grupomedicamentos;
	private String nombremedicamento;
	private String descripcion; // Nuevo campo añadido
	private BigDecimal dosis;

	public Medicamentos() {
	}

	public Medicamentos(Grupomedicamentos grupomedicamentos, String nombremedicamento, String descripcion, BigDecimal dosis) {
		this.grupomedicamentos = grupomedicamentos;
		this.nombremedicamento = nombremedicamento;
		this.descripcion = descripcion;
		this.dosis = dosis;
	}

	public Integer getIdmedicamento() {
		return this.idmedicamento;
	}

	public void setIdmedicamento(Integer idmedicamento) {
		this.idmedicamento = idmedicamento;
	}

	public Grupomedicamentos getGrupomedicamentos() {
		return this.grupomedicamentos;
	}

	public void setGrupomedicamentos(Grupomedicamentos grupomedicamentos) {
		this.grupomedicamentos = grupomedicamentos;
	}

	public String getNombremedicamento() {
		return this.nombremedicamento;
	}

	public void setNombremedicamento(String nombremedicamento) {
		this.nombremedicamento = nombremedicamento;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getDosis() {
		return this.dosis;
	}

	public void setDosis(BigDecimal dosis) {
		this.dosis = dosis;
	}
}
