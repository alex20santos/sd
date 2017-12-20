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
	<h3>Login User</h3>
	<s:form action="antecipatedVote" method="post">
		User id:
		<s:textfield name="username" />
		Password:
		<s:textfield name="password" />
		<s:hidden name="type" value="getElections" />
		<s:submit value="Login"/>
	</s:form>
	<p>
</body>
</html>