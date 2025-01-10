import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {
    private JTextField userName;
    private JPasswordField userPass;
    private JButton ingresarButton;
    public JPanel loginPanel;


    public Login() {
        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:mysql://localhost:3306/calificaciones";
                String username = "root";
                String password = "1234";

                Connection conn = null;

                try {
                    conn = DriverManager.getConnection(url, username, password);
                    System.out.println(">> Conexión con la DB exitosa!");

                    String parametroName = userName.getText();
                    String parametroPass = new String(userPass.getPassword());

                    String query = "SELECT password FROM usuarios WHERE username = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, parametroName);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                String passwordDB = rs.getString("password");

                                if (passwordDB.equals(parametroPass)) {
                                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");

                                    // Abrir ventana para el ingreso de calificaciones:
                                    JFrame frame = new JFrame("Registro Calificaiones");
                                    frame.setContentPane(new Grades().gradesPanel);
                                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    frame.setSize(600,600);
                                    frame.setPreferredSize(new Dimension(600,600));
                                    frame.pack();
                                    frame.setLocationRelativeTo(null);
                                    frame.setVisible(true);

                                } else {
                                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Lo sentimos. Parece que tu usuario no está registrado.");
                            }
                        }
                    }
                } catch (SQLException error) {
                    System.out.println(error.getMessage());
                    JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos");
                } finally {
                    try {
                        if (conn != null) {
                           conn.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
