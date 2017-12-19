<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Person</title>
</head>
<body>
	<h1>Editar Pessoa</h1>
	<s:form action="PersonEditor" method="post">
		<s:text name="Numero de cc:" />
		<s:textfield name="id" />
			<p>
		1 - Validade do Cartão de Cidadão 
			<p>
		2 - Morada
			<p>
		3 - Contacto telefonico
			<p>
        4 - Departamento
        		<p>
        5 - Função
        		<p>
        6 - Password
        		<p>
        Option: <s:textfield name="option" />
        <p>
        Novo conteudo no campo escolhido: <s:textfield name="newContent" />
		<s:submit/>
	</s:form>
	
</body>
</html>