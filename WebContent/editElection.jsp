<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit election</title>
</head>
<body>
	<h1>Editar Eleição</h1>
	<s:form action="EditElections" method="post">
			<p>
		1 - Data
			<p>
		2 - Nome
			<p>
		3 - Descrição
			<p>
        Option: <s:textfield name="option" />
        <p>
        Novo conteudo no campo escolhido: <s:textfield name="newContent" />
		<s:hidden name="type" value="editElection" />
		<s:submit/>
	</s:form>
	
</body>
</html>