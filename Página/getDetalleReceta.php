<?php
$conn = mysql_connect("mysql.hostinger.es","u147283082_admin" ,"seminario2");
mysql_selectdb("u147283082_hlife", $conn);
if($_POST){
	$receta=$_POST['receta'];
	$query = "SELECT r.Nombre as nombre, r.Instrucciones as instrucciones, r.categoria as categoria, u.nick as usuario FROM Receta r, Usuario u, WHERE r.Nombre like '".$receta."' AND r.Usuario=Usuario.id;";
	//mysql_query("BEGIN");
	$result1 = mysql_query($query);
	//mysql_query("COMMIT");
	if($result1){
        $bdds = array();
	    $campo=array_keys($bdds);
	    while($r = mysql_fetch_assoc($result1)){
		    $bdds[] = $r;
	    }
	    mysql_close($conn);
	    echo json_encode($bdds);
    }
}
?>