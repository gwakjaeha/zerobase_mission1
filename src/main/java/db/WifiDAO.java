package db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import data.HistoryDTO;
import data.WifiDTO;

public class WifiDAO {
	
	public int WifiInsert() throws IOException {
		int index = 1;
		int affectedCount = 0;
		
		while(true){
			StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
			urlBuilder.append("/" + URLEncoder.encode("426d79587a6b6a6837326a5550534d","UTF-8") ); /*인증키*/
			urlBuilder.append("/" + URLEncoder.encode("json","UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
			urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
			urlBuilder.append("/" + URLEncoder.encode(String.valueOf(index),"UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
			urlBuilder.append("/" + URLEncoder.encode(String.valueOf(index + 999),"UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
			
			index += 1000;

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/xml");
			
			BufferedReader rd;
			
			// 서비스코드가 정상이면 200~300사이의 숫자가 나옴
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				conn.disconnect();
				break;
			}
			
			StringBuilder sb = new StringBuilder();
			String line;
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			JsonElement element = JsonParser.parseString(sb.toString());

			if(!element.getAsJsonObject().has("TbPublicWifiInfo")) {
				conn.disconnect();
				break;
			}
			
			JsonObject root = element.getAsJsonObject().get("TbPublicWifiInfo").getAsJsonObject();
			JsonArray row = root.getAsJsonObject().get("row").getAsJsonArray();
			
			WifiDTO[] wifiArray = new WifiDTO[row.size()];
			
			for(int i = 0 ; i < row.size(); i++) {
				wifiArray[i] = new WifiDTO();
				wifiArray[i].setX_swifi_mgr_no(row.get(i).getAsJsonObject().get("X_SWIFI_MGR_NO").getAsString());
				wifiArray[i].setX_swifi_wrdofc(row.get(i).getAsJsonObject().get("X_SWIFI_WRDOFC").getAsString());
				wifiArray[i].setX_swifi_main_nm(row.get(i).getAsJsonObject().get("X_SWIFI_MAIN_NM").getAsString());
				wifiArray[i].setX_swifi_adres1(row.get(i).getAsJsonObject().get("X_SWIFI_ADRES1").getAsString());
				wifiArray[i].setX_swifi_adres2(row.get(i).getAsJsonObject().get("X_SWIFI_ADRES2").getAsString());
				wifiArray[i].setX_swifi_instl_floor(row.get(i).getAsJsonObject().get("X_SWIFI_INSTL_FLOOR").getAsString());
				wifiArray[i].setX_swifi_instl_ty(row.get(i).getAsJsonObject().get("X_SWIFI_INSTL_TY").getAsString());
				wifiArray[i].setX_swifi_instl_mby(row.get(i).getAsJsonObject().get("X_SWIFI_INSTL_MBY").getAsString());
				wifiArray[i].setX_swifi_svc_se(row.get(i).getAsJsonObject().get("X_SWIFI_SVC_SE").getAsString());
				wifiArray[i].setX_swifi_cmcwr(row.get(i).getAsJsonObject().get("X_SWIFI_CMCWR").getAsString());
				wifiArray[i].setX_swifi_cnstc_year(row.get(i).getAsJsonObject().get("X_SWIFI_CNSTC_YEAR").getAsString());
				wifiArray[i].setX_swifi_inout_door(row.get(i).getAsJsonObject().get("X_SWIFI_INOUT_DOOR").getAsString());
				wifiArray[i].setX_swifi_remars3(row.get(i).getAsJsonObject().get("X_SWIFI_REMARS3").getAsString());
				wifiArray[i].setLnt(row.get(i).getAsJsonObject().get("LNT").getAsDouble());
				wifiArray[i].setLat(row.get(i).getAsJsonObject().get("LAT").getAsDouble());
				wifiArray[i].setWork_dttm(row.get(i).getAsJsonObject().get("WORK_DTTM").getAsString());	
			}
	        
			DBConnect dbConn = new DBConnect();
			
	        try {
	
	            String sql = " INSERT INTO WIFI_INFO (x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, x_swifi_adres2, " 
	            		+ " x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, x_swifi_cmcwr, " 
	            		+ " x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lnt, lat, work_dttm) "
	            		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            
	            for(int i = 0; i < wifiArray.length; i++) {
	            	dbConn.pstmt = dbConn.conn.prepareStatement(sql);
	            	dbConn.pstmt.setString(1, wifiArray[i].getX_swifi_mgr_no());
	            	dbConn.pstmt.setString(2, wifiArray[i].getX_swifi_wrdofc());
	            	dbConn.pstmt.setString(3, wifiArray[i].getX_swifi_main_nm());
	            	dbConn.pstmt.setString(4, wifiArray[i].getX_swifi_adres1());
	            	dbConn.pstmt.setString(5, wifiArray[i].getX_swifi_adres2());
	            	dbConn.pstmt.setString(6, wifiArray[i].getX_swifi_instl_floor());
	            	dbConn.pstmt.setString(7, wifiArray[i].getX_swifi_instl_ty());
	            	dbConn.pstmt.setString(8, wifiArray[i].getX_swifi_instl_mby());
	            	dbConn.pstmt.setString(9, wifiArray[i].getX_swifi_svc_se());
	                dbConn.pstmt.setString(10, wifiArray[i].getX_swifi_cmcwr());
	                dbConn.pstmt.setString(11, wifiArray[i].getX_swifi_cnstc_year());
	                dbConn.pstmt.setString(12, wifiArray[i].getX_swifi_inout_door());
	                dbConn.pstmt.setString(13, wifiArray[i].getX_swifi_remars3());
	                dbConn.pstmt.setDouble(14, wifiArray[i].getLnt());
	                dbConn.pstmt.setDouble(15, wifiArray[i].getLat());
	                
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	                LocalDateTime dateTime = LocalDateTime.parse(wifiArray[i].getWork_dttm(), formatter);
	                dbConn.pstmt.setTimestamp(16, Timestamp.valueOf(dateTime));
	
	                int affected = dbConn.pstmt.executeUpdate();
	
	                if(affected > 0) affectedCount++;
	                
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } 
	        
	        dbConn.dbClose();
		}
		
		return affectedCount;
	}
	
	public List<WifiDTO> nearestWifiSelect(double lat, double lnt){
		List<WifiDTO> wifiList = new ArrayList<>();
      
		DBConnect dbConn = new DBConnect();
		
        try {
            
            String sql = " SELECT ROUND(6371*acos(cos(radians(?))*cos(radians(wi.lat))*cos(radians(wi.lnt)-radians(?))+sin(radians(?))*sin(radians(wi.lat))), 4) AS distance, " 
            		+ " wi.x_swifi_mgr_no, wi.x_swifi_wrdofc, wi.x_swifi_main_nm, wi.x_swifi_adres1, wi.x_swifi_adres2, wi.x_swifi_instl_floor, wi.x_swifi_instl_ty, wi.x_swifi_instl_mby, wi.x_swifi_svc_se, wi.x_swifi_cmcwr, wi.x_swifi_cnstc_year, wi.x_swifi_inout_door, wi.x_swifi_remars3, wi.lnt, wi.lat, wi.work_dttm "
            		+ " FROM WIFI_INFO wi "
            		+ " ORDER BY distance "
            		+ " LIMIT 20 ";

            dbConn.pstmt = dbConn.conn.prepareStatement(sql);
            dbConn.pstmt.setDouble(1, lat);
            dbConn.pstmt.setDouble(2, lnt);
            dbConn.pstmt.setDouble(3, lat);
            
            dbConn.rs = dbConn.pstmt.executeQuery();
            
            while(dbConn.rs.next()){
                
                WifiDTO wifi = new WifiDTO();
                
                wifi.setDistance(dbConn.rs.getDouble("distance"));
                wifi.setX_swifi_mgr_no(dbConn.rs.getString("x_swifi_mgr_no"));
				wifi.setX_swifi_wrdofc(dbConn.rs.getString("x_swifi_wrdofc"));
				wifi.setX_swifi_main_nm(dbConn.rs.getString("x_swifi_main_nm"));
				wifi.setX_swifi_adres1(dbConn.rs.getString("x_swifi_adres1"));
				wifi.setX_swifi_adres2(dbConn.rs.getString("x_swifi_adres2"));
				wifi.setX_swifi_instl_floor(dbConn.rs.getString("x_swifi_instl_floor"));
				wifi.setX_swifi_instl_ty(dbConn.rs.getString("x_swifi_instl_ty"));
				wifi.setX_swifi_instl_mby(dbConn.rs.getString("x_swifi_instl_mby"));
				wifi.setX_swifi_svc_se(dbConn.rs.getString("x_swifi_svc_se"));
				wifi.setX_swifi_cmcwr(dbConn.rs.getString("x_swifi_cmcwr"));
				wifi.setX_swifi_cnstc_year(dbConn.rs.getString("x_swifi_cnstc_year"));
				wifi.setX_swifi_inout_door(dbConn.rs.getString("x_swifi_inout_door"));
				wifi.setX_swifi_remars3(dbConn.rs.getString("x_swifi_remars3"));
				wifi.setLnt(dbConn.rs.getDouble("lnt"));
				wifi.setLat(dbConn.rs.getDouble("lat"));
				
				wifi.setWork_dttm(dbConn.rs.getTimestamp("work_dttm").toString());
				
				wifiList.add(wifi);
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        dbConn.dbClose();
        
        return wifiList;
	}
	
	public void historyInsert(double lat, double lnt){
		
		DBConnect dbConn = new DBConnect();
		
        try {

            String sql = " INSERT INTO WIFI_SEARCH_HISTORY (lnt, lat, view_date) " +
                    " values (?, ?, ?); ";

            dbConn.pstmt = dbConn.conn.prepareStatement(sql);
            dbConn.pstmt.setDouble(1, lnt);
            dbConn.pstmt.setDouble(2, lat);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

            dbConn.pstmt.setTimestamp(3, Timestamp.valueOf(dateFormat.format(calendar.getTime())));

            int affected = dbConn.pstmt.executeUpdate();

            if(affected > 0){
                System.out.println("저장 성공");
            } else {
                System.out.println("저장 실패");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        dbConn.dbClose();
	}
	
	public List<HistoryDTO> historySelect(){
		
		List<HistoryDTO> historyList = new ArrayList<>();
		
		DBConnect dbConn = new DBConnect();
		
        try {

            String sql = " SELECT * FROM WIFI_SEARCH_HISTORY "
            		+ " ORDER BY ID DESC "
            		+ " LIMIT 20; ";

            dbConn.pstmt = dbConn.conn.prepareStatement(sql);

            dbConn.rs = dbConn.pstmt.executeQuery();
            
            while(dbConn.rs.next()){
                
                HistoryDTO history = new HistoryDTO();
                
                history.setId(dbConn.rs.getInt("id"));
                history.setLnt(dbConn.rs.getDouble("lnt"));
                history.setLat(dbConn.rs.getDouble("lat"));
                history.setViewDate(dbConn.rs.getTimestamp("view_date").toString());
                
                historyList.add(history);
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        dbConn.dbClose();
        
        return historyList;
	}
	
	public void historyDelete(int id) {
		
		DBConnect dbConn = new DBConnect();
		
        try {

            String sql = " DELETE FROM WIFI_SEARCH_HISTORY " +
                    " WHERE id = ?; ";

            dbConn.pstmt = dbConn.conn.prepareStatement(sql);
            dbConn.pstmt.setInt(1, id);

            int affected = dbConn.pstmt.executeUpdate();

            if(affected > 0){
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        dbConn.dbClose();
    }
}
