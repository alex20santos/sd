<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Person</title>
</head>
<body>
	<h1>Gest�o de listas candidatas a uma eleicao</h1>
	
	<s:form action="candidatesManage" method="post">
		Nome da lista:
		<s:textfield name="listName" />
		Tipo de lista:
		<s:textfield name="listType" />
		ID's dos membros da lista (separados por ","):
		<s:textfield name="members" />
		<s:hidden name="type" value="createCandidateList" />
		<s:submit value="Criar nova lista"/>
	</s:form>
	
	<s:form action="candidatesManage" method="post">
		Nome da lista:
		<s:textfield name="listName" />
		<s:hidden name="type" value="deleteCandidateList" />
		<s:submit value="Eliminar lista"/>
	</s:form>
</body>
</html>