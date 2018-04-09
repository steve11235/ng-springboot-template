package com.fusionalliance.internal.springboottemplate.application;

import java.io.IOException;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fusionalliance.internal.sharedspringboot.SpringContextHelper;

/**
 * This class is the Spring Boot entry point. It also provides access to select methods of the Spring application context.
 * <p>
 * The persistence beans are configured in such a way as to allow multiple data source, session factories, and transaction managers. The configuration
 * values are hard-code here for clarity; they can easily be externalized.
 * <p>
 * Note that the primary transaction manager name is "DefaultTransactionManager"; this name is used in the base implementation of TransactionLayer.
 * Other transaction managers can be added with their own names.
 */
@Configuration
@EnableAutoConfiguration(exclude = { //
		DataSourceAutoConfiguration.class, //
		DataSourceTransactionManagerAutoConfiguration.class, //
		HibernateJpaAutoConfiguration.class, //
		JdbcTemplateAutoConfiguration.class, //
		JpaRepositoriesAutoConfiguration.class //
})
@ComponentScan
@ServletComponentScan(basePackages = "com.fusionalliance.internal.springboottemplate.service")
@EnableTransactionManagement()
public class Application {
	public static void main(final String... args) throws Exception {
		// Put the application context in a helper class to increase its visibility across projects
		SpringContextHelper.setApplicationContext(SpringApplication.run(Application.class, new String[0]));

//		Retained for debugging purposes
//		final SortedSet<String> beanNames = new TreeSet<>(Arrays.asList(SpringContextHelper.getApplicationContext().getBeanDefinitionNames()));
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
	}

	/**
	 * Create a pooled DataSource for the default database, in this case, PostgreSQL.
	 * <p>
	 * Use the Tomcat pooled DataSource. Spring Boot 2 prefers HikariDataSource, but this appears to be a new implementation, and, as of April 2018,
	 * the documentation seems sparse.
	 * 
	 * @return
	 */
	@Bean(name = "defaultDataSource")
	public DataSource createDefaultDataSource() {
		final DataSource dataSource = new DataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://ec2-54-83-58-222.compute-1.amazonaws.com:5432/d6iub63mmp60e3");
		dataSource.setUsername("zyzpvqwduyqiti");
		dataSource.setPassword("3cd7e11084757b2323a3df58891be95bcbc3e14c986e793909e7aed3c50dd254");
		dataSource.setConnectionProperties("sslmode=require; testOnBorrow=true; validationInterval=30000; maxAge=600000;");

		return dataSource;
	}

	/**
	 * Create a Hibernate session factory for the default DataSource.
	 * 
	 * @param defaultDataSourceParm
	 * @return
	 */
	@Bean(name = "defaultSessionFactory")
	public SessionFactory getHibernateSessionFactory(@Qualifier("defaultDataSource") final DataSource defaultDataSourceParm) {
		// Spring artifice for preparing a SessionFactory; a factory prepares a factory
		final LocalSessionFactoryBean sessionFactoryFactory = new LocalSessionFactoryBean();
		sessionFactoryFactory.setDataSource(defaultDataSourceParm);
		sessionFactoryFactory.setPackagesToScan("com.fusionalliance.internal.business.entity");

		final Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		hibernateProperties.put("show.sql", true);
		hibernateProperties.put("format.sql", true);
		hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.put("hibernate.jdbc.time_zone", "UTC");

		sessionFactoryFactory.setHibernateProperties(hibernateProperties);
		try {
			sessionFactoryFactory.afterPropertiesSet();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		final SessionFactory sessionFactory = sessionFactoryFactory.getObject();

		return sessionFactory;
	}

	/**
	 * Create a Hibernate transaction manager for the default Hibernate session factory.
	 * 
	 * @param sessionFactoryParm
	 * @return
	 */
	@Bean(name = "defaultTransactionManager")
	public HibernateTransactionManager createHibernateTransactionManager(
			@Qualifier("defaultSessionFactory") final SessionFactory sessionFactoryParm) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactoryParm);

		return transactionManager;
	}
}
