package com.example.Taskmaster;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // This tells Spring to load application-test.properties
class TaskmasterApplicationTests {

	@Test
	void contextLoads() {
	}

}
