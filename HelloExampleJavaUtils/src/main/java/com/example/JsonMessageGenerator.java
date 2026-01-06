package com.example;

import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;

public class JsonMessageGenerator {

    public MbElement generateJsonMessageWithSingleValue(MbElement root, String attributeName, String attributeValue) throws MbException {
        return root.createElementAsLastChild(MbJSON.PARSER_NAME)
                .createElementAsLastChild(MbElement.TYPE_NAME, MbJSON.DATA_ELEMENT_NAME, null)
                .createElementAsLastChild(MbElement.TYPE_NAME_VALUE, attributeName, attributeValue);
    }
}
