package com.inenergis.dao.bigdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.inenergis.dao.bigdata", "com.inenergis.entity"},
        entityManagerFactoryRef = "redshiftEntityManager",
        transactionManagerRef = "redshiftTransactionManager"
)
public class RedshiftConfiguration {
    private static final Logger log = LoggerFactory.getLogger(RedshiftConfiguration.class);

    @Value("${redshift.jdbc.driverClassName}")
    private String driverClassName;
    @Value("${redshift.jdbc.url}")
    private String url;
    @Value("${redshift.jdbc.user}")
    private String user;
    @Value("${redshift.jdbc.pass}")
    private String password;
    @Value("${redshift.hibernate.dialect}")
    private String hibernateDialect;
    @Value("${redshift.hibernate.temp.use_jdbc_metadata_defaults}")
    private String useJdbcMetadataDefaults;

    @Bean
    public LocalContainerEntityManagerFactoryBean redshiftEntityManager() {
        log.info("redshiftEntityManager");

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(redshiftDatasource());
        em.setPackagesToScan(new String[]{"com.inenergis.model.meter"});
        em.setPersistenceUnitName("redshift");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", useJdbcMetadataDefaults);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource redshiftDatasource() {
        log.info("redshiftDatasource");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager redshiftTransactionManager() {
        log.info("redshiftTransactionManager");

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(redshiftEntityManager().getObject());
        return transactionManager;
    }
}
