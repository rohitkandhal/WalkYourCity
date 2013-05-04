<?php
mysql_connect("localhost","mayank","xtralyf321!@#");//database connection
mysql_select_db("test");

$id = $_POST['group1'];
#Print "Selected id is $id";
$order = "DELETE FROM destinations where id = $id";
    

$result = mysql_query($order); 
if($result){
        echo("<br>Destination removed successfully");
} else{
        echo("<br>Operation failed");
}
Print "<br><br> <a href='edit.php'>Back</a>";
?>
