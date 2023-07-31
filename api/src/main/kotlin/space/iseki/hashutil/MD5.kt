@file:JvmName("-MD5")

package space.iseki.hashutil

import java.io.File
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.inputStream

@JvmOverloads
fun File.MD5(bufferSize: Int = Util.DEFAULT_BUFFER_SIZE): MD5 =
    inputStream().use { Util.hashStream(MD5.getMessageDigest(), it, bufferSize) }.let(::MD5)

@JvmOverloads
fun Path.MD5(bufferSize: Int = Util.DEFAULT_BUFFER_SIZE, vararg openOption: StandardOpenOption): MD5 =
    inputStream(*openOption).use { Util.hashStream(MD5.getMessageDigest(), it, bufferSize) }.let(::MD5)

@JvmOverloads
fun InputStream.MD5(bufferSize: Int = Util.DEFAULT_BUFFER_SIZE): MD5 =
    Util.hashStream(MD5.getMessageDigest(), this, bufferSize).let(::MD5)
