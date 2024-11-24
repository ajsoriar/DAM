import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorBiblioteca {
    
    private List<Libro> libros;

    public static void main(String[] args) {
        ServidorBiblioteca servidor = new ServidorBiblioteca();
        servidor.startServer();
    }

    public ServidorBiblioteca() {
        libros = new ArrayList<>();
        libros.add(new Libro("000", "1984", "George Orwell", 35));
        libros.add(new Libro("123", "Libro A", "Autor A", 101.50));
        libros.add(new Libro("456", "Libro B", "Autor B", 15));
        libros.add(new Libro("789", "Libro C", "Autor C", 20.0));
        libros.add(new Libro("321", "Brave New World", "Aldous Huxley", 10));
        libros.add(new Libro("654", "Fahrenheit 451", "Ray Bradbury", 15));
        libros.add(new Libro("987", "The Handmaid's Tale", "Margaret Atwood", 20));
        libros.add(new Libro("741", "The Road", "Cormac McCarthy", 5));
        libros.add(new Libro("852", "The Hunger Games", "Suzanne Collins", 12.50));
        libros.add(new Libro("963", "Divergent", "Veronica Roth", 8));
        libros.add(new Libro("147", "The Giver", "Lois Lowry", 7.50));
        libros.add(new Libro("258", "Never Let Me Go", "Kazuo Ishiguro", 18));
        libros.add(new Libro("369", "The Maze Runner", "James Dashner", 9));
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(Config.getPort())) {
            System.out.println("Servidor iniciado en el puerto 8080. Esperando conexión...");
    
            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
    
                    System.out.println("Cliente conectado.");
    
                    boolean continuar = true; // Bandera para mantener la conexión abierta.
                    while (continuar) {
                        try {
                            String opcion = (String) entrada.readObject();
                            String parametro = (String) entrada.readObject();
    
                            if ("SALIR".equals(opcion)) {
                                System.out.println("Cliente ha finalizado la conexión.");
                                continuar = false;
                                break;
                            }

                            // Listar libros
                            if ("LISTAR".equals(opcion)) {
                                String resultado = listBooks();
                                salida.writeObject(resultado);
                                System.out.println("Resultado enviado al cliente.");
                                continue;
                            }
    
                            String resultado;
                            if ("ISBN".equals(opcion)) {
                                resultado = buscarPorISBN(parametro);
                            } else if ("TITULO".equals(opcion)) {
                                resultado = searchByTitle(parametro);
                            } else if ("AUTOR".equals(opcion)) {
                                resultado = searchByAuthor(parametro);
                            } else  if ("ANYADIR".equals(opcion)) {
                                // Añadir libro
                                String[] datos = parametro.split(",");
                                if (datos.length == 4) {
                                    libros.add(new Libro(datos[0], datos[1], datos[2], Double.parseDouble(datos[3])));
                                    resultado = "Libro añadido correctamente.";
                                } else {
                                    resultado = "Datos incorrectos.";
                                }
                            } else {
                                resultado = "Opción no válida.";
                            }
    
                            salida.writeObject(resultado); // writeObject se usa para enviar cualquier objeto serializable.

                            System.out.println("Resultado enviado al cliente.");
                        } catch (IOException | ClassNotFoundException e) {
                            System.err.println("Error procesando solicitud: " + e.getMessage());
                            continuar = false;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String buscarPorISBN(String isbn) {
        for (Libro libro : libros) {
            if (libro.getISBN().equalsIgnoreCase(isbn)) {
                return libro.toString();
            }
        }
        return "No se encontró ningún libro con el ISBN: " + isbn;
    }

    // private String searchByTitle(String titulo) { // Buscar por título exacto
    //     // log the title of the book
    //     System.out.println("Buscando libro con título: '" + titulo + "'");

    //     for (Libro libro : libros) {
    //         if (libro.getTitulo().equalsIgnoreCase(titulo)) {
    //             return libro.toString();
    //         }
    //     }
    //     return "No se encontró ningún libro con el título: " + titulo;
    // }

    private String searchByTitle(String titulo) { // Admitir búsquedas parciales sin distinción entre mayúsculas y minúsculas
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Libros encontrados ---\n");
        int count = 0;
        for (Libro libro : libros) {
            if (libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                sb.append(libro.toString()).append("\n");
                count++;
            }
        }
        if (count == 0) {
            return "No se encontró ningún libro con el título: " + titulo;
        }
        return sb.toString();
    }

    // private String searchByAuthor(String autor) { // Buscar por autor exacto
    //     for (Libro libro : libros) {
    //         if (libro.getAutor().equalsIgnoreCase(autor)) {
    //             return libro.toString();
    //         }
    //     }
    //     return "No se encontró ningún libro con el autor: " + autor;
    // }

    private String searchByAuthor(String autor) { // Admitir búsquedas parciales sin distinción entre mayúsculas y minúsculas
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Libros encontrados ---\n");
        int count = 0;
        for (Libro libro : libros) {
            if (libro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                sb.append(libro.toString()).append("\n");
                count++;
            }
        }
        if (count == 0) {
            return "No se encontró ningún libro con el autor: " + autor;
        }
        return sb.toString();
    }

    private String listBooks() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Libros en la biblioteca ---\n");
        for (Libro libro : libros) {
            sb.append(libro.toString()).append("\n");
        }
        return sb.toString();
    }
}
