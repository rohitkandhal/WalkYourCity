<?php
mysql_connect("localhost","mayank","xtralyf321!@#");//database connection
mysql_select_db("test");

//inserting data order

$data= mysql_query("SELECT * FROM destinations order by id") or die(mysql_error());
print "<h3>Choose a Destination to Remove</h3><br>";
print "<table border cellpadding=3>";

$desttype = array(0 => "Amusement",
		  1 => "Landmark",
		  2 => "Commerce",
		  3 => "Open Space");
Print "<html><head><script>";
Print "</script></head><form method='post' action='delete.php'>";
Print "<tr>";
Print "<th></th><th>Name</th><th>Type</th><th>Description</th>";
Print "</tr>";
#Print "<select>";
while($info = mysql_fetch_array($data))
{
Print "<tr>";
Print "<td> <input type=radio name=group1 value=".$info['id']."></td>";
Print "<td>".$info['name']."</td>";
Print "<td>".$desttype[$info['type']]."</td>";
Print "<td>".$info['description']."</td>";
Print "</tr>";
}

Print "</select></table>";
#Print "<br><input type=submit value='Edit'></input>";
Print "<br><input type=submit value='Delete'>";
Print "</form><br><br><a href='index.html'>Back</a></html>";

?>
