<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Artagasmator</title>
	
</head>
<body>
	<div style="margin:20px;margin-left:400px">
		<form action="j_spring_security_check" method="POST">
			<table>
				<tr>
					<td colspan="2">
						<div class="logo"></div>
					</td>
					<td>
					
					</td>
				</tr>
				<tr>
					<td>
						<label for="j_username">Usuario:</label>
						
					</td>
					
					<td>
						<input style="width:250px" id="j_username" name="j_username" type="text" />
					</td>
				</tr>
				<tr>
					<td>
						<label for="j_password">Contraseña:</label>
						
					</td>
					<td>
						<input style="width:250px" id="j_password" name="j_password" type="password"></input>
					</td>
				</tr>
				<tr>
					<td>
						
					</td>
					<td>
						<input style="width:150px" type="submit" value="Iniciar sesión"></input>
					</td>
				</tr>
			</table>
			
		</form>
		
		<br /> 
		
		<div class="recommender-subtitle" style="color:#FF9300;margin-left:-45px; margin-bottom:15px">¿Todavía no tienes usuario en Artgasmator? Regístrate.</div>
		<form action="${ctx}/login" method="POST">
			<table>
				<tr>
					<td>
						<label for="user">Nombre usuario:</label>
					</td>
					<td>
						<input style="width:250px" type="text" name="user" id="user" >
					</td>
				</tr>
				<tr>
					<td>
						<label for="password">Contraseña:</label>
					</td>
					<td>
						<input style="width:250px" type="password" name="password" id="password" >
					</td>
				</tr>
				
				<tr>
					<td>
						<label for="repeatpassword">Repetir contraseña:</label>
					</td>
					<td>
						<input style="width:250px" type="password" name="repeatpassword" id="repeatpassword" >
					</td>
				</tr>
				
				<tr>
					<td>
						
					</td>
					<td>
						<input style="width:150px" type="submit" value="Registrarse"> 	
					</td>
				</tr>
			</table>
		 </form>
	</div>
</body>
</html>
