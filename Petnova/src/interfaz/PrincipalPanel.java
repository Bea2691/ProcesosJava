package interfaz;

import javax.swing.*;
import java.awt.*;

class PrincipalPanel extends JPanel {
    private JLabel lblFondo;
    private ImageIcon originalIcon;

    public PrincipalPanel() {
    	setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    	
    	JLabel lblNewLabel = new JLabel("");
    	lblNewLabel.setIcon(new ImageIcon(PrincipalPanel.class.getResource("/imagenes/Fondo_Petnova.png")));
    	add(lblNewLabel);
        

    }	
}
