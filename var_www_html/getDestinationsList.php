<?php
mysql_connect("localhost","mayank","xtralyf321!@#");
mysql_select_db("test");
#phpinfo();
$output = array();
$q=mysql_query("SELECT * FROM destinations order by id");
while($e=mysql_fetch_assoc($q))
      $output[]=$e;

print($testing= json_encode($output));

mysql_close();
?>
