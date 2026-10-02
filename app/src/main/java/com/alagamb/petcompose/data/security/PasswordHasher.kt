package com.alagamb.petcompose.data.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Turns passwords into salted one-way hashes (PBKDF2-HMAC-SHA256), so the database never holds
 * a readable password.
 *
 * Stored format: `pbkdf2_sha256$<iterations>$<salt>$<hash>` (salt and hash in Base64). The
 * iteration count travels with each hash, so it can be raised later without breaking old ones.
 *
 * Hashing is slow on purpose: call it off the main thread.
 */
object PasswordHasher {

    private const val PREFIX = "pbkdf2_sha256"
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val ITERATIONS = 120_000
    private const val SALT_BYTES = 16
    private const val HASH_BITS = 256

    fun hash(password: String): String {
        val salt = ByteArray(SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val hash = pbkdf2(password, salt, ITERATIONS)
        return listOf(PREFIX, ITERATIONS, salt.encode(), hash.encode()).joinToString("$")
    }

    fun verify(password: String, stored: String): Boolean {
        val parts = stored.split('$')
        if (parts.size != 4 || parts[0] != PREFIX) return false
        val iterations = parts[1].toIntOrNull() ?: return false
        return try {
            val expected = parts[3].decode()
            val actual = pbkdf2(password, parts[2].decode(), iterations)
            MessageDigest.isEqual(expected, actual)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    /** `false` for passwords saved as plain text by older versions of the app. */
    fun isHashed(stored: String): Boolean = stored.startsWith("$PREFIX$")

    private fun pbkdf2(password: String, salt: ByteArray, iterations: Int): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, HASH_BITS)
        return try {
            SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private fun ByteArray.encode(): String = Base64.getEncoder().encodeToString(this)

    private fun String.decode(): ByteArray = Base64.getDecoder().decode(this)
}
