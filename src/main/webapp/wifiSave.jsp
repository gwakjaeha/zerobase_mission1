<%@page import="db.WifiDAO"%>
<%@page import="java.io.IOException"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
p{
	font-family: Arial;
	font-size: 30px;
	font-weight: 700;
	text-align: center;
}
</style>
<body>
	<%
	int affectedRow = 0; 
	try{
		WifiDAO wifiDAO = new WifiDAO();
		affectedRow = wifiDAO.WifiInsert();
	}
	catch(IOException e){
		e.printStackTrace();
	}
	%>
	<p class='title'> <%=affectedRow%>개의 WIFI정보를 정상적으로 저장하였습니다.</p>
	<a href="http://localhost:8080/zerobase-mission1/wifiMain.jsp"> <center> 홈으로 가기 </center> </a>
</body>
</html>