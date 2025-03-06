package interfaz;

import javax.swing.*;

import conexion.Cliente;

import java.awt.*;
import java.awt.event.*;

public class V_Menu extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> userList;
    private DefaultListModel<String> listModel;
    private Cliente cliente;

    public V_Menu(Cliente cliente) {
        this.cliente = cliente;
        setTitle("Chat - " + cliente.getUsername());
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        setLocationRelativeTo(null);

        //Listener para el doble clic para abrir chat
        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedUser = userList.getSelectedValue();
                    cliente.abrirChat(selectedUser);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(userList);
        add(scrollPane, BorderLayout.CENTER);
    }
    //Método para actulizar visualmente la lista de usuarios conectados
    public void actualizarListaUsuarios(String[] users) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (String user : users) {
                if (!user.isEmpty() && !user.equals(cliente.getUsername())) {
                    listModel.addElement(user);
                }
            }
        });
    }
}