package net.cpollet.jixture.spring;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author Christophe Pollet
 */
public class TestConfiguration {
	private final static Logger logger = LoggerFactory.getLogger(TestConfiguration.class);

	@Test
	public void springConfigrationIsWorking() {
		GenericApplicationContext applicationContext = new GenericApplicationContext(new ClassPathXmlApplicationContext("classpath:/spring/mockedTransactionManagerContext.xml"));


		for (String beanName : applicationContext.getBeanDefinitionNames()) {
			if (!applicationContext.getBeanDefinition(beanName).isAbstract()) {
				logger.info("Requesting bean {}", beanName);
				applicationContext.getBean(beanName);
			}
		}
	}
}
