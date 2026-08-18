package com.chatflow.apigateway.APIGatewayChatFlow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import java.security.KeyPairGenerator;

@SpringBootTest
class ApiGatewayChatFlowApplicationTests {

	@Test
	void contextLoads() {
	}
    @Test
    void loadsValidX509PublicKey() throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
    }

}
