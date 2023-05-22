package org.java.country;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
//		milestone 1 done
//		SELECT c.name, c.country_id , r.name, c2.name 
//		FROM countries c 
//			JOIN regions r 
//			ON c.region_id = r.region_id 
//			JOIN continents c2 
//			ON r.continent_id = c2.continent_id 
//		ORDER BY c.name ASC;
		
		String url = "jdbc:mysql://localhost:3306/countries";
		String user = "root";
		String password = "code";

		try (Scanner sc = new Scanner(System.in); 
				Connection con = DriverManager.getConnection(url, user, password)){
			
			String sql = "SELECT c.name, c.country_id , r.name, c2.name "
					+ "FROM countries c "
					+ "JOIN regions r "
					+ "ON c.region_id = r.region_id "
					+ "JOIN continents c2 "
					+ "ON r.continent_id = c2.continent_id "
					+ "WHERE c.name LIKE ?"
					+ "ORDER BY c.name ASC";
			
			System.out.print("Ricerca passeggeri per nome nazionalità: ");
			String searchLastname = sc.nextLine();
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {				
					
				ps.setString(1,"%" + searchLastname + "%");
				
				try (ResultSet rs = ps.executeQuery()) {					
					while(rs.next()) {
						
						final String c1 = rs.getString(1);
						final int id = rs.getInt(2);
						final String r1 = rs.getString(3);
						final String c2 = rs.getString(4);
						
						System.out.println(c1 + " - " + id + " - " + r1 + " - " + c2);
					}				
				}	
				
					
			} catch (SQLException ex) {
				System.err.println("Query not well formed");
			}
		} catch (SQLException ex) {
			System.err.println("Error during connection to db");
		}
	}
}
