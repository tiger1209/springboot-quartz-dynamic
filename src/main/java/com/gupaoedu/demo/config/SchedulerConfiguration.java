package com.gupaoedu.demo.config;

import com.uusafe.framework.database.pools.DbPoolDruid;
import com.uusafe.framework.database.pools.IDbPool;
import com.uusafe.platform.utils.exception.EmptyThreadParamException;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Author wangcunlei
 * @Date 2019-11-19 13:41
 * @Description
 */
@Configuration
public class SchedulerConfiguration {

    @Autowired
    private JobFactory jobFactory;

    @Bean(name="SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean()
        throws IOException, EmptyThreadParamException {
        IDbPool dbPool = DbPoolDruid.getInstance("/uusafe/platform/mysql");
        //获取配置属性
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        //创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(propertiesFactoryBean.getObject());
        //使用数据源

        factory.setDataSource(dbPool.getDsPool("mbs"));

        factory.setJobFactory(jobFactory);
        return factory;
    }

    @Bean(name="scheduler")
    public Scheduler scheduler() throws IOException, EmptyThreadParamException {
        return schedulerFactoryBean().getScheduler();
    }

}
