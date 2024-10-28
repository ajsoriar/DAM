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

    <!-- Access default MySQL examle database and print a couple of tables --> 

    <?php
    $conn = new mysqli("localhost",
                          "root",
                          "password",
                          "example");   // Connect to the example database

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $result = $conn->query("SELECT * FROM users");  // Query the users table
    if ($result->num_rows > 0) {
        echo "<h2>Users</h2>";
        echo "<table border='1'><tr><th>ID</th><th>Name</th><th>Email</th></tr>";
        while($row = $result->fetch_assoc()) {
            echo "<tr><td>".$row["id"]."</td><td>".$row["name"]."</td><td>".$row["email"]."</td></tr>";
        }
        echo "</table>";
    } else {
        echo "0 results";
    }

    $result = $conn->query("SELECT * FROM posts");  // Query the posts table
    if ($result->num_rows > 0) {
        echo "<h2>Posts</h2>";
        echo "<table border='1'><tr><th>ID</th><th>Title</th><th>Content</th></tr>";
        while($row = $result->fetch_assoc()) {
            echo "<tr><td>".$row["id"]."</td><td>".$row["title"]."</td><td>".$row["content"]."</td></tr>";
        }
        echo "</table>";
    } else {
        echo "0 results";
    }

    $conn->close();

?>

</body>
</html>
