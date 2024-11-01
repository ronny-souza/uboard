package br.com.uboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class UboardApplicationTests extends TestContainersDatabaseConfiguration {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	@DisplayName("Testing application context loads")
	void testContextLoads() {
		Assertions.assertNotNull(this.applicationContext);
	}
}
