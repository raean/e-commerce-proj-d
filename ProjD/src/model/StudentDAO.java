package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO  {
	private List<StudentBean> students;
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	
	public List<StudentBean> retrieve(String namePrefix, double minGpa, String sortOption) throws Exception {
		this.students = new ArrayList<>();
		
		
		// We connect to the database here:
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection con = DriverManager.getConnection(DB_URL);
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		// We create our own schema and add the placeholder value manually.
		String query = "SELECT * FROM sis WHERE gpa >= " + minGpa + " AND (surname LIKE '" + namePrefix +
				"%' OR givenname LIKE '" + namePrefix +"%') ORDER BY " + sortOption;
		System.out.println(query);
		ResultSet r = s.executeQuery(query);
		try {
			while (r.next()) {
				this.students.add(new StudentBean(r.getString("surname")+", "+ r.getString("givenname"),
						r.getString("major"), Integer.parseInt(r.getString("courses")), Double.parseDouble(r.getString("gpa"))));
			} 
		} catch (Exception e) {
			throw new Exception();
		}
		r.close();
		s.close();
		con.close();
		
		return this.students;
	}
	
}
