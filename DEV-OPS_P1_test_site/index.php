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
        $db = 'CData MariaDB Sys';
        $user = 'root';
        $pass = '';

        // URL-encode the database name
        $db_encoded = urlencode($db);

        try {
            // Include charset in DSN
            $pdo = new PDO("mysql:host=$host;dbname=$db_encoded;charset=utf8mb4", $user, $pass);

            // Set error mode to exceptions
            $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            // SQL query with backticks
            $sql = "SELECT * FROM `CData MariaDB Sys`.`innodb_table_stats`";

            $stmt = $pdo->prepare($sql);
            $stmt->execute();

            echo "<h2>innodb_table_stats</h2>";
            echo "<table border='1'>";
            echo "<tr>
                    <th>database_name</th>
                    <th>table_name</th>
                    <th>last_update</th>
                    <th>n_rows</th>
                    <th>clustered_index_size</th>
                    <th>sum_of_other_index_sizes</th>
                </tr>";

            // Fetch data as an associative array
            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                echo "<tr>
                        <td>{$row['database_name']}</td>
                        <td>{$row['table_name']}</td>
                        <td>{$row['last_update']}</td>
                        <td>{$row['n_rows']}</td>
                        <td>{$row['clustered_index_size']}</td>
                        <td>{$row['sum_of_other_index_sizes']}</td>
                    </tr>";
            }
            echo "</table>";

        } catch (PDOException $e) {
            echo "Error en la conexión: " . $e->getMessage();
        }
    
    ?>

</body>
</html>
