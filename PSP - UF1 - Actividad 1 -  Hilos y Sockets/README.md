# Instructions to run the Client-Server Library system

![Library exercise](./circe-library.png?raw=true 'Library exercise')

This system is composed of four classes:
- **Book**: Represents the data of a book.
- **LibraryServer**: Acts as a server to handle book queries.
- **LibraryClient**: A client that interacts with the server to perform queries.
- **Config**: Stores server configurations.

---

## Steps to run the system

### 1. Compile the classes
Make sure you have **Java JDK** installed. Compile the three classes using the following command from the terminal or command line:

```bash
javac Libro.java ServidorBiblioteca.java ClienteBiblioteca.java Config.java
```

### 2. Run the server
Run the server with the following command:

```bash
java ServidorBiblioteca
```

### 3. Run the client
Run the client with the following command:

```bash
java ClienteBiblioteca
```

---

## Instructions to interact with the client

The client will display a menu with the options:

0. Listar todos los libros
1. Consultar libro por ISBN
2. Consultar libro por título
3. Consultar libro por autor
4. Anyadir un libro
5. Salir de la aplicación

Select an option by entering the corresponding number and follow the instructions to enter the ISBN or title of the book you want to query.

---

## Additional notes

- The server will start on port 8080.
- Make sure the server is running before starting the client.
- You can stop the server at any time by pressing `Ctrl + C` in the terminal where it is running.

---