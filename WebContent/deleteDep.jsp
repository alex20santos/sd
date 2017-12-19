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


	<h1>Delete Department</h1>

	<s:form action="DepartmentManagement" method="post">
		<s:text name="Nome do departamento:" />
		<s:textfield name="depName" />
		<s:hidden name="type" value="deleteDep"/>
		<s:submit/>
	</s:form>

</body>
</html>