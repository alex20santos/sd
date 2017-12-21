<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Election</title>
</head>
<body>
	<h1>New Election</h1>

	<p>
	<form action="CreateCG" method="post">
		<s:submit value="Conselho Geral"></s:submit>
	</form>
	<p>
	
	<form action="CreateNucleo" method="post">
		<s:submit value="Nucleo de Estudantes"></s:submit>
	</form>
	<p>
	<form action="CreateDep" method="post">
		<s:submit value="Departamento"></s:submit>
	</form>
	<p-->


	<br>
	<p><a href="admin_menu.jsp">Back</a></p>
	

</body>
</html>