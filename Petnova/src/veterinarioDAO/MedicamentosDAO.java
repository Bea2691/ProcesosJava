package veterinarioDAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import PetnovaHN.Grupomedicamentos;
import PetnovaHN.Medicamentos;

import java.util.List;

public class MedicamentosDAO {

    private SessionFactory sessionFactory;

    public MedicamentosDAO() {
        // Configurar Hibernate
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Medicamentos.class)
                .addAnnotatedClass(Grupomedicamentos.class)
                .buildSessionFactory();
    }

    /**
     * Obtiene todos los medicamentos de la base de datos.
     *
     * @return Lista de medicamentos.
     */
    public List<Medicamentos> getAllMedicamentos() {
        Session session = null;
        List<Medicamentos> medicamentos = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Consulta para obtener todos los medicamentos
            Query<Medicamentos> query = session.createQuery("FROM Medicamentos", Medicamentos.class);
            medicamentos = query.getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return medicamentos;
    }
    /**
     * Obtiene un medicamento por su nombre.
     *
     * @param nombreMedicamento Nombre del medicamento.
     * @return Medicamento correspondiente o null si no existe.
     */
    public Medicamentos getMedicamentoPorNombre(String nombreMedicamento) {
        Session session = null;
        Medicamentos medicamento = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Consulta para obtener el medicamento por nombre
            Query<Medicamentos> query = session.createQuery("FROM Medicamentos WHERE nombremedicamento = :nombre", Medicamentos.class);
            query.setParameter("nombre", nombreMedicamento);
            medicamento = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return medicamento;
    }


    /**
     * Cierra la sesión de Hibernate.
     */
    public void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
