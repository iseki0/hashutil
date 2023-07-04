package space.iseki.hashutil

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.HexFormat
import kotlin.test.assertContentEquals

class MD5Test {
    private val hs = "000102030405060708090a0b0c0d0e0f"
    private val hh = HexFormat.of().parseHex(hs)
    private val h = MD5(hs)

    @Test
    fun test(){
        check(MD5(hh) == h)
    }
    @Test
    fun bytes() {
        assertContentEquals(hh,h.bytes())
    }

    @Test
    fun testToString() {
        assertEquals(hs, h.toString())
    }

    @Test
    fun testEquals() {
        assertTrue(h == MD5(hs))
    }

    @Test
    fun testHashCode() {
        assertTrue(h.hashCode() == MD5(hs).hashCode())
    }
}