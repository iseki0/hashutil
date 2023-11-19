@file:JvmName("-${typename}")

package space.iseki.hashutil

import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Path

fun Path.${typename}(): ${typename} = ${typename}.of(this)

fun InputStream.${typename}(): ${typename} = ${typename}.of(this)

fun ByteArray.${typename}(): ${typename} = ${typename}.of(this)

fun String.${typename}(): ${typename} = ${typename}(Charsets.UTF_8)

fun String.${typename}(charset: Charset): ${typename} = ${typename}.of(toByteArray(charset))
