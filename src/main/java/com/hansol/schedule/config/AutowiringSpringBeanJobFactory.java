package com.hansol.schedule.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

//org.quartz.Job 을 상속(implements)받은 클래스가 Autowired를 정상적으로 할 수 있도록 도와주는 객체
public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
	private transient AutowireCapableBeanFactory beanFactory;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		beanFactory = applicationContext.getAutowireCapableBeanFactory();
	}

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		final Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job);
		return job;
	}

}
