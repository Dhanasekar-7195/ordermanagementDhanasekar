package com.ordermanagement.tenant;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantAwareRoutingDataSource extends AbstractRoutingDataSource {
	
	@Override
    protected Object determineCurrentLookupKey() {
        String tenant = TenantContext.getCurrentTenant();
        if (tenant == null) {
            tenant = "default"; 
        }
        return tenant;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();

      
        String currentTenant = TenantContext.getCurrentTenant();

        if (currentTenant != null) {
        	System.out.println("Setting schema for tenant: " + currentTenant);
            connection.createStatement().execute("SET search_path TO " + currentTenant);
        }

        return connection;
    }

	

}
