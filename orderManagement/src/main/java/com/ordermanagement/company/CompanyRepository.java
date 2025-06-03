package com.ordermanagement.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.entity.UserMasterEntity;

//PHASE 1

@Component
public class CompanyRepository {

	@Autowired
	private CompanyDAO cDAO;

	@Autowired
	private UserMasterDAO umDAO;

	public Map<String, Object> addCompany(CompanyDTO cDTO) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		CompanyEntity cENT = new CompanyEntity();
		cENT.setCompanyName(cDTO.getCompanyName());
		cENT.setEmail(cDTO.getEmail());
		cENT.setMobileNumber(cDTO.getMobileNumber());
		cENT.setPassword(cDTO.getPassword());
		cENT.setUserName(cDTO.getUserName());

		// Check if company already exists
		CompanyEntity existingCompany = this.cDAO.findCompanyByCompanyName(cENT.getCompanyName());
		if (existingCompany != null) {
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("error", "Company name already exists");
			return response;
		}

		UserMasterEntity existingEmail = this.umDAO.findByEmail(cENT.getEmail());
		if (existingEmail != null) {
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("error", "Email already exists");
			return response;
		}

		// Check if mobile number already exists
		UserMasterEntity existingMobile = this.umDAO.findUserByPhoneNoBy(cENT.getMobileNumber());
		if (existingMobile != null) {
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("error", "Mobile number already exists");
			return response;
		}

		if (cENT.getMobileNumber().length() != 10 || !cENT.getMobileNumber().matches("\\d+")) {
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("code", "510");
			response.put("error", "Please enter a valid 10-digit mobile number");
			return response;
		}

		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
		if (!cDTO.getPassword().matches(passwordPattern)) {
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("code", "400");
			response.put("error",
					"Password must be between 8 to 12 characters and include uppercase, lowercase, number, and special character.");
			return response;
		}

		List<UserMasterEntity> existingUsers = umDAO.findUserByNameByList(cENT.getUserName());
		for (UserMasterEntity user1 : existingUsers) {
			if (user1 != null && !user1.getUserId().equals(cENT.getTenantId())) {
				if (passwordEncoder.matches(cDTO.getPassword(), user1.getPassword())) {
					Map<String, Object> response = new LinkedHashMap<>();
					response.put("status", "Username already exists with the same password");
					return response;
				}
			}
		}

		cDAO.save(cENT);
		cENT.setPassword(passwordEncoder.encode(cDTO.getPassword()));
		cDAO.save(cENT);

		UserMasterEntity userEnt = new UserMasterEntity();
		userEnt.setUserName(cENT.getUserName());
		userEnt.setPassword(cENT.getPassword());
		userEnt.setEmail(cENT.getEmail());
		userEnt.setActive(true);
		userEnt.setRole("Admin");
		// userEnt.setToken("-");
//		    userEnt.setTokenCreationDate(null);
		userEnt.setLocation("-");
		userEnt.setCompanyName(cENT.getCompanyName());
		userEnt.setMobileNumber(cENT.getMobileNumber());
		userEnt.setReturnCredit(0);
		userEnt.setTenantId(cENT.getTenantId());

		umDAO.save(userEnt);

		// schema creation

		// String schemaName = cENT.getCompanyName();
		String schemaName = cENT.getCompanyName().toLowerCase().replace(" ", "_");
		String url = "jdbc:postgresql://postgres-05bcb8e7-5ac1-4f9f-809d-b4fb4ba125f5.cqryblsdrbcs.us-east-1.rds.amazonaws.com:7062/mynkrcQWyRjV";
		String user = "95c1692602ef";
		String password = "26bda3df300ed64e8bdb8f85";

		try (Connection conn = DriverManager.getConnection(url, user, password)) {

			if (!schemaExists(conn, schemaName)) {

				createSchema(conn, schemaName);
			} else {
				System.out.println("Schema already exists: " + schemaName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("error", "Schema creation failed: " + e.getMessage());
			return response;
		}

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "success");
		response.put("company", cENT.getCompanyName());
		response.put("TenantId", cENT.getTenantId());
		return response;
	}

	private boolean schemaExists(Connection conn, String schemaName) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			String query = "SELECT 1 FROM information_schema.schemata WHERE schema_name = '" + schemaName + "'";
			return stmt.executeQuery(query).next();
		}
	}

	private void createSchema(Connection conn, String schemaName) throws SQLException {
		// Create the schema in the company database
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("CREATE SCHEMA " + schemaName);
			System.out.println("Schema created: " + schemaName);
		}

		createTablesAndSequence(conn, schemaName);
	}

	private void createTablesAndSequence(Connection conn, String schemaName) throws SQLException {

		String[] tableCreationSQLs = {
				"CREATE TABLE IF NOT EXISTS " + schemaName + ".order_master_sap (" + "orderid VARCHAR(255) NOT NULL, "
						+ "salesordertype VARCHAR(255), " + "salesorganization VARCHAR(255), "
						+ "distributionchannel VARCHAR(255), " + "organizationdivision VARCHAR(255), "
						+ "customerid VARCHAR(255), " + "orderdate VARCHAR(255), "
						+ "incotermsclassification VARCHAR(255), " + "incotermstransferlocation VARCHAR(255), "
						+ "contactperson VARCHAR(255), " + "deliverylocation VARCHAR(255), "
						+ "postalcode VARCHAR(255), " + "streetname VARCHAR(255), " + "region VARCHAR(255), "
						+ "telephonenumber VARCHAR(255), " + "total DOUBLE PRECISION, " + "status VARCHAR(255), "
						+ "salesorderid VARCHAR(255), " + "PRIMARY KEY (orderid)" + ");",

				"CREATE TABLE IF NOT EXISTS " + schemaName + ".order_master_item_sap ("
						+ "ordermasteritemid VARCHAR(255) NOT NULL, " + "product VARCHAR(255), "
						+ "categoryname VARCHAR(255), " + "producttype VARCHAR(255), " + "baseunit VARCHAR(255), "
						+ "productdescription VARCHAR(255), " + "standardprice DOUBLE PRECISION, "
						+ "currency VARCHAR(255), " + "qty VARCHAR(255), " + "totalamount DOUBLE PRECISION, "
						+ "orderid VARCHAR(255), " + "PRIMARY KEY (ordermasteritemid), "
						+ "FOREIGN KEY (orderid) REFERENCES " + schemaName
						+ ".order_master_sap (orderid) ON DELETE CASCADE" + ");",

				"CREATE SEQUENCE " + schemaName + ".generator START WITH 1 INCREMENT BY 1;"

		};

		try (Statement stmt = conn.createStatement()) {

			for (String sql : tableCreationSQLs) {
				stmt.executeUpdate(sql);
			}
			System.out.println("Tables and sequence created for schema: " + schemaName);
		}
	}

}
