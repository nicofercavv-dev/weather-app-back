package com.academia.db.climatempo;

import com.academia.db.climatempo.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
class ClimatempoApplicationTests extends AbstractIntegrationTest {
	@Test
	void contextLoads() {
        assertTrue(true);
	}

    @Test
    @DisplayName("Deve simular o método main para fins de cobertura")
    void testMain() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(ClimatempoApplication.class, new String[]{}))
                    .thenReturn(null);

            ClimatempoApplication.main(new String[]{});

            mockedSpringApplication.verify(() -> SpringApplication.run(ClimatempoApplication.class, new String[]{}));
        }
    }

}
