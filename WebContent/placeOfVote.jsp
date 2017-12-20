<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Place of vote</title>
</head>
<body>
	<h2>Local em que votou um eleitor</h2>
	<s:form action="placeOfVote" method="post">
		<s:text name="Numero de cc:" />
		<s:textfield name="id" />
        <p>
        	<s:hidden name="type" value="getVotes" />
		<s:submit/>
	</s:form>
	
</body>
</html>