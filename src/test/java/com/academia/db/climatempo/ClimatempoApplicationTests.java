package com.academia.db.climatempo;

import com.academia.db.climatempo.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ClimatempoApplicationTests extends AbstractIntegrationTest {
	@Test
	void contextLoads() {
	}

}
