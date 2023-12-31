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
    }

    @Test
    fun test1(){
        val ref = Path.of("../gradle/wrapper/gradle-wrapper.jar").MD5()
        println(ref)
        assertEquals(MD5("bd2800c24d911ce05e46f6a283bf713b"), ref)
    }

    @Test
    fun test2(){
        val shA256 = "aa".encodeToByteArray().SHA256()
        assertEquals(SHA256("961b6dd3ede3cb8ecbaacbd68de040cd78eb2ed5889130cceb4c49268ea4d506"), shA256)
        println(shA256)
    }
}