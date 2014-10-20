<?php
$conn = mysql_connect("mysql.hostinger.es","u147283082_admin" ,"seminario2");
mysql_selectdb("u147283082_hlife", $conn);
if($_POST){
	$user=$_POST['user'];
	$name=$_POST['name'];
	$pass=$_POST['pass'];
	$email=$_POST['email'];
        $query = mysql_query("SELECT nick FROM Usuario WHERE nick='".$user."'");
        if (mysql_num_rows($query) == 0){
        $query = mysql_query("SELECT email FROM Usuario WHERE email='".$email."'");
        if (mysql_num_rows($query) == 0){
	$query = "INSERT INTO Usuario (nick, nombre, pass, email) VALUES ('".$user."','".$name."','".$pass."','".$email."');";
	//mysql_query("BEGIN");
	$result1 = mysql_query($query);
	//mysql_query("COMMIT");	
        if($result1){
            echo 'Insertado';
           mysql_close($conn);
	}
        }else{
            echo 'Ya existe un usuario con ese correo electrónico';
        }
        }else{
            echo 'Ya existe un usuario con ese nombre';
        }
}
?>
						