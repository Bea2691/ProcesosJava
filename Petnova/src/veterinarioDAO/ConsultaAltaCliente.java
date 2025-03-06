package veterinarioDAO;

import java.awt.Desktop;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import PetnovaHN.Citas;
import PetnovaHN.Clientes;
import PetnovaHN.Consultas;
import PetnovaHN.Consultaservicios;
import PetnovaHN.Direccion;
import PetnovaHN.Facturas;
import PetnovaHN.HibernateUtil;
import PetnovaHN.Mascotas;
import PetnovaHN.Paises;
import PetnovaHN.Personas;
import PetnovaHN.Provincias;
import PetnovaHN.Razamascota;
import PetnovaHN.Tipomascota;

public class ConsultaAltaCliente {

	private SessionFactory sessionFactory;

	public ConsultaAltaCliente() {
		this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Personas.class)
				.buildSessionFactory();
	}

	public Personas buscarPersonaPorDni(String dni) {
		
		Session session = sessionFactory.openSession();
		Personas persona = null;

		try {
			
			session.beginTransaction();

			
			persona = session.get(Personas.class, dni);

			if (persona != null) {
			
				Direccion direccion = persona.getDireccion();
				if (direccion != null) {
			
					Provincias provincia = direccion.getProvincias();
					if (provincia != null) {
			
						Paises pais = provincia.getPaises();
						if (pais != null) {
							
							System.out.println("Persona: " + persona.getNombre() + " " + persona.getApellido());
							System.out.println("Dirección: " + direccion.getCalle());
							System.out.println("Provincia: " + provincia.getNombreprovincia());
							System.out.println("País: " + pais.getNombrepais());
						}
					}
				}
			}

			
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
		} finally {
			
			session.close();
		}

		return persona;
	}
	public void guardarCliente(String dni, String nombre, String apellido, String telefono, String email, String calle, String nombreprovincia, String nobmrepais) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Transaction transaction = null;

	    try {
	      
	        Personas personaExistente = buscarPersonaPorDni(dni);
	        if (personaExistente != null) {
	      
	            JOptionPane.showMessageDialog(null, "Cliente con DNI " + dni + " ya existe.");
	            return;
	        }

	        transaction = session.beginTransaction();

	      
	        Provincias provincia = (Provincias) session.createQuery("FROM Provincias WHERE nombreprovincia = :nombreProvincia")
	                                                   .setParameter("nombreProvincia", nombreprovincia)
	                                                   .uniqueResult();

	        if (provincia == null) {
	            JOptionPane.showMessageDialog(null, "Provincia no encontrada.");
	            return;
	        }

	        Paises pais = provincia.getPaises(); 
	        if (pais == null) {
	            JOptionPane.showMessageDialog(null, "No se encontró el país asociado a la provincia.");
	            return;
	        }

	       
	        Direccion direccion = new Direccion();
	        direccion.setCalle(calle);
	        direccion.setProvincias(provincia); 
	        provincia.setPaises(pais); 
	        session.save(direccion); 

	       
	        Personas persona = new Personas();
	        persona.setDni(dni);
	        persona.setNombre(nombre);
	        persona.setApellido(apellido);
	        persona.setTelefono(Integer.parseInt(telefono));
	        persona.setEmail(email);
	        persona.setDireccion(direccion); 
	        session.save(persona); 

	       
	        Clientes cliente = new Clientes();
	        cliente.setDnicaux(dni); 
	        java.sql.Date fechaAlta = new java.sql.Date(System.currentTimeMillis());
	        cliente.setFechaalta(fechaAlta);
	        cliente.setPersonas(persona); 
	        session.save(cliente);

	        transaction.commit();

	        JOptionPane.showMessageDialog(null, "Cliente guardado exitosamente.");

	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback(); 
	        }
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Ocurrió un error al guardar el cliente.");
	    } finally {
	        session.close(); 
	    }
	}

	
	public static void cargarPaises(JComboBox<String> cbPais) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	        session.beginTransaction();

	        List<Paises> paises = session.createCriteria(Paises.class).list();

	        cbPais.removeAllItems(); 

	        for (Paises pais : paises) {
	            cbPais.addItem(pais.getNombrepais());
	        }

	        session.getTransaction().commit();
	    } catch (Exception e) {
	        if (session.getTransaction() != null) {
	            session.getTransaction().rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	}
	public static void cargarProvinciasPorPais(String paisSeleccionado, JComboBox<String> cbProvincia) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Transaction tx = session.beginTransaction();

	        
	        CriteriaBuilder builder = session.getCriteriaBuilder();
	        CriteriaQuery<Paises> query = builder.createQuery(Paises.class);
	        Root<Paises> root = query.from(Paises.class);
	        query.select(root).where(builder.equal(root.get("nombrepais"), paisSeleccionado));

	        Paises pais = session.createQuery(query).uniqueResult();

	        if (pais != null) {
	           
	            CriteriaQuery<Provincias> queryProvincias = builder.createQuery(Provincias.class);
	            Root<Provincias> rootProvincias = queryProvincias.from(Provincias.class);
	            queryProvincias.select(rootProvincias)
	                           .where(builder.equal(rootProvincias.get("paises"), pais));

	            List<Provincias> provincias = session.createQuery(queryProvincias).getResultList();

	            cbProvincia.removeAllItems(); 

	            for (Provincias provincia : provincias) {
	                cbProvincia.addItem(provincia.getNombreprovincia()); 
	            }
	        }

	        tx.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	




	public void guardarMascota(int numChip, String nombremascota, Date fechanacimiento, String sexo, Integer idRaza,
			String observaciones, String dniPersona) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();


			Razamascota razaMascota = session.get(Razamascota.class, idRaza);
			if (razaMascota == null) {
				throw new HibernateException("Raza no encontrada con ID: " + idRaza);
			}

			
			Personas persona = (Personas) session.createQuery("FROM Personas WHERE dni = :dni")
					.setParameter("dni", dniPersona).uniqueResult();
			if (persona == null) {
				throw new HibernateException("Persona no encontrada con DNI: " + dniPersona);
			}

			
			Mascotas nuevaMascota = new Mascotas();
			nuevaMascota.setNchip(numChip); 
			nuevaMascota.setNombremascota(nombremascota);
			nuevaMascota.setFechanacimiento(fechanacimiento);
			nuevaMascota.setSexo(sexo);
			nuevaMascota.setRazamascota(razaMascota); 
			nuevaMascota.setPersonas(persona); 
			nuevaMascota.setObservaciones(observaciones);

			
			session.save(nuevaMascota);

			
			transaction.commit();

			System.out.println("Mascota guardada exitosamente.");
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	public List<String> buscarPersonaYMascotaPorDni(String dni) {
	    
	    Session session = sessionFactory.openSession();
	    List<String> resultados = new ArrayList<>();

	    try {
	        
	        session.beginTransaction();

	        
	        Personas persona = session.get(Personas.class, dni);

	        if (persona != null) {
	            
	            List<Mascotas> mascotas = session.createQuery(
	                "FROM Mascotas WHERE dnipaux = :dni", Mascotas.class)
	                .setParameter("dni", dni)
	                .getResultList();

	            if (!mascotas.isEmpty()) {
	                
	                for (Mascotas mascota : mascotas) {
	                    resultados.add(persona.getNombre()  + " - " + mascota.getNombremascota() + " " + mascota.getNchip());
	                }
	            } else {
	                resultados.add(persona.getNombre() + " " + persona.getApellido() + " - Sin mascotas");
	            }
	        }
	        
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	        
	        if (session.getTransaction() != null) {
	            session.getTransaction().rollback();
	        }
	    } finally {
	        
	        session.close();
	    }

	    return resultados;
	}

	
	public static void guardarCitaEnBaseDeDatos(Citas nuevaCita) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Transaction transaction = null;

	    try {
	        transaction = session.beginTransaction();
	        session.save(nuevaCita); 
	        transaction.commit();
	        System.out.println("Cita guardada exitosamente.");
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback(); 
	        }
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	}
	public static void eliminarCitaDeBaseDeDatos(Citas citaAEliminar) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Transaction transaction = null;

	    try {
	        transaction = session.beginTransaction();

	      
	        Citas citaEnDB = session.get(Citas.class, citaAEliminar.getIdcita());

	        if (citaEnDB != null) {
	            session.delete(citaEnDB); 
	            transaction.commit();
	            System.out.println("Cita eliminada exitosamente.");
	        } else {
	            System.out.println("La cita no existe en la base de datos.");
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback(); 
	        }
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	}
	public List<Object[]> buscarMascotasPorDni(String dniPersona) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Object[]> mascotas = null;

		try {
			session.beginTransaction();

			
			String hql = "SELECT m.nombremascota, t.nombretipo, r.nombreraza, m.nchip, m.sexo " + "FROM Mascotas m "
					+ "JOIN m.razamascota r " + "JOIN r.tipomascota t " + "WHERE m.personas.dni = :dni";

			Query<Object[]> query = session.createQuery(hql);
			query.setParameter("dni", dniPersona);

			
			mascotas = query.list();

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return mascotas;
	}
	 public static void abrirPDFDesdeBD(Integer idFactura) {
	        Session session = HibernateUtil.getSessionFactory().openSession();

	        try {
	            session.beginTransaction();
	            String hql = "SELECT f.archivopdf FROM Facturas f WHERE f.idfactura = :id";
	            Query<byte[]> query = session.createQuery(hql);
	            query.setParameter("id", idFactura);

	            byte[] archivoBytes = query.uniqueResult();
	            session.getTransaction().commit();

	            if (archivoBytes != null) {
	                //Crear un archivo temporal
	                File tempFile = File.createTempFile("Factura_" + idFactura, ".pdf");
	                Files.write(tempFile.toPath(), archivoBytes, StandardOpenOption.CREATE);

	                //Abrir el PDF con el visor predeterminado
	                Desktop.getDesktop().open(tempFile);

	                //Marcar el archivo para ser eliminado al cerrar la aplicación
	                tempFile.deleteOnExit();
	            } else {
	                JOptionPane.showMessageDialog(null, "No se encontró el archivo PDF en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            session.close();
	        }
	    }
	 public static List<Object[]> buscarFacturasPorDni(String dniPersona) {
		    Session session = HibernateUtil.getSessionFactory().openSession();
		    List<Object[]> facturas = null;

		    try {
		        
		        CriteriaBuilder cb = session.getCriteriaBuilder();
		        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

		        
		        Root<Facturas> facturaRoot = cq.from(Facturas.class);
		        Join<Facturas, Personas> personaJoin = facturaRoot.join("personas");

		        
		        cq.multiselect(facturaRoot.get("idfactura"), facturaRoot.get("fechafactura"), facturaRoot.get("archivopdf"));

		        
		        cq.where(cb.equal(personaJoin.get("dni"), dniPersona));

		        
		        Query<Object[]> query = session.createQuery(cq);
		        facturas = query.getResultList();

		       
		        if (facturas.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "No se encontraron facturas para el DNI proporcionado.");
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Ocurrió un error al obtener las facturas.");
		    } finally {
		        session.close();
		    }

		    return facturas;
		}



	public static List<Object[]> obtenerConsultasPorDni(String dni) {
	    List<Object[]> resultados = new ArrayList<>();

	    
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

	        
	        String hqlMascota = "FROM Mascotas m WHERE m.personas.dni = :dni";
	        Query<Mascotas> queryMascota = session.createQuery(hqlMascota, Mascotas.class);
	        queryMascota.setParameter("dni", dni);
	        List<Mascotas> mascotas = queryMascota.list();

	        if (!mascotas.isEmpty()) {
	           
	            for (Mascotas mascota : mascotas) {
	              
	                String hqlConsultas = "FROM Consultas c WHERE c.mascotas.nchip = :nchip";
	                Query<Consultas> queryConsultas = session.createQuery(hqlConsultas, Consultas.class);
	                queryConsultas.setParameter("nchip", mascota.getNchip());
	                List<Consultas> consultas = queryConsultas.list();

	                for (Consultas consulta : consultas) {
	                   
	                    String hqlConsultaServicios = "FROM Consultaservicios cs WHERE cs.consultas.idconsulta = :idConsulta AND cs.facturado=false";
	                    Query<Consultaservicios> queryConsultaServicios = session.createQuery(hqlConsultaServicios, Consultaservicios.class);
	                    queryConsultaServicios.setParameter("idConsulta", consulta.getIdconsulta());
	                    List<Consultaservicios> consultaServicios = queryConsultaServicios.list();

	                    
	                    if (consultaServicios.isEmpty()) {
	                        continue; 
	                    }

	                    
	                    List<String> serviciosList = new ArrayList<>();
	                    for (Consultaservicios cs : consultaServicios) {
	                        String servicioNombre = cs.getServicios().getNombre();
	                        Integer cantidad = cs.getCantidad();
	                        BigDecimal precio = cs.getServicios().getPreciopvp();
	                        serviciosList.add(servicioNombre + " (x" + cantidad + ")" + " (x" + precio + " €)");
	                    }
	                    String serviciosConcat = String.join(", ", serviciosList);

	                 
	                    Object[] row = new Object[7];
	                    row[0] = consulta.getIdconsulta();
	                    row[1] = consulta.getFechaconsulta();
	                    row[2] = mascota.getPersonas().getNombre() + " " + mascota.getPersonas().getApellido();
	                    row[3] = mascota.getNombremascota(); 
	                    row[4] = mascota.getNchip();
	                    row[5] = serviciosConcat;
	                    row[6] = "Imprimir";

	                    resultados.add(row);
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	       
	    }

	    return resultados;
	}

	
	public static void marcarServiciosComoFacturados(int idConsulta) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();

	        
	        CriteriaBuilder cb = session.getCriteriaBuilder();
	        CriteriaQuery<Consultaservicios> cq = cb.createQuery(Consultaservicios.class);
	        Root<Consultaservicios> root = cq.from(Consultaservicios.class);

	        
	        Join<Consultaservicios, Consultas> consultasJoin = root.join("consultas");
	        cq.select(root).where(cb.equal(consultasJoin.get("idconsulta"), idConsulta));

	        
	        List<Consultaservicios> servicios = session.createQuery(cq).getResultList();

	        
	        for (Consultaservicios servicio : servicios) {
	            servicio.setFacturado(true);
	            session.update(servicio); 
	        }

	        
	        transaction.commit();

	        System.out.println("Servicios facturados para la consulta ID: " + idConsulta);
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    }
	}

	public boolean isChipRegistrado(int numChip) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	       
	        CriteriaBuilder cb = session.getCriteriaBuilder();
	        CriteriaQuery<Mascotas> cq = cb.createQuery(Mascotas.class);
	        Root<Mascotas> root = cq.from(Mascotas.class);

	       
	        cq.select(root).where(cb.equal(root.get("nchip"), numChip));

	       
	        Mascotas mascota = session.createQuery(cq).uniqueResult();
	        
	       
	        return mascota != null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        session.close();
	    }
	}



	public static void cargarTiposMascota(JComboBox<String> comboBoxTipoMascota) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	       
	        CriteriaBuilder cb = session.getCriteriaBuilder();
	        CriteriaQuery<Tipomascota> cq = cb.createQuery(Tipomascota.class);
	        Root<Tipomascota> root = cq.from(Tipomascota.class);

	      
	        List<Tipomascota> tipos = session.createQuery(cq).getResultList();

	      
	        comboBoxTipoMascota.removeAllItems();

	      
	        for (Tipomascota tipo : tipos) {
	            comboBoxTipoMascota.addItem(tipo.getNombretipo());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	}
	public static void cargarRazasPorTipo(JComboBox<String> comboBoxRazaMascota, int idTipo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
           
            Criteria criteria = session.createCriteria(Razamascota.class);
            criteria.createAlias("tipomascota", "tm");  
            criteria.add(Restrictions.eq("tm.idtipo", idTipo));  
            List<Razamascota> razas = criteria.list();  
            comboBoxRazaMascota.removeAllItems();
            for (Razamascota raza : razas) {
                comboBoxRazaMascota.addItem(raza.getNombreraza());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


	public void closeSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
}
