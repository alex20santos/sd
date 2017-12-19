<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>Registo de pessoa</h2>
	<s:form action="PersonRegister" method="post">
		<br>
		<s:text name="Tipo de registo (estudante/professor/trabalhador):" />
		<s:textfield name="tag" />
		<br>
		<br>
		<s:text name="Numero de cc:" />
		<s:textfield name="username" />
		<br>
		<br>
		<s:text name="Password:" />
		<s:textfield name="password" />
		<br>
		<br>
		<s:text name="Departamento:" />
		<s:textfield name="dep_name" />
		<br>
		<br>
		<s:text name="Expiração do cartão (dd/mm/aaaa)):" />
		<s:textfield name="expDate" />
		<br>
		<br>
		<s:text name="Contacto telfonico:" />
		<s:textfield name="contact" />
		<br>
		<br>
		<s:text name="Morada:" />
		<s:textfield name="address" />
		<br>
		<br>
		<s:submit/>
	</s:form>
	<br>
	<p><a href="admin_menu.jsp">Back</a></p>
</body>
</html>