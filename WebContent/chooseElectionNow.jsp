<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Live Elections</title>
</head>
<body>
	<h1>Eleições a acontecer </h1>


	<s:form action="ElectionsNow" method="post">
		<c:forEach items="${session.electionsNow}" var="value">
	           ${value.name}
				<input type="hidden" name="chosenElectionString" value="${value.name}">
				<s:hidden name="type" value="getInfo" />
				<s:submit value="Choose"/>
			<p>
		</c:forEach>
	</s:form>
	
</body>
</html>