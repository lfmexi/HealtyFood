<?php
if($_FILES['file']['name']){
$file_path = "images/";

$file_path = $file_path . date('Ydmhisu').$_POST['user'].'.'.end(explode('.', $_FILES['file']['name']));

if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {
    echo "success";
} else{
    echo "fail";
}
}

$conn = mysql_connect("mysql.hostinger.es","u147283082_admin" ,"seminario2");
mysql_selectdb("u147283082_hlife", $conn);
if($_POST){
	$user=$_POST['user'];
	$name=$_POST['name'];
	$ins=$_POST['ins'];
	$cat=$_POST['categoria'];
        $ingredientes=$_POST['ingredientes'];
        $query = "INSERT INTO Receta (Nombre, Instrucciones, Usuario, Fecha, Categoria, Foto) SELECT '".$name."','".$ins."',Usuario.id, CURRENT_DATE(),'".$cat."','".$file_path."' FROM Usuario WHERE Usuario.nick='".$user."';";
	//mysql_query("BEGIN");
	$result1 = mysql_query($query);
	//mysql_query("COMMIT");	
        if($result1){
           $repid = mysql_insert_id();
           $ings = json_decode($ingredientes);
           foreach ($ings as $ng) {
             $query = "INSERT INTO Receta_Ingrediente (idReceta, idIngrediente, gramos, litros, unidades) SELECT ".$repid.",Ingrediente.id,".$ng->gramos.",".$ng->litros.",".$ng->unidades." FROM Ingrediente WHERE Ingrediente.Nombre='".$ng->nombre_ingrediente."';";
             $reulta2=mysql_query($query);
           }
           mysql_close($conn);
	}
        
}
?>