<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User voting places</title>
</head>
<body>
	<h2>Votos do utilizador escolhido </h2>

		<c:forEach items="${session.previousVotes}" var="value">
			<s:form action="vote" method="post">
				<h3>Eleição: ${value.election.name}</h3>
				<p>
				Local de voto: ${value.place_of_vote.name}
				<p>
			</s:form>
		</c:forEach>
</body>
</html>