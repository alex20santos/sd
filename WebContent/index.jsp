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
	<h1>Login Menu</h1>
	<h3>Login User</h3>
	<s:form action="login" method="post">
		User id:
		<s:textfield name="username" />
		Password:
		<s:textfield name="password" />
		<s:submit value="Login"/>
	</s:form>
	<p>
	<h3>Login Admin</h3>
	<s:form action="loginAdmin" method="post">
		Username admin:
		<s:textfield name="username" />
		Password:
		<s:textfield name="password" />
		<s:submit value="Login"/>
	</s:form>

</body>
</html>