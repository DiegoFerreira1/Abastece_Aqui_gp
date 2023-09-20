package com.example.abasteceaqui;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.example.abasteceaqui.tools.FieldValidator;

import org.junit.Test;

public class ExampleUnitTest {
    @Test
    public void field_validation() {


        // Valid passwords
        assertTrue(FieldValidator.validate("Password123@", FieldValidator.TYPE_PASSWORD));
        assertTrue(FieldValidator.validate("pass@123Aaa", FieldValidator.TYPE_PASSWORD));
        assertTrue(FieldValidator.validate("0Aa#1234", FieldValidator.TYPE_PASSWORD));
        assertTrue(FieldValidator.validate("Senha123*", FieldValidator.TYPE_PASSWORD));
        assertTrue(FieldValidator.validate("##$Enha0", FieldValidator.TYPE_PASSWORD));

        // Invalid passwords
        assertFalse(FieldValidator.validate("password123@", FieldValidator.TYPE_PASSWORD));
        assertFalse(FieldValidator.validate("pass", FieldValidator.TYPE_PASSWORD));
        assertFalse(FieldValidator.validate("senha123", FieldValidator.TYPE_PASSWORD));
        assertFalse(FieldValidator.validate("Senha123", FieldValidator.TYPE_PASSWORD));
        assertFalse(FieldValidator.validate("Senha@@@", FieldValidator.TYPE_PASSWORD));
        assertFalse(FieldValidator.validate("senha@@@", FieldValidator.TYPE_PASSWORD));

    }
}