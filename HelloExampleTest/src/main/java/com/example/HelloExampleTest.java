package com.example;

import static com.ibm.integration.test.v1.Matchers.nodeCallCountIs;
import static com.ibm.integration.test.v1.Matchers.terminalPropagateCountIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.InputStream;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ibm.integration.test.v1.Matchers;
import com.ibm.integration.test.v1.NodeSpy;
import com.ibm.integration.test.v1.SpyObjectReference;
import com.ibm.integration.test.v1.TestMessageAssembly;
import com.ibm.integration.test.v1.TestSetup;
import com.ibm.integration.test.v1.exception.TestException;

class HelloExampleTest {

	private static final String APPLICATION_NAME = "HelloExample";
	private static final String MESSAGE_FLOW = "main"; 
	private static final String IMPLEMENTATION_SUBFLOW = "implementation";
	
	@AfterEach
	public void cleanupTest() throws TestException {
		// Ensure any mocks created by a test are cleared after the test runs 
		TestSetup.restoreAllMocks();
	}

	@Test
	void givenValidInput_whenCallImplementationFlow_thenSuccess() throws Exception {
		SpyObjectReference nodeReference = new SpyObjectReference()
				.application(APPLICATION_NAME)
				.messageFlow(MESSAGE_FLOW)
				.node(IMPLEMENTATION_SUBFLOW);
		NodeSpy nodeSpy = new NodeSpy(nodeReference);
		
		TestMessageAssembly expectedMessageAssembly = TestHelper.loadTestMessageFromMessageAssembly("/successfullResponse.mxml");
		String expectedMessage = expectedMessageAssembly.getJSONMessageBodyAsString();
		
		TestMessageAssembly inputMessageAssembly = new TestMessageAssembly();
		inputMessageAssembly.localEnvironmentPath("HTTP.Input.QueryString.name").setValue("TestUser");
		
		nodeSpy.evaluate(inputMessageAssembly, false, "Input");
		
 		assertThat(nodeSpy, nodeCallCountIs(1));
 		assertThat(nodeSpy, terminalPropagateCountIs("Output", 1));
		
 		TestMessageAssembly resultAssembly = nodeSpy.propagatedMessageAssembly("Output", 1);
 		String resultMessage = resultAssembly.getMessageBodyAsString();
		
 		
		assertThat(resultMessage, is(expectedMessage));
	}
	
	@Test
	void givenInvalidInput_whenCallImplementationFlow_thenSuccess() throws Exception {
		SpyObjectReference nodeReference = new SpyObjectReference()
				.application(APPLICATION_NAME)
				.messageFlow(MESSAGE_FLOW)
				.node(IMPLEMENTATION_SUBFLOW);
		NodeSpy nodeSpy = new NodeSpy(nodeReference);
		
		String testUsername = "Dummy";
		
		String expectedMessage = generateUnsuccessfullJsonResponse(testUsername);
		
		TestMessageAssembly inputMessageAssembly = new TestMessageAssembly();
		inputMessageAssembly.localEnvironmentPath("HTTP.Input.QueryString.name").setValue(testUsername);
		
		nodeSpy.evaluate(inputMessageAssembly, false, "Input");
		
 		assertThat(nodeSpy, nodeCallCountIs(1));
 		assertThat(nodeSpy, terminalPropagateCountIs("Output", 1));
		
 		TestMessageAssembly resultAssembly = nodeSpy.propagatedMessageAssembly("Output", 1);
 		String resultMessage = resultAssembly.getMessageBodyAsString();
		
		assertThat(resultMessage, is(expectedMessage));
	}
	
	private String generateUnsuccessfullJsonResponse(String username) {
		return "{\"Message\":\"Invalid user: " + username + "!\"}";
	}
}
