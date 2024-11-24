import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteBiblioteca {

    public void iniciar() {

        cls(); // LOL: Clear the screen like in old DOS programs.

        try (Socket socket = new Socket("localhost", Config.getPort());
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Conectado al servidor.");
            boolean continuar = true;

            while (continuar) {
                showAppMenu();
                int opcion = readMenuOption();

                String opcionEnviar = null;
                String parametro = null;

                switch (opcion) {
                    case 0:
                        opcionEnviar = "LISTAR";
                        parametro = "LISTAR";
                        break;
                    case 1:
                        opcionEnviar = "ISBN";
                        System.out.print("Introduce el ISBN del libro: ");
                        parametro = readKeyboardImput();
                        break;
                    case 2:
                        opcionEnviar = "TITULO";
                        System.out.print("Introduce el título del libro: ");
                        parametro = readKeyboardImput();
                        break;
                    case 3:
                        opcionEnviar = "AUTOR";
                        System.out.print("Introduce el autor del libro: ");
                        parametro = readKeyboardImput();
                        break;
                    case 4:
                        opcionEnviar = "ANYADIR";
                        parametro = addABook();
                        break;
                    case 5:
                        opcionEnviar = "SALIR";
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                        continue;
                }

                try {
                    salida.writeObject(opcionEnviar);
                    salida.writeObject(parametro);

                    if (!"SALIR".equals(opcionEnviar)) {
                        String respuesta = (String) entrada.readObject();
                        System.out.println("\nRespuesta del servidor: " + respuesta);

                        // Pause the program to allow the user to read the response and press whatever key to continue
                        System.out.println("\nPresiona Enter para continuar...");
                        Scanner scanner = new Scanner(System.in);
                        scanner.nextLine();
                        
                    } else {
                        System.out.println("Conexión cerrada.");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error al comunicarse con el servidor: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
        }
    }

    private void showAppMenu() {
        cls();
        //System.out.println("\n--- Menú Biblioteca ---");
        System.out.println(getTitleString("Menú Biblioteca"));
        System.out.println("0. Listar todos los libros");
        System.out.println("1. Consultar libro por ISBN");
        System.out.println("2. Consultar libro por título");
        System.out.println("3. Consultar libro por autor");
        System.out.println("4. Anyadir un libro");
        System.out.println("5. Salir de la aplicación");
        System.out.print("Selecciona una opción: ");
    }

    private int readMenuOption() {
        Scanner scanner = new Scanner(System.in);
        try {
            return Integer.parseInt(scanner.nextLine()); // Leer un número entero
        } catch (NumberFormatException e) {
            System.out.println("Por favor, introduce una opción de menú válida.");
            return -1;
        }
    }

    private String readKeyboardImput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine(); // Leer una cadena de texto
    }

    private String addABook() {
        
        String[] prompts = {
            "Introduce el ISBN del libro: ",
            "Introduce el título del libro: ",
            "Introduce el autor del libro: ",
            "Introduce el precio del libro: "
        };

        String[] errorMessages = {
            "El ISBN no puede contener caracteres especiales.",
            "El título no puede contener caracteres especiales.",
            "El autor no puede contener caracteres especiales.",
            "El precio debe ser un número entero o decimal."
        };
        
        Scanner scanner = new Scanner(System.in);
        int[] validationTypes = {1, 1, 1, 2}; // Here we define the validation type for each field. 1 = String, 2 = Numeric
        String[] fieldValues = new String[prompts.length];

        for (int i = 0; i < prompts.length; i++) { // Loop of questions to add a book
            while (true) {
                System.out.print(prompts[i]);
                String input = scanner.nextLine();
                boolean valid;

                // Validate the input
                if (validationTypes[i] == 1) {
                    valid = !checkProblematicCharacters(input);
                } else if (validationTypes[i] == 2) {
                    valid = !checkNumeric(input);
                } else {
                    valid = true;
                }

                if (!valid) {
                    System.out.println(errorMessages[i]);
                } else {
                    // If validationType is 2, standardize the numeric input. Do nothing if in other case.
                    fieldValues[i] = validationTypes[i] == 2 ? standarizeNumeric(input) : input;
                    break;
                }
            }
        }

        // Collect the inputs
        String isbn = fieldValues[0];
        String titulo = fieldValues[1];
        String autor = fieldValues[2];
        String precio = fieldValues[3];

        return isbn + "," + titulo + "," + autor + "," + precio;
    }

    // Method to check for special or strange characters.
    private boolean checkProblematicCharacters(String texto) {
        return !texto.matches("[a-zA-Z0-9 ]*");
    }

    // Metod to standardize the decimal separator and check if the input is a valid number
    private String standarizeNumeric(String texto) {
        texto = texto.trim();
        String standardizedText = texto.replace(",", "."); // Replace comma with dot to standardize the decimal separator
        return standardizedText;
    }

    private boolean checkNumeric(String texto) {
        String standardizedText = standarizeNumeric(texto);
        return !standardizedText.matches("[0-9]+(\\.[0-9]+)?");
    }

    private String getTitleString(String title) {
        int length = title.length();
        String underline = "-".repeat(length);
        return "\n" + title + "\n" + underline + "\n";
    }

    private void cls() {
        System.out.print("\033[H\033[2J");
    }

    public static void main(String[] args) {
        ClienteBiblioteca cliente = new ClienteBiblioteca();
        cliente.iniciar();
    }
}