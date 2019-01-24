package com.payline.payment.docapost.Integration;

import com.payline.pmapi.integration.FullTest;
import com.payline.pmapi.integration.exception.TestException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

/**
 * Add JVM option -Deditable.java.test.console=true to intellij to be able to enter text while debugging.
 */
public class FullTestIt {

    @TestFactory
    public Collection<DynamicTest> fullTest() throws TestException {
        return FullTest.getInstance().fullTest();
    }
}
