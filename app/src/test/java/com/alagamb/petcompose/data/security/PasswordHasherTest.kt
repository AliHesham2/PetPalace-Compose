package com.alagamb.petcompose.data.security

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun hash_doesNotContainThePassword() {
        val hash = PasswordHasher.hash("Secret#123")
        assertFalse(hash.contains("Secret#123"))
        assertTrue(PasswordHasher.isHashed(hash))
    }

    @Test
    fun verify_acceptsTheRightPasswordOnly() {
        val hash = PasswordHasher.hash("Secret#123")
        assertTrue(PasswordHasher.verify("Secret#123", hash))
        assertFalse(PasswordHasher.verify("secret#123", hash))
        assertFalse(PasswordHasher.verify("", hash))
    }

    @Test
    fun hash_usesADifferentSaltEveryTime() {
        assertNotEquals(PasswordHasher.hash("Secret#123"), PasswordHasher.hash("Secret#123"))
    }

    @Test
    fun verify_rejectsPlainTextAndMalformedValues() {
        assertFalse(PasswordHasher.isHashed("Secret#123"))
        assertFalse(PasswordHasher.verify("Secret#123", "Secret#123"))
        assertFalse(PasswordHasher.verify("x", "pbkdf2_sha256\$abc\$salt\$hash"))
        assertFalse(PasswordHasher.verify("x", "pbkdf2_sha256\$1000\$!!!\$???"))
    }

    @Test
    fun verify_supportsUnicodePasswords() {
        val hash = PasswordHasher.hash("كلمة-سر-١٢٣")
        assertTrue(PasswordHasher.verify("كلمة-سر-١٢٣", hash))
    }
}
