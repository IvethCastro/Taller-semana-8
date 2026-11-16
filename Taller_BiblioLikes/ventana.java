import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;
import java.util.ArrayList;

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

    private final CatalogoLibros catalogo = new CatalogoLibros();
    private final DefaultListModel<Libro> modeloResultados = new DefaultListModel<>();
    private final DefaultListModel<Libro> modeloRanking = new DefaultListModel<>();
    private int codigoSeleccionado = -1;
    private String busquedaTextoActiva = null;

    public ventana() {
        catalogo.cargarDatosIniciales();
        String[] generos = {"Programación", "Bases de datos", "Redes", "Seguridad",
                "Sistemas", "Software", "IA", "Gestión"};
        cbGenero.setModel(new DefaultComboBoxModel<>(generos));
        cbGeneroEditar.setModel(new DefaultComboBoxModel<>(generos));
        int anioActual = Year.now().getValue();
        spAnio.setModel(new SpinnerNumberModel(anioActual, 1, anioActual, 1));
        spAnioEditar.setModel(new SpinnerNumberModel(anioActual, 1, anioActual, 1));
        listaResultados.setModel(modeloResultados);
        listaRanking.setModel(modeloRanking);
        txtCodEditar.setEditable(false);
        actualizarTotal();
        limpiarSeleccion();
        actualizarRanking();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarLibro();
            }
        });
        btnBuscarCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCodigo();
            }
        });
        btnBuscarTexto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarTexto();
            }
        });
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });
        btnMeGusta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                darMeGusta();
            }
        });
        btnActualizarRanking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarRanking();
            }
        });
        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Libro libro = (Libro) listaResultados.getSelectedValue();
                if (libro == null) limpiarSeleccion();
                else mostrarDetalle(libro);
            }
        });
        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedIndex() == 2) actualizarRanking();
        });
    }

    private void registrarLibro() {
        try {
            int codigo = leerCodigo(txtCodigo, "registro");
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String genero = cbGenero.getSelectedItem().toString();
            int anio = (Integer) spAnio.getValue();
            catalogo.registrar(new Libro(codigo, titulo, autor, genero, anio, 0));
            actualizarTotal(); actualizarRanking();
            txtCodigo.setText(""); txtTitulo.setText(""); txtAutor.setText("");
            cbGenero.setSelectedIndex(0);
            spAnio.setValue(Year.now().getValue());
            JOptionPane.showMessageDialog(panel1, "Libro registrado correctamente.");
        } catch (IllegalArgumentException ex) { avisar(ex.getMessage()); }
    }

    private int leerCodigo(JTextField campo, String contexto) {
        try {
            int codigo = Integer.parseInt(campo.getText().trim());
            if (codigo <= 0) throw new NumberFormatException();
            return codigo;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ingresa un código positivo válido para " + contexto + ".");
        }
    }

    private void buscarCodigo() {
        limpiarResultados();
        busquedaTextoActiva = null;
        try {
            int codigo = leerCodigo(txtBuscarCodigo, "la búsqueda");
            int indice = catalogo.buscarIndicePorCodigo(codigo);
            if (indice == -1) { avisar("No existe un libro con el código " + codigo + "."); return; }
            Libro libro = catalogo.buscarPorCodigo(codigo);
            modeloResultados.addElement(libro);

            listaResultados.setSelectedIndex(0);
        } catch (IllegalArgumentException ex) { avisar(ex.getMessage()); }
    }

    private void buscarTexto() {
        limpiarResultados();
        String texto = txtBuscarTexto.getText().trim();
        if (texto.isEmpty()) { busquedaTextoActiva = null; avisar("Escribe un título o autor para buscar."); return; }
        busquedaTextoActiva = texto;
        ArrayList<Libro> encontrados = catalogo.buscarCoincidencias(texto);
        for (Libro libro : encontrados) modeloResultados.addElement(libro);
        JOptionPane.showMessageDialog(panel1, "Coincidencias: " + encontrados.size());
        if (encontrados.isEmpty()) avisar("No se encontraron coincidencias.");
    }

    private void mostrarDetalle(Libro libro) {
        codigoSeleccionado = libro.getCodigo();
        txtCodEditar.setText(String.valueOf(libro.getCodigo()));
        txtTituloEditar.setText(libro.getTitulo());
        txtAutorEditar.setText(libro.getAutor());
        cbGeneroEditar.setSelectedItem(libro.getGenero());
        spAnioEditar.setValue(libro.getAnio());
        lblLikes.setText("Me gusta: " + libro.getMeGusta());
        btnGuardar.setEnabled(true); btnMeGusta.setEnabled(true);
    }

    private void guardarCambios() {
        if (codigoSeleccionado == -1) { avisar("Selecciona primero un libro."); return; }
        String titulo = txtTituloEditar.getText().trim();
        String autor = txtAutorEditar.getText().trim();
        String genero = cbGeneroEditar.getSelectedItem().toString();
        int anio = (Integer) spAnioEditar.getValue();
        if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty()
                || anio < 1 || anio > Year.now().getValue()) {
            avisar("Completa título, autor, género y un año válido."); return;
        }
        int codigo = codigoSeleccionado;
        if (!catalogo.editarLibro(codigo, titulo, autor, genero, anio)) {
            avisar("No se pudieron guardar los cambios."); return;
        }
        actualizarRanking();
        refrescarResultados(codigo);
        JOptionPane.showMessageDialog(panel1, "Cambios guardados correctamente.");
    }

    private void darMeGusta() {
        if (codigoSeleccionado == -1) { avisar("Selecciona primero un libro."); return; }
        int codigo = codigoSeleccionado;
        try {
            catalogo.darMeGusta(codigo);
            refrescarResultados(codigo);
            actualizarRanking();
        } catch (IllegalArgumentException ex) { limpiarSeleccion(); avisar(ex.getMessage()); }
    }

    private void refrescarResultados(int codigo) {
        ArrayList<Libro> visibles;
        if (busquedaTextoActiva != null) {
            visibles = catalogo.buscarCoincidencias(busquedaTextoActiva);
        } else {
            visibles = new ArrayList<>();
            Libro libro = catalogo.buscarPorCodigo(codigo);
            if (libro != null) visibles.add(libro);
        }
        listaResultados.clearSelection();
        modeloResultados.clear();
        int indiceSeleccion = -1;
        for (Libro libro : visibles) {
            modeloResultados.addElement(libro);
            if (libro.getCodigo() == codigo) indiceSeleccion = modeloResultados.size() - 1;
        }

        if (indiceSeleccion >= 0) listaResultados.setSelectedIndex(indiceSeleccion);
        else limpiarSeleccion();
    }

    private void limpiarResultados() {
        listaResultados.clearSelection();
        modeloResultados.clear();

        limpiarSeleccion();
    }

    private void limpiarSeleccion() {
        codigoSeleccionado = -1;
        txtCodEditar.setText(""); txtTituloEditar.setText(""); txtAutorEditar.setText("");
        cbGeneroEditar.setSelectedIndex(0);
        spAnioEditar.setValue(Year.now().getValue());
        lblLikes.setText("Me gusta: -");
        btnGuardar.setEnabled(false); btnMeGusta.setEnabled(false);
    }

    private void actualizarTotal() {
        lblTotal.setText("Total de libros: " + catalogo.obtenerCantidadLibros());
    }

    private void actualizarRanking() {
        modeloRanking.clear();
        for (Libro libro : catalogo.obtenerRanking()) modeloRanking.addElement(libro);
    }

    private void avisar(String mensaje) {
        JOptionPane.showMessageDialog(panel1, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ventana");
        frame.setContentPane(new ventana().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


