<!-- Simplest PHP template -->

<!DOCTYPE html>
<html>
<head>
    <title>Simplest PHP template</title>
</head>
<body>

    <h1>Simplest PHP template</h1>
    <p><?php echo "Hello, World!"; ?></p>

    <p>Today is <?php echo date('l, F jS, Y'); ?>.</p>

    <p>PHP version: <?php echo phpversion(); ?></p>

    <?php

        $host = 'localhost';
        $db = 'mysql';
        $user = 'newuser';
        $pass = 'password';

        // URL-encode the database name
        $db_encoded = urlencode($db);

        try {
            // Include charset in DSN
            $pdo = new PDO("mysql:host=$host;dbname=$db_encoded;charset=utf8mb4", $user, $pass);

            // Set error mode to exceptions
            $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            // SQL query with backticks
            $sql = "SELECT User, Host FROM user";

            $stmt = $pdo->prepare($sql);
            $stmt->execute();

            echo "<table border='1'>";
            echo "<tr>
                    <th>User</th>
                    <th>Host</th>
                </tr>";

            // Fetch data as an associative array
            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                echo "<tr>
                        <td>{$row['User']}</td>
                        <td>{$row['Host']}</td>
                    </tr>";
            }
            echo "</table>";

        } catch (PDOException $e) {
            echo "Error en la conexión: " . $e->getMessage();
        }
    
    ?>

</body>
</html>
