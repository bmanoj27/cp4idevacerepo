package com.example;

import java.io.InputStream;

import com.ibm.integration.test.v1.TestMessageAssembly;
import com.ibm.integration.test.v1.exception.TestException;

public class TestHelper {

	public static TestMessageAssembly loadTestMessageFromMessageAssembly(String messageAssemblyPath) throws TestException {
		TestMessageAssembly testMessageAssembly = new TestMessageAssembly();
		try {
			InputStream messageStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(messageAssemblyPath);
			if (messageStream == null) {
				throw new TestException("Unable to locate message assembly file: " + messageAssemblyPath);
			}
			testMessageAssembly.buildFromRecordedMessageAssembly(messageStream);
		} catch (Exception ex) {
			throw new TestException("Failed to load input message", ex);
		}
		return testMessageAssembly;
	}
}
