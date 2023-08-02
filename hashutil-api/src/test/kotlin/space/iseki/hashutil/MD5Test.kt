package space.iseki.hashutil

import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MD5Test {

    @Test
    fun test0(){
        val text = "010203040506070809101a1b1c1d1e1f"
        val md5 = MD5(text)
        assertEquals(md5, MD5(md5.bytes()))
        assertEquals(text, md5.toString())
        assertNotNull(MD5.getMessageDigest())
    }

    @Test
    fun test1(){
        println(Path.of("../gradle/wrapper/gradle-wrapper.jar").MD5())
    }

    @Test
    fun test2(){
        val shA256 = "aa".encodeToByteArray().SHA256()
        println(shA256)
    }
}