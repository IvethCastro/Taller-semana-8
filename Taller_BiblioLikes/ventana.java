import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ventana {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField txtCodigo;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JComboBox cbGenero;
    private JSpinner spAnio;
    private JButton btnRegistrar;
    private JLabel lblTotal;
    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JTextField txtBuscarTexto;
    private JButton btnBuscarTexto;
    private JList listaResultados;
    private JButton btnGuardar;
    private JButton btnMeGusta;
    private JTextField txtCodEditar;
    private JTextField txtTituloEditar;
    private JTextField txtAutorEditar;
    private JComboBox cbGeneroEditar;
    private JSpinner spAnioEditar;
    private JLabel lblLikes;
    private JList listaRanking;
    private JButton btnActualizarRanking;

    public ventana() {
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnBuscarCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnBuscarTexto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnMeGusta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnActualizarRanking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}



