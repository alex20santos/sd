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
	<a href="insertPerson.jsp">Registar Pessoa</a>
	<p>
	<a href="editPerson.jsp">Editar dados de uma pessoa</a>
	<p>
	<a href="depManagementMenu.jsp"> Gerir departamentos</a>
	<p>
	<a href="createElection.jsp"> Criar eleição</a>
	<p>
	<p>
	<form id = "myForm" action="candidatesManage" method="post">
		<s:hidden name="type" value="getElections" />
    		<a href="candidatesMenu.jsp" onclick="document.getElementById('myForm').submit();">Gestão de listas candidatas a uma eleição</a>
	</form>
	<p>
	<form id = "myForm" action="Elections" method="post">
		<s:hidden name="type" value="getElections" />
    		<a href="chooseElection.jsp" onclick="document.getElementById('myForm').submit();">Editar eleição</a>
	</form>
	<p>
	<a href="antecipatedVoteLogin.jsp"> Voto antecipado</a>
	<p>
	<a href="placeOfVote.jsp"> Ver em que local votou um eleitor</a>
	<p>	
	<form id = "myForm" action="Elections" method="post">
		<s:hidden name="type" value="pastElections" />
    		<a href="choosePreviousElection.jsp" onclick="document.getElementById('myForm').submit();">Mostrar informação detalhada de eleições passadas</a>  
    		<s:submit value="Choose"/> 		
	</form>
	<p>
	<a href="membersManageMenu.jsp"> Gestão de membros de mesa de voto</a>
	
	
	

</body>
</html>