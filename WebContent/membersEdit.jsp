<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Members of voting table</title>
</head>
<body>
	
	    <s:form action="membersManagement" method="post">
	        	Numero de CC:
	        <s:textfield name="userID"/>
	        <p>
	        1 - Adicionar membro
	        <p>
	        2 - Remover membro
	        <p>
	        3 - Mudar delegado
	        <p>
	        <s:hidden name="type" value=""/>
	        <s:textfield name="op"/>
			<s:submit />

	        <p>
        	</s:form>
        
</body>
</html>