
<%@page import="java.util.List"%>
<%@page import="db.WifiDAO"%>
<%@page import="data.WifiDTO"%>
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
input::placeholder {
  color: black;
  font-style: default;
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
	
	<a href="http://localhost:8080/zerobase-mission1/wifiSave.jsp">Open API 와이파이 정보 가져오기</a>
	
	<br>
	<br>
	
	<%
		String latStr = request.getParameter("lat");
		String lntStr = request.getParameter("lnt");
		
		List<WifiDTO> wifiList = null;
		
		if(latStr != null && lntStr != null && !"".equals(latStr) && !"".equals(lntStr)){
			double lat = Double.parseDouble(latStr);
			double lnt = Double.parseDouble(lntStr);
			WifiDAO wifiDAO = new WifiDAO();
			wifiList = wifiDAO.nearestWifiSelect(lat, lnt);	
			wifiDAO.historyInsert(lat, lnt);
		}
	 if (wifiList != null){
	 %>
	 	<form method="get" action="wifiMain.jsp">
			LAT: <input type="text" name="lat" value=<%=latStr%>>
			LNT: <input type="text" name="lnt" value=<%=lntStr%>>
			
			<input type="button" value="내 위치 가져오기" onclick='getCurPos()'>
			<input type="submit" value="근처 wifi 정보보기">
		</form>
	
		<table>
			<thead>
				<tr>
					<th>거리(km)</th>
					<th>관리번호</th>
					<th>자치구</th>
					<th>와이파이명</th>
					<th>도로명주소</th>
					<th>상세주소</th>
					<th>설치위치(층)</th>
					<th>설치유형</th>
					<th>설치기관</th>
					<th>서비스구분</th>
					<th>망종류</th>
					<th>설치년도</th>
					<th>실내외구분</th>
					<th>WIFI접속환경</th>
					<th>X좌표</th>
					<th>Y좌표</th>
					<th>작업일자</th>
				</tr>
			</thead>
			<tbody>
				<tr>
				<%
					for(WifiDTO wifi : wifiList){
				%>
					<tr>
						<td> <%=wifi.getDistance()%> </td>
						<td> <%=wifi.getX_swifi_mgr_no()%> </td>
						<td> <%=wifi.getX_swifi_wrdofc()%> </td>
						<td> <%=wifi.getX_swifi_main_nm()%> </td>
						<td> <%=wifi.getX_swifi_adres1()%> </td>
						<td> <%=wifi.getX_swifi_adres2()%> </td>
						<td> <%=wifi.getX_swifi_instl_floor()%> </td>
						<td> <%=wifi.getX_swifi_instl_ty()%> </td>
						<td> <%=wifi.getX_swifi_instl_mby()%> </td>
						<td> <%=wifi.getX_swifi_svc_se()%> </td>
						<td> <%=wifi.getX_swifi_cmcwr()%> </td>
						<td> <%=wifi.getX_swifi_cnstc_year()%> </td>
						<td> <%=wifi.getX_swifi_inout_door()%> </td>
						<td> <%=wifi.getX_swifi_remars3()%> </td>
						<td> <%=wifi.getLat()%> </td>
						<td> <%=wifi.getLnt()%> </td>
						<td> <%=wifi.getWork_dttm()%> </td>
					</tr>
				<%
					}
				%>
				</tr>
			</tbody>
		</table>
	<% } else { %>
		<form method="get" action="wifiMain.jsp">
			LAT: <input type="text" name="lat" placeholder=0.0>
			LNT: <input type="text" name="lnt" placeholder=0.0>
			
			<input type="button" value="내 위치 가져오기" onclick="getUserLocation()">
			<input type="submit" value="근처 wifi 정보보기">
		</form>
	<% } %>
	
<script type = "text/javascript">
	function success({ coords, timestamp }) {
        const latitude = coords.latitude;   // 위도
        const longitude = coords.longitude; // 경도
        
        var lat = document.getElementsByName("lat");
		var lnt = document.getElementsByName("lnt");
        
		lat[0].value = latitude;
		lnt[0].value = longitude;
    }

    function getUserLocation() {
        if (!navigator.geolocation) {
            throw "위치 정보가 지원되지 않습니다.";
        }
        return navigator.geolocation.getCurrentPosition(success);
    }
</script>

</body>
</html>