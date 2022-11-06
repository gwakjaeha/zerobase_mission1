package db;
import java.sql.*;

public class DBConnect {
	public Connection conn;
	public PreparedStatement pstmt;
	public ResultSet rs;
	
	public DBConnect() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String dbUrl = "jdbc:mariadb://localhost:3306/db1";
	        String dbUserId = "testuser";
	        String dbPassword = "jaeha";
			
			conn = DriverManager.getConnection(dbUrl, dbUserId, dbPassword);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dbClose() {
		try {
			if(conn != null) conn.close();
			if(pstmt != null) pstmt.close();
			if(rs != null) rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
