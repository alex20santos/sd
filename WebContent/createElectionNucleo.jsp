<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Election Nucleo</title>
</head>
<body>
	<h2>Criar eleição nucleo de estudantes</h2>
	<s:form action="Elections" method="post">
		<br>
		<br>
		<s:text name="Titulo:" />
		<s:textfield name="name" />
		<br>
		<br>
		<s:text name="Descrição:" />
		<s:textfield name="description" />
		<br>
		<br>
		<s:text name="Departamento:" />
		<s:textfield name="department" />
		<br>
		<br>
		<s:text name="Data (dd/mm/aaaa)):" />
		<s:textfield name="date" />
		<br>
		<br>
		<s:text name="Hora inicio (hh:mm):" />
		<s:textfield name="hInit" />
		<br>
		<br>
		<s:text name="Hora fim (hh:mm):" />
		<s:textfield name="hEnd" />
		<br>
		<br>
		<s:hidden name="type" value="createElectionNucleo"/>
		<s:submit/>
	</s:form>
	<br>
	<p><a href="admin_menu.jsp">Back</a></p>
</body>
</html>