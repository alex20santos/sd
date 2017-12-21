<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Live election info</title>
</head>
<body>
	<h1>Eleições a acontecer</h1>

         <s:form action="ElectionsNow" method="post">
           		Titulo: ${session.chosenElection.name}
           		<p>
           		Descrição: ${session.chosenElection.description}
           		<p>
           		Data: ${session.chosenElection.date}
           		<p>
           		Candidatos:
				<c:forEach items="${session.candidatesOfElection}" var="value" varStatus="status">
					<p>
					<span style="margin-left:76px">${value.name} com ${session.candidatesVotes[status.index]} votos</span>  
					<p>				
				</c:forEach>
				Numero total de votos na eleicao: ${session.totalVotes}
		</s:form>
		<p>
	
</body>
</html>