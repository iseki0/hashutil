@file:JvmName("-${typename}")

package space.iseki.hashutil

import java.io.File
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.inputStream

@JvmOverloads
fun File.${typename}(bufferSize: Int = Util.DEFAULT_BUFFER_SIZE): ${typename} =
    inputStream().use { Util.hashStream(MD5.getMessageDigest(), it, bufferSize) }.let(::${typename})

@JvmOverloads
fun Path.${typename}(bufferSize: Int = Util.DEFAULT_BUFFER_SIZE, vararg openOption: StandardOpenOption): ${typename} =
    inputStream(*openOption).use { Util.hashStream(MD5.getMessageDigest(), it, bufferSize) }.let(::${typename})

@JvmOverloads
fun InputStream.${typename}(bufferSize: Int = Util.DEFAULT_BUFFER_SIZE): ${typename} =
    Util.hashStream(${typename}.getMessageDigest(), this, bufferSize).let(::${typename})
