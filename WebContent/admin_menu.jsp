<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<h1>Admin Console</h1>
	<p>
	<form action="Person" method="post">
		<s:submit value="Registar Pessoa"></s:submit>
	</form>
	<p>
	<form action="EditPerson" method="post">
		<s:submit value="Editar dados de uma Pessoa"></s:submit>
	</form>
	<p>
	<form action="DepManage" method="post">
		<s:submit value="Gerir departamentos"></s:submit>
	</form>
	<p>
	<form action="CreateEl" method="post">
		<s:submit value="Criar eleição"></s:submit>
	</form>
	<p>
	<p>
	<form id = "myForm" action="candidatesManage" method="post">
		<s:hidden name="type" value="getElections" />
		<s:submit value="Gestão de listas candidatas a uma eleição"></s:submit>
	</form>
	<p>
	<form id = "myForm" action="EditElections" method="post">
		<s:hidden name="type" value="getElections" />
    		<s:submit value="Editar eleição"></s:submit>
	</form>
	<p>
	<form action="AntVote" method="post">
		<s:submit value="Voto antecipado"></s:submit>
	</form>
	<p>
	<form action="PlaceVote" method="post">
		<s:submit value="Ver em que local votou um eleitor"></s:submit>
	</form>
	<p>	
	<form action="ElectionsNow" method="post">
		<s:hidden name="type" value="electionsNow" />
		<s:submit value="Mostrar informação detalhada de eleições a acontecer neste momento"></s:submit>
	</form>
	<p>
	<form id = "myForm" action="Elections" method="post">
		<s:hidden name="type" value="pastElections" />
		<s:submit value="Consultar detalhes de eleições passadas"></s:submit>
	</form>
	<p>
	<form action="MembersMenu" method="post">
		<s:submit value="Gestão de membros de mesa de voto"></s:submit>
	</form>
	<p>

	
	

</body>
</html>