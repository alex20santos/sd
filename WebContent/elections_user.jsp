<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="rmi.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Available Elections</title>
</head>
<body>
<h1>Presenting elections: </h1>

	<c:forEach items="${session.elections_user_now}" var="value">
         <s:form action="vote" method="post">
           		${value.name}
			<s:hidden name="type" value="getCandidates" />
			<input type="hidden" name="chosenElectionString" value="${value.name}">
			<s:submit value="Choose"/>
		</s:form>
		<p>
	</c:forEach>

	
	
	<p><a href="<s:url action="index" />">Back</a></p>
</body>
</html>