package jaspersoftMetodos;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperExportManager;





public class Principal {

	    public static void generarFactura(int idFactura, int idConsulta) {
	        System.out.println("Generando factura con idFactura: " + idFactura + " y idConsulta: " + idConsulta);

	        String reportSource = "./Plantillas/Facturitas.jrxml"; // Ruta de tu plantilla JRXML
	        String pdfFilePath = "./Informes/InformeFactura.pdf";  // Ruta del archivo PDF generado

	        Connection conn = null;

	        try {
	            // Conexión a la base de datos
	            Class.forName("com.mysql.cj.jdbc.Driver"); 
	            String url = "jdbc:mysql://167.235.9.66/comsiste_petnovabd";
	            String usuario = "comsiste_Dam";
	            String contrasenia = "cesfuencarral";
	            conn = DriverManager.getConnection(url, usuario, contrasenia);

	            // Compilar y llenar el reporte
	            JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
	            Map<String, Object> parametros = new HashMap<>();
	            parametros.put("idFactura", idFactura);
	            parametros.put("idConsulta", idConsulta);
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn);

	            // Exportar a PDF
	            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFilePath);
	            System.out.println("Factura generada: " + pdfFilePath);

	            // Abrir automáticamente el PDF
	            File pdfFile = new File(pdfFilePath);
	            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
	                Desktop.getDesktop().open(pdfFile);
	            } else {
	                System.out.println("No se pudo abrir el archivo PDF automáticamente.");
	            }

	        } catch (JRException e) {
	            System.err.println("Error al generar el reporte JasperReports.");
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            System.err.println("Driver de base de datos no encontrado.");
	            e.printStackTrace();
	        } catch (SQLException e) {
	            System.err.println("Error en la conexión con la base de datos.");
	            e.printStackTrace();
	        } catch (IOException e) {
	            System.err.println("Error al intentar abrir el PDF.");
	            e.printStackTrace();
	        } finally {
	            // Cerrar conexión en el bloque finally
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    System.err.println("Error al cerrar la conexión con la base de datos.");
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	}
