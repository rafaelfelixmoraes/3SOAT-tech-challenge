package br.com.tech.challenge;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@ActiveProfiles("test")
class TechChallengeApplicationTests {

	@AfterAll
	static void clearDatabase(@Autowired Flyway flyway) {
		flyway.clean();
		flyway.migrate();
	}

	@Test
	void contextLoads() {
	}

}
