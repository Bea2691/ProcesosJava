package PetnovaHN;

// Generated 23 ene 2025, 19:11:43 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Trabajadores generated by hbm2java
 */
@Entity
@Table(name = "trabajadores")
public class Trabajadores implements java.io.Serializable {

	@Id
	@Column(name = "dnitaux", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) // or GenerationType.AUTO if you prefer
	private String dnitaux;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamentos departamentos;

    @ManyToOne
    @JoinColumn(name = "dnitaux", nullable = false)
    private Personas personas;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "pass")
    private String pass;

    @OneToMany(mappedBy = "trabajadores")
    private Set ausenciaslaboraleses = new HashSet(0);

    @OneToMany(mappedBy = "trabajadores")
    private Set chatsForDnitreceptoraux = new HashSet(0);

    @OneToMany(mappedBy = "trabajadores")
    private Set adiestramientos = new HashSet(0);

    @OneToMany(mappedBy = "trabajadores")
    private Set chatsForDnitemisoraux = new HashSet(0);

    public Trabajadores() {
    }

    public Trabajadores(Departamentos departamentos, Personas personas) {
        this.departamentos = departamentos;
        this.personas = personas;
    }

    public Trabajadores(Departamentos departamentos, Personas personas, String usuario, String pass,
            Set ausenciaslaboraleses, Set chatsForDnitreceptoraux, Set adiestramientos, Set chatsForDnitemisoraux) {
        this.departamentos = departamentos;
        this.personas = personas;
        this.usuario = usuario;
        this.pass = pass;
        this.ausenciaslaboraleses = ausenciaslaboraleses;
        this.chatsForDnitreceptoraux = chatsForDnitreceptoraux;
        this.adiestramientos = adiestramientos;
        this.chatsForDnitemisoraux = chatsForDnitemisoraux;
    }

    public String getDnitaux() {
        return this.dnitaux;
    }

    public void setDnitaux(String dnitaux) {
        this.dnitaux = dnitaux;
    }

    public Departamentos getDepartamentos() {
        return this.departamentos;
    }

    public void setDepartamentos(Departamentos departamentos) {
        this.departamentos = departamentos;
    }

    public Personas getPersonas() {
        return this.personas;
    }

    public void setPersonas(Personas personas) {
        this.personas = personas;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Set getAusenciaslaboraleses() {
        return this.ausenciaslaboraleses;
    }

    public void setAusenciaslaboraleses(Set ausenciaslaboraleses) {
        this.ausenciaslaboraleses = ausenciaslaboraleses;
    }

    public Set getChatsForDnitreceptoraux() {
        return this.chatsForDnitreceptoraux;
    }

    public void setChatsForDnitreceptoraux(Set chatsForDnitreceptoraux) {
        this.chatsForDnitreceptoraux = chatsForDnitreceptoraux;
    }

    public Set getAdiestramientos() {
        return this.adiestramientos;
    }

    public void setAdiestramientos(Set adiestramientos) {
        this.adiestramientos = adiestramientos;
    }

    public Set getChatsForDnitemisoraux() {
        return this.chatsForDnitemisoraux;
    }

    public void setChatsForDnitemisoraux(Set chatsForDnitemisoraux) {
        this.chatsForDnitemisoraux = chatsForDnitemisoraux;
    }

}
