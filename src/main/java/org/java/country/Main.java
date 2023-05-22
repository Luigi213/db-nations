package org.java.country;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
			String searchCountry = sc.nextLine();
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {				
					
				ps.setString(1,"%" + searchCountry + "%");
				
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
			String sql1 = "SELECT DISTINCT c.name, l.`language`, cs.`year`, cs.population, cs.gdp "
					+ "FROM countries c "
					+ "JOIN country_languages cl "
					+ "ON c.country_id = cl.country_id "
					+ "JOIN languages l "
					+ "ON cl.language_id = l.language_id "
					+ "JOIN country_stats cs "
					+ "ON c.country_id = cs.country_id "
					+ "WHERE c.country_id = ? "
					+ "ORDER BY cs.`year` DESC ";
			
			System.out.print("Scegli un id: ");
			int searchCountryId = sc.nextInt();
			
			try (PreparedStatement ps = con.prepareStatement(sql1)) {				
				
				ps.setInt(1, searchCountryId);
				
				try (ResultSet rs = ps.executeQuery()) {
					int min = Integer.MAX_VALUE;
					int max = Integer.MIN_VALUE;
					
					Set<String> mySetCountry = new HashSet<>();
					Set<String> mySetLanguages = new HashSet<>();
					List<Integer> myListYear = new ArrayList<>();
					List<Long> myListPopulation = new ArrayList<>();
					List<Long> myListGpd = new ArrayList<>();
					while(rs.next()) {
						
						final String countryName = rs.getString(1);
						mySetCountry.add(countryName);
						final String languages = rs.getNString(2);
						mySetLanguages.add(languages);
						final int years = rs.getInt(3);
						myListYear.add(years);
						final long population = rs.getLong(4);
						myListPopulation.add(population);
						final long gdp = rs.getLong(5);
						myListGpd.add(gdp);
					}		
					System.out.print("Deatails for country: ");
					for(String s : mySetCountry) {
						System.out.println(s);
					}
					System.out.print("Languages: ");
					for(String s : mySetLanguages) {
						System.out.print(s + ", ");
					}
					System.out.println("Most recent stats");
					System.out.println("Year: " + myListYear.get(0));
					System.out.println("Population: " + myListPopulation.get(0));
					System.out.println("GDP: " + myListGpd.get(0));
				}
			} catch (SQLException ex) {
				System.err.println("Query not well formed");
			}	
		} catch (SQLException ex) {
			System.err.println("Error during connection to db");
		}
	}
}
