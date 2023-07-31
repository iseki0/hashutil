package space.iseki.hashutil

import kotlin.test.Test
import kotlin.test.assertEquals

class MD5Test {

    @Test
    fun test0(){
        val text = "010203040506070809101a1b1c1d1e1f"
        val md5 = MD5(text)
        assertEquals(md5, MD5(md5.bytes()))
        assertEquals(text, md5.toString())
    }
}