
private val cl = object {}::class.java.classLoader!!
val template = cl.getResourceAsStream("template")!!.reader().use { it.readText() }
val pre = cl.getResourceAsStream("pre")!!.reader().use { it.readText() }

fun main() {
    println(pre)
    println(a("MD5", 16, "MD5"))
    println(a("SHA1", 20, "SHA-1"))
    println(a("SHA224", 28, "SHA-224"))
    println(a("SHA256", 32, "SHA-256"))
    println(a("SHA384", 48, "SHA-384"))
    println(a("SHA512", 64, "SHA-512"))
}

fun a(name: String, len: Int, alg: String): String {
    val ilen = len / 4
    val lenConst = "${name}_LENGTH"
    val decl = (0 until ilen).joinToString("\n") { "private val i$it: Int," }.prependIndent(" ".repeat(4))
    val constructor1 =
        (1 until ilen).joinToString("\n") { "getIntOffset(input, off + $it * 4)," }.prependIndent(" ".repeat(8))
    val copyInto =
        (1 until ilen).joinToString("\n") { "putIntOffset(arr, off + $it * 4, i$it)" }.prependIndent(" ".repeat(8))
    return template.replace("%decl%", decl)
        .replace("%name%", name)
        .replace("%lenConst%", lenConst)
        .replace("%constructor1%", constructor1)
        .replace("%copyInto%", copyInto)
        .replace("%len%", len.toString())
        .replace("%alg%", alg)
}

