<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Members Management</title>
</head>
<body>

        <s:form action="membersManagement" method="post">
            Nome do departamento:
	        	<s:textfield name="depName"/>
	        	<p>
			<s:hidden name="type" value="setDep"/>
			<s:submit value="Submit"/>
		</s:form>
        

</body>
</html>