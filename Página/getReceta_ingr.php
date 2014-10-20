<?php
$conn = mysql_connect("mysql.hostinger.es","u147283082_admin" ,"seminario2");
mysql_selectdb("u147283082_hlife", $conn);
if($_POST){
	$receta=$_POST['receta'];
	$query = "SELECT i.Nombre as ingrediente, ri.unidades as unidades, ri.gramos as gramos, ri.litros as litros, (ri.unidades*i.cantidad) as calu, (ri.gramos*i.cantidad) as calg, (ri.litros*i.cantidad) as call FROM Receta r, Receta_Ingrediente ri, Ingrediente i, WHERE r.Nombre like '".$receta."' AND r.idReceta=ri.idReceta AND i.id=ri.idIngrediente;";
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