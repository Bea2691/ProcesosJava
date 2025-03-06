package interfaz;

import javax.swing.*;
import conexion.Cliente;
import java.awt.*;
import java.awt.event.*;

public class V_Chat extends JFrame {
    
	private static final long serialVersionUID = 1L;
	private JTextArea chatArea;
    private JTextField inputField;
    private Cliente cliente;
    private String contact;
    private JPopupMenu emojiMenu;

    @SuppressWarnings("unused")
	public V_Chat(Cliente cliente, String contact) {
        this.cliente = cliente;
        this.contact = contact;
        setTitle("Chat con " + contact);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Listener para eliminar ventana del mapa al cerrarse
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                cliente.eliminarVentanaChat(contact);
            }
        });

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField(30);
        JButton sendBtn = new JButton("Enviar");

        //Panel inferior
        JPanel bottomPanel = new JPanel();
        
        JButton btnEmojis = new JButton("😀"); // Botón con emoji
        bottomPanel.add(btnEmojis);
        bottomPanel.add(inputField);
        bottomPanel.add(sendBtn);

        //Menu con los emojis con un listener para insertarlos en el texfield
        emojiMenu = new JPopupMenu();
        String[] emojis = {"😊", "😄", "❤️", "😆", "😂", "😍", "💩", "😎", "😜", "👍", "👏", "😡"}; 

        for (String emoji : emojis) {
            JMenuItem item = new JMenuItem(emoji);
            item.addActionListener(e -> inputField.setText(inputField.getText() + " " + emoji));
            emojiMenu.add(item);
        }

        //Mostrar el menú emergente al hacer clic en el botón de emojis
        btnEmojis.addActionListener(e -> emojiMenu.show(btnEmojis, 0, btnEmojis.getHeight()));

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);

        sendBtn.addActionListener(e -> sendMessage());

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }
    //Metodo para pasarle lo que se escribe ne interfaz al cliente para que maneje el envio de mensajes
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            cliente.enviarMensaje(contact, message);
            chatArea.append("Tú: " + message + "\n");
            inputField.setText("");
        }
    }

    public void appendMessage(String sender, String message) {
        chatArea.append(sender + ": " + message + "\n");
    }

    public void appendHistory(String history) {
        chatArea.setText(history); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new V_Chat(new Cliente(), "Contacto"));
    }
}
