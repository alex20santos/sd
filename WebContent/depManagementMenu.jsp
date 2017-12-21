<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Department Management</title>
</head>
<body>


	<h1>Department Menu</h1>


	
	<p>
	<form action="InsertDep" method="post">
		<s:submit value="Inserir"></s:submit>
	</form>
	<p>
	<form action="EditDep" method="post">
		<s:submit value="Editar"></s:submit>
	</form>
	<p>
	<form action="Deletedep" method="post">
		<s:submit value="Eliminar"></s:submit>
	</form>
	<p>


</body>
</html>