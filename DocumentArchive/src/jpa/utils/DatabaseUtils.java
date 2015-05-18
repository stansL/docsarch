package jpa.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
	private static Connection con;
	private DatabaseUtils(){
		
	}
	
	public static Connection getConnection(){
		String url="jdbc:mysql://localhost:3306/management";
		String username="root";
		String password="2806";		
		
		if(con!=null){
			return con;
		}else{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}		
		
		return con;
	}
	
	

}
