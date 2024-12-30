package com.ordermanagement.DBConfig;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.ordermanagement.tenant.TenantAwareRoutingDataSource;

@Configuration
public class MultitenantConfig {
	
	  @Autowired
	    private DataSourceProperties dataSourceProperties;

	    @Bean
	    @Primary
	    public DataSource dataSource() {
	        TenantAwareRoutingDataSource dataSource = new TenantAwareRoutingDataSource();

	        Map<Object, Object> dataSources = new HashMap<>();
	        dataSources.put("default", createDataSource(
	                dataSourceProperties.getUrl(),
	                dataSourceProperties.getUsername(),
	                dataSourceProperties.getPassword()
	        ));

	        // Add more tenants
//	        dataSources.put("gsus", createDataSource("jdbc:postgresql://localhost:5432/gsus", "postgres", "1234"));
//	        dataSources.put("software", createDataSource("jdbc:postgresql://localhost:5432/software", "postgres", "1234"));
//	        dataSources.put("sevenwonders", createDataSource("jdbc:postgresql://localhost:5432/sevenwonders", "postgres", "1234"));

	        dataSource.setTargetDataSources(dataSources);
	        dataSource.setDefaultTargetDataSource(dataSources.get("default"));

	        return dataSource;
	    }

	    public DataSource createDataSource(String url, String username, String password) {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName("org.postgresql.Driver");
	        dataSource.setUrl(url);
	        dataSource.setUsername(username);
	        dataSource.setPassword(password);
	        return dataSource;
	    }

	    @Bean(name = "entityManagerFactory")
	    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
	                                                                        @Qualifier("dataSource") DataSource dataSource) {
	        return builder
	                .dataSource(dataSource)
	                .packages("com.ordermanagement.*")
	                .build();
	    }

	    @Bean(name = "transactionManager")
	    public PlatformTransactionManager transactionManager(
	            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
	        return new JpaTransactionManager(entityManagerFactory.getObject());
	    }


}
