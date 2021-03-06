package com.zhongjian.webserver.beanconfiguration;

import java.io.IOException;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import com.zhongjian.webserver.common.LoggingUtil;
/**
 * mybatis的相关配置设置
 * @author Jfei
 *
 */
@Configuration
@AutoConfigureAfter(DatasourceConfig.class)
@EnableTransactionManagement
@MapperScan("com.zhongjian.webserver.mapper")
public class MybatisConfiguration implements TransactionManagementConfigurer{

    //  配置类型别名
        @Value("${mybatis.typeAliasesPackage}")
        private String typeAliasesPackage;

    //  配置mapper的扫描，找到所有的mapper.xml映射文件
//        @Value("${mybatis.mapperLocations : classpath:com/fei/springboot/dao/*.xml}")
        @Value("${mybatis.mapperLocations}")
        private String mapperLocations;

    //  加载全局的配置文件
        @Value("${mybatis.configLocation}")
        private String configLocation;
        
        @Autowired
        private DataSource dataSource;

        // 提供SqlSeesion
        @Bean(name = "sqlSessionFactory")
        @Primary
        public SqlSessionFactory sqlSessionFactory() {
            try {
                SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
                sessionFactoryBean.setDataSource(dataSource);

                // 读取配置 
                sessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
                
                //设置mapper.xml文件所在位置 
                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
                sessionFactoryBean.setMapperLocations(resources);
             //设置mybatis-config.xml配置文件位置
                sessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
                return sessionFactoryBean.getObject();
            } catch (IOException e) {
                LoggingUtil.e("mybatis resolver mapper*xml is error " + e);
                return null;
            } catch (Exception e) {
                LoggingUtil.e("mybatis sqlSessionFactoryBean create error " + e);
                return null;
            }
        }

        @Bean
        public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        	return new SqlSessionTemplate(sqlSessionFactory);
        }
        
        //事务管理
        @Override
		@Bean
        public PlatformTransactionManager annotationDrivenTransactionManager() {
            return new DataSourceTransactionManager(dataSource);
        }

        //将要执行的sql进行日志打印(不想拦截，就把这方法注释掉)
//        @Bean
//        public SqlPrintInterceptor sqlPrintInterceptor(){
//        	return new SqlPrintInterceptor();
//        }
}
