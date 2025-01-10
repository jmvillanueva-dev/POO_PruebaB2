import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Grades {
    private JTextField cedulaId;
    private JButton calcularPromedioButton;
    private JButton registrarCalificacionesButton;
    private JLabel validacionCalf;
    private JTextField calf1;
    private JTextField calf2;
    private JTextField calf3;
    private JTextField calf4;
    private JTextField calf5;
    public JPanel gradesPanel;
    private JTextField studentName;


    public Grades() {
        calcularPromedioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Double grade1 = validarRango(calf1.getText());
                Double grade2 = validarRango(calf2.getText());
                Double grade3 = validarRango(calf3.getText());
                Double grade4 = validarRango(calf4.getText());
                Double grade5 = validarRango(calf5.getText());

                Double promedio = (grade1 + grade2 + grade3 + grade4 + grade5)/5;
                validacionCalf.setText("El Promedio del estudiante es: " + promedio);
            }
        });
        registrarCalificacionesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String url = "jdbc:mysql://localhost:3306/calificaciones";
                String user = "root";
                String password = "1234";

                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = DriverManager.getConnection(url, user, password);
                    System.out.println(">> [Registro Notas] Se ha conectado a la BDD");

                    String sql = "INSERT INTO estudiantes(cedula, nombre, nota1, nota2, nota3, nota4, nota5) \n" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, cedulaId.getText());
                    ps.setString(2, studentName.getText());
                    ps.setDouble(3, Double.parseDouble(calf1.getText()));
                    ps.setDouble(4, Double.parseDouble(calf2.getText()));
                    ps.setDouble(5, Double.parseDouble(calf3.getText()));
                    ps.setDouble(6, Double.parseDouble(calf4.getText()));
                    ps.setDouble(7, Double.parseDouble(calf5.getText()));

                    int datosInsertados = ps.executeUpdate();
                    System.out.println("Se han registrado los datos: " + datosInsertados);

                    JOptionPane.showMessageDialog(null, "Registro Exitoso");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    // Cerrar la conexión
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private double validarRango(String text) throws IllegalArgumentException {
        try {
            double grade = Double.parseDouble(text);
            if (grade < 0 || grade > 20) {
                throw new IllegalArgumentException("La calificación debe estar entre 0 y 20: " + text);
            }
            return grade;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Entrada no válida: " + text);
        }
    }
}
