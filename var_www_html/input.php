<?php
mysql_connect("localhost","mayank","xtralyf321!@#");//database connection
mysql_select_db("test");

//inserting data order

$order = "INSERT INTO destinations
			(lats, longs, name, type, description, favourite)
			VALUES
			($_POST[lat],
			 $_POST[long],
			'$_POST[name]',
			 $_POST[type],
			'$_POST[desc]',0)";

//declare in the order variable
$result = mysql_query($order);	//order executes
if($result){
	echo("<br>Input data is succeed");
} else{
	echo("<br>Input data is fail");
}
Print "<br><br> <a href='addnew.html'>Back</a>";
?>
