package com.demoCommunity.Community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
/**
 * 为了让该类可以使用Spring容器，需要实现ApplicationContextAware
 */
public class CommunityApplicationTests implements ApplicationContextAware {
    /*
    为了让ApplicationContext有真实的this
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    public void testApplicationContext(){
		//System.out.println(applicationContext);
//		AlphaDao alphaDao=applicationContext.getBean("Mybatis",AlphaDao.class);
//		System.out.println(alphaDao.select());
//		AlphaService alphaService=applicationContext.getBean(AlphaService.class);
//		alphaService.init();
//		alphaService.destroy();
	}

//	@Test
//	public void testBeanManagement(){
//    	AlphaService alphaService=applicationContext.getBean(AlphaService.class);
//		System.out.println(alphaService);
//	}
}
