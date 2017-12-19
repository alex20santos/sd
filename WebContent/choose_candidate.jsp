<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Vote</title>
</head>
<body>
<h1>Your vote options:</h1>

		<c:forEach items="${session.candidatesOfElection}" var="value">
			<s:form action="vote" method="post">
				${value.name}
				<s:hidden name="type" value="register_vote" />
				<input type="hidden" name="chosenCandidate" value="${value.name}">
				<s:submit value="Vote in this candidate"/>
				<p>
			</s:form>
		</c:forEach>
		

		
		
	<p><a href="elections_user.jsp">Back</a></p>

</body>
</html>