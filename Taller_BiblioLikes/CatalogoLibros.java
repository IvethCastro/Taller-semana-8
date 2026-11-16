import java.util.ArrayList;
public class CatalogoLibros {

    private ArrayList<Libro> libros;
    private boolean datosCargados = false;
    public CatalogoLibros() {
        libros = new ArrayList<>();
    }

    public CatalogoLibros(ArrayList<Libro> libros) {
        this.libros = new ArrayList<>(libros);
        ordenarPorCodigo();
    }

    public void cargarDatosIniciales() {
        if (!datosCargados) {
            libros.addAll(DatosIniciales.cargar());
            ordenarPorCodigo();
            datosCargados = true;
        }
    }

    public int obtenerCantidadLibros() {
        return libros.size();
    }

    public void agregarLibro(Libro libro) {
        if (libro == null) {
            return;
        }
        if (buscarPorCodigo(libro.getCodigo()) != null) {
            return;
        }
        libros.add(libro);
        ordenarPorCodigo();
    }

    public ArrayList<Libro> getLibros() {
        return new ArrayList<>(libros);
    }

    public Libro buscarPorCodigo(int codigo) {
        for (Libro libro : libros) {
            if (libro.getCodigo() == codigo) {
                return libro;
            }
        }
        return null;
    }
    public boolean eliminarLibro(int codigo) {
        int indice = buscarIndicePorCodigo(codigo);

        if (indice != -1) {
            libros.remove(indice);
            return true;
        }

        return false;
    }

    private void ordenarPorCodigo() {
        for (int i = 0; i < libros.size() - 1; i++) {
            for (int j = 0; j < libros.size() - 1 - i; j++) {

                if (libros.get(j).getCodigo() > libros.get(j + 1).getCodigo()) {
                    Libro auxiliar = libros.get(j);
                    libros.set(j, libros.get(j + 1));
                    libros.set(j + 1, auxiliar);
                }
            }
        }
    }

    public ArrayList<Libro> buscarCoincidencias(String texto) {
        ArrayList<Libro> resultados = new ArrayList<>();

        if (texto == null || texto.trim().equals("")) {
            return resultados;
        }
        texto = texto.trim();

        for (Libro libro : libros) {
            String titulo = libro.getTitulo();
            String autor = libro.getAutor();

            boolean coincide = false;

            for (int i = 0; i <= titulo.length() - texto.length(); i++) {
                String parte = titulo.substring(i, i + texto.length());

                if (parte.equalsIgnoreCase(texto)) {
                    coincide = true;
                    break;
                }
            }

            if (!coincide) {
                for (int i = 0; i <= autor.length() - texto.length(); i++) {
                    String parte = autor.substring(i, i + texto.length());
                    if (parte.equalsIgnoreCase(texto)) {
                        coincide = true;
                        break;
                    }
                }
            }
            if (coincide) {
                resultados.add(libro);
            }
        }
        return resultados;
    }

    public int buscarIndicePorCodigo(int codigo) {
        if (codigo <= 0) {
            return -1;
        }

        int inicio = 0;
        int fin = libros.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int codigoMedio = libros.get(medio).getCodigo();
            if (codigoMedio == codigo) {
                return medio;
            }
            if (codigo < codigoMedio) {
                fin = medio - 1;
            } else {
                inicio = medio + 1;
            }
        }
        return -1;
    }

    public boolean editarLibro(int codigo, String titulo, String autor, String genero, int anio) {
        if (codigo <= 0) {
            return false;
        }

        if (titulo == null || titulo.trim().equals("")
                || autor == null || autor.trim().equals("")
                || genero == null || genero.trim().equals("")) {
            return false;
        }

        if (anio <= 0) {
            return false;
        }

        int indice = buscarIndicePorCodigo(codigo);
        if (indice == -1) {
            return false;
        }
        Libro libro = libros.get(indice);
        libro.setTitulo(titulo.trim());
        libro.setAutor(autor.trim());
        libro.setGenero(genero.trim());
        libro.setAnio(anio);
        return true;
    }

    public ArrayList<Libro> obtenerRanking() {
        ArrayList<Libro> ranking = new ArrayList<>(libros);

        for (int i = 0; i < ranking.size() - 1; i++) {

            for (int j = 0; j < ranking.size() - 1 - i; j++) {
                if (ranking.get(j).getMeGusta()
                        < ranking.get(j + 1).getMeGusta()) {
                    Libro auxiliar = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, auxiliar);
                }
            }
        }
        return ranking;
    }
}
