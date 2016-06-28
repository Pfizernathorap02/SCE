package com.pfizer.sce.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleDriver;

public class JdbcConnectionUtil {

	private static Connection con = null;

	public static Connection getJdbcConnection() {

		try {
			DriverManager.registerDriver(new OracleDriver());
			con = DriverManager
					.getConnection("jdbc:oracle:thin:sales_call/pfizer#01@amrndhp005c.pfizer.com:1556:ENID80");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}

	public static void closeJdbcConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
