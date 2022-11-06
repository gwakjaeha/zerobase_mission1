<%@page import="java.util.List"%>
<%@page import="db.WifiDAO"%>
<%@page import="data.HistoryDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
.border-separator {
  padding-right: 0.5em;
  border-right: 1px solid #000;
  margin-right: 0.5em;
}
.title{
font-family: Arial;
font-size: 30px;
font-weight: 700;
}
table{
	width: 100%;
	font-size: 10px;
}
th, td{
	border:solid 1px #000;
}

</style>
<body>
	<p class="title">와이파이 정보 구하기</p>
	<span class="border-separator">
		<a href="http://localhost:8080/zerobase-mission1/wifiMain.jsp">홈</a>
	</span>
	<span class="border-separator">
		<a href="http://localhost:8080/zerobase-mission1/history.jsp">위치 히스토리 목록</a>
	</span>
	
	<a href="http://localhost:8080/zerobase-mission1/wifiMain.jsp">Open API 와이파이 정보 가져오기</a>
	<br>
	<br>
	
	<%
		String idStr = request.getParameter("id");
		
		WifiDAO wifiDAO = new WifiDAO();
		
		if(idStr != null && !"".equals(idStr)){
			int id = Integer.parseInt(idStr);
				
			wifiDAO.historyDelete(id);
		}
	
		List<HistoryDTO> historyList = wifiDAO.historySelect();
		
	if (historyList != null){
	%>
		<table id='historyTable'>
			<thead>
				<tr>
					<th>ID</th>
					<th>X좌표</th>
					<th>Y좌표</th>
					<th>조회일자</th>
					<th>비고</th>
				</tr>
			</thead>
			<tbody>
				<tr>
				<%
					for(HistoryDTO history : historyList){
				%>
					<tr>
						<td> <%=history.getId()%> </td>
						<td> <%=history.getLat()%> </td>
						<td> <%=history.getLnt()%> </td>
						<td> <%=history.getViewDate()%> </td>
						<td> <center> <button class="btn" type="button" onclick="getId()">삭제</button> </center>
						</td>
					</tr>
				<%
					}
				%>
				</tr>
			</tbody>
		</table>
	<%
	}
	%>
	<script type = "text/javascript">
		function getId(){
			let historyTable = document.getElementById('historyTable');
			for(let i = 2; i < historyTable.rows.length; i++){
				historyTable.rows.item(i).cells.item(4).onclick = function(){
					let id = historyTable.rows.item(i).cells.item(0).innerText;
					location.href = "http://localhost:8080/zerobase-mission1/history.jsp?" + "id=" + id;
				}
			}
		}
	</script>
</body>
</html>