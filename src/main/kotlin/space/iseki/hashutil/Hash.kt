package space.iseki.hashutil

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.HexFormat
import java.io.File
import java.io.InputStream
import java.lang.invoke.MethodHandles
import java.nio.ByteOrder
import java.nio.file.OpenOption
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.inputStream

private val INT_ARR_HANDLE = MethodHandles.byteArrayViewVarHandle(IntArray::class.java, ByteOrder.BIG_ENDIAN)!!

private fun putIntOffset(bytes: ByteArray, off: Int, value: Int){
    INT_ARR_HANDLE.set(bytes, off, value)
}

private fun getIntOffset(bytes: ByteArray, off: Int) = INT_ARR_HANDLE.get(bytes, off) as Int

private val hex = HexFormat.of()

private fun MessageDigest.hashStream(inputStream: InputStream): ByteArray {
    val buffer = ByteArray(4 * 1024)
    var read: Int
    while (inputStream.read(buffer).also { read = it } > -1) {
        update(buffer, 0, read)
    }
    return digest()
}

private const val MD5_LENGTH = 16

@Suppress("SameParameterValue", "unused", "DuplicatedCode", "MemberVisibilityCanBePrivate")
@Serializable(MD5.Serializer::class)
class MD5 private constructor(
    private val i0: Int,
    private val i1: Int,
    private val i2: Int,
    private val i3: Int,
) {
    constructor(input: ByteArray) : this(
        input,
        0
    )

    internal constructor(input: ByteArray, off: Int) : this(
        getIntOffset(input, off),
        getIntOffset(input, off + 1 * 4),
        getIntOffset(input, off + 2 * 4),
        getIntOffset(input, off + 3 * 4),
    )

    constructor(input: String) : this(hex.parseHex(input))

    private fun copyInto(arr: ByteArray, off: Int) {
        putIntOffset(arr, off, i0)
        putIntOffset(arr, off + 1 * 4, i1)
        putIntOffset(arr, off + 2 * 4, i2)
        putIntOffset(arr, off + 3 * 4, i3)
    }

    fun bytes(): ByteArray = ByteArray(MD5_LENGTH).also { copyInto(it, 0) }

    override fun toString(): String = hex.formatHex(bytes())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MD5

        if (i0 != other.i0) return false
        if (i1 != other.i1) return false
        if (i2 != other.i2) return false
        return i3 == other.i3
    }

    override fun hashCode(): Int {
        var result = i0
        result = 31 * result + i1
        result = 31 * result + i2
        result = 31 * result + i3
        return result
    }

    object Serializer : KSerializer<MD5> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder) = decoder.decodeString().let(::MD5)

        override fun serialize(encoder: Encoder, value: MD5) {
            encoder.encodeString(value.toString())
        }

    }
}

private fun getMD5() = MessageDigest.getInstance("MD5")!!

fun InputStream.MD5(): MD5 = getMD5().hashStream(this).let(::MD5)

fun File.MD5() = inputStream().use { it.MD5() }

fun Path.MD5(vararg openOption: OpenOption) = inputStream(*openOption).use { it.MD5() }

private const val SHA1_LENGTH = 20

@Suppress("SameParameterValue", "unused", "DuplicatedCode", "MemberVisibilityCanBePrivate")
@Serializable(SHA1.Serializer::class)
class SHA1 private constructor(
    private val i0: Int,
    private val i1: Int,
    private val i2: Int,
    private val i3: Int,
    private val i4: Int,
) {
    constructor(input: ByteArray) : this(
        input,
        0
    )

    internal constructor(input: ByteArray, off: Int) : this(
        getIntOffset(input, off),
        getIntOffset(input, off + 1 * 4),
        getIntOffset(input, off + 2 * 4),
        getIntOffset(input, off + 3 * 4),
        getIntOffset(input, off + 4 * 4),
    )

    constructor(input: String) : this(hex.parseHex(input))

    private fun copyInto(arr: ByteArray, off: Int) {
        putIntOffset(arr, off, i0)
        putIntOffset(arr, off + 1 * 4, i1)
        putIntOffset(arr, off + 2 * 4, i2)
        putIntOffset(arr, off + 3 * 4, i3)
        putIntOffset(arr, off + 4 * 4, i4)
    }

    fun bytes(): ByteArray = ByteArray(SHA1_LENGTH).also { copyInto(it, 0) }

    override fun toString(): String = hex.formatHex(bytes())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SHA1

        if (i0 != other.i0) return false
        if (i1 != other.i1) return false
        if (i2 != other.i2) return false
        if (i3 != other.i3) return false
        return i4 == other.i4
    }

    override fun hashCode(): Int {
        var result = i0
        result = 31 * result + i1
        result = 31 * result + i2
        result = 31 * result + i3
        result = 31 * result + i4
        return result
    }

    object Serializer : KSerializer<SHA1> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder) = decoder.decodeString().let(::SHA1)

        override fun serialize(encoder: Encoder, value: SHA1) {
            encoder.encodeString(value.toString())
        }

    }
}

private fun getSHA1() = MessageDigest.getInstance("SHA-1")!!

fun InputStream.SHA1(): SHA1 = getSHA1().hashStream(this).let(::SHA1)

fun File.SHA1() = inputStream().use { it.SHA1() }

fun Path.SHA1(vararg openOption: OpenOption) = inputStream(*openOption).use { it.SHA1() }

private const val SHA224_LENGTH = 28

@Suppress("SameParameterValue", "unused", "DuplicatedCode", "MemberVisibilityCanBePrivate")
@Serializable(SHA224.Serializer::class)
class SHA224 private constructor(
    private val i0: Int,
    private val i1: Int,
    private val i2: Int,
    private val i3: Int,
    private val i4: Int,
    private val i5: Int,
    private val i6: Int,
) {
    constructor(input: ByteArray) : this(
        input,
        0
    )

    internal constructor(input: ByteArray, off: Int) : this(
        getIntOffset(input, off),
        getIntOffset(input, off + 1 * 4),
        getIntOffset(input, off + 2 * 4),
        getIntOffset(input, off + 3 * 4),
        getIntOffset(input, off + 4 * 4),
        getIntOffset(input, off + 5 * 4),
        getIntOffset(input, off + 6 * 4),
    )

    constructor(input: String) : this(hex.parseHex(input))

    private fun copyInto(arr: ByteArray, off: Int) {
        putIntOffset(arr, off, i0)
        putIntOffset(arr, off + 1 * 4, i1)
        putIntOffset(arr, off + 2 * 4, i2)
        putIntOffset(arr, off + 3 * 4, i3)
        putIntOffset(arr, off + 4 * 4, i4)
        putIntOffset(arr, off + 5 * 4, i5)
        putIntOffset(arr, off + 6 * 4, i6)
    }

    fun bytes(): ByteArray = ByteArray(SHA224_LENGTH).also { copyInto(it, 0) }

    override fun toString(): String = hex.formatHex(bytes())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SHA224

        if (i0 != other.i0) return false
        if (i1 != other.i1) return false
        if (i2 != other.i2) return false
        if (i3 != other.i3) return false
        if (i4 != other.i4) return false
        if (i5 != other.i5) return false
        return i6 == other.i6
    }

    override fun hashCode(): Int {
        var result = i0
        result = 31 * result + i1
        result = 31 * result + i2
        result = 31 * result + i3
        result = 31 * result + i4
        result = 31 * result + i5
        result = 31 * result + i6
        return result
    }

    object Serializer : KSerializer<SHA224> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder) = decoder.decodeString().let(::SHA224)

        override fun serialize(encoder: Encoder, value: SHA224) {
            encoder.encodeString(value.toString())
        }

    }
}

private fun getSHA224() = MessageDigest.getInstance("SHA-224")!!

fun InputStream.SHA224(): SHA224 = getSHA224().hashStream(this).let(::SHA224)

fun File.SHA224() = inputStream().use { it.SHA224() }

fun Path.SHA224(vararg openOption: OpenOption) = inputStream(*openOption).use { it.SHA224() }

private const val SHA256_LENGTH = 32

@Suppress("SameParameterValue", "unused", "DuplicatedCode", "MemberVisibilityCanBePrivate")
@Serializable(SHA256.Serializer::class)
class SHA256 private constructor(
    private val i0: Int,
    private val i1: Int,
    private val i2: Int,
    private val i3: Int,
    private val i4: Int,
    private val i5: Int,
    private val i6: Int,
    private val i7: Int,
) {
    constructor(input: ByteArray) : this(
        input,
        0
    )

    internal constructor(input: ByteArray, off: Int) : this(
        getIntOffset(input, off),
        getIntOffset(input, off + 1 * 4),
        getIntOffset(input, off + 2 * 4),
        getIntOffset(input, off + 3 * 4),
        getIntOffset(input, off + 4 * 4),
        getIntOffset(input, off + 5 * 4),
        getIntOffset(input, off + 6 * 4),
        getIntOffset(input, off + 7 * 4),
    )

    constructor(input: String) : this(hex.parseHex(input))

    private fun copyInto(arr: ByteArray, off: Int) {
        putIntOffset(arr, off, i0)
        putIntOffset(arr, off + 1 * 4, i1)
        putIntOffset(arr, off + 2 * 4, i2)
        putIntOffset(arr, off + 3 * 4, i3)
        putIntOffset(arr, off + 4 * 4, i4)
        putIntOffset(arr, off + 5 * 4, i5)
        putIntOffset(arr, off + 6 * 4, i6)
        putIntOffset(arr, off + 7 * 4, i7)
    }

    fun bytes(): ByteArray = ByteArray(SHA256_LENGTH).also { copyInto(it, 0) }

    override fun toString(): String = hex.formatHex(bytes())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SHA256

        if (i0 != other.i0) return false
        if (i1 != other.i1) return false
        if (i2 != other.i2) return false
        if (i3 != other.i3) return false
        if (i4 != other.i4) return false
        if (i5 != other.i5) return false
        if (i6 != other.i6) return false
        return i7 == other.i7
    }

    override fun hashCode(): Int {
        var result = i0
        result = 31 * result + i1
        result = 31 * result + i2
        result = 31 * result + i3
        result = 31 * result + i4
        result = 31 * result + i5
        result = 31 * result + i6
        result = 31 * result + i7
        return result
    }

    object Serializer : KSerializer<SHA256> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder) = decoder.decodeString().let(::SHA256)

        override fun serialize(encoder: Encoder, value: SHA256) {
            encoder.encodeString(value.toString())
        }

    }
}

private fun getSHA256() = MessageDigest.getInstance("SHA-256")!!

fun InputStream.SHA256(): SHA256 = getSHA256().hashStream(this).let(::SHA256)

fun File.SHA256() = inputStream().use { it.SHA256() }

fun Path.SHA256(vararg openOption: OpenOption) = inputStream(*openOption).use { it.SHA256() }

private const val SHA384_LENGTH = 48

@Suppress("SameParameterValue", "unused", "DuplicatedCode", "MemberVisibilityCanBePrivate")
@Serializable(SHA384.Serializer::class)
class SHA384 private constructor(
    private val i0: Int,
    private val i1: Int,
    private val i2: Int,
    private val i3: Int,
    private val i4: Int,
    private val i5: Int,
    private val i6: Int,
    private val i7: Int,
    private val i8: Int,
    private val i9: Int,
    private val i10: Int,
    private val i11: Int,
) {
    constructor(input: ByteArray) : this(
        input,
        0
    )

    internal constructor(input: ByteArray, off: Int) : this(
        getIntOffset(input, off),
        getIntOffset(input, off + 1 * 4),
        getIntOffset(input, off + 2 * 4),
        getIntOffset(input, off + 3 * 4),
        getIntOffset(input, off + 4 * 4),
        getIntOffset(input, off + 5 * 4),
        getIntOffset(input, off + 6 * 4),
        getIntOffset(input, off + 7 * 4),
        getIntOffset(input, off + 8 * 4),
        getIntOffset(input, off + 9 * 4),
        getIntOffset(input, off + 10 * 4),
        getIntOffset(input, off + 11 * 4),
    )

    constructor(input: String) : this(hex.parseHex(input))

    private fun copyInto(arr: ByteArray, off: Int) {
        putIntOffset(arr, off, i0)
        putIntOffset(arr, off + 1 * 4, i1)
        putIntOffset(arr, off + 2 * 4, i2)
        putIntOffset(arr, off + 3 * 4, i3)
        putIntOffset(arr, off + 4 * 4, i4)
        putIntOffset(arr, off + 5 * 4, i5)
        putIntOffset(arr, off + 6 * 4, i6)
        putIntOffset(arr, off + 7 * 4, i7)
        putIntOffset(arr, off + 8 * 4, i8)
        putIntOffset(arr, off + 9 * 4, i9)
        putIntOffset(arr, off + 10 * 4, i10)
        putIntOffset(arr, off + 11 * 4, i11)
    }

    fun bytes(): ByteArray = ByteArray(SHA384_LENGTH).also { copyInto(it, 0) }

    override fun toString(): String = hex.formatHex(bytes())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SHA384

        if (i0 != other.i0) return false
        if (i1 != other.i1) return false
        if (i2 != other.i2) return false
        if (i3 != other.i3) return false
        if (i4 != other.i4) return false
        if (i5 != other.i5) return false
        if (i6 != other.i6) return false
        if (i7 != other.i7) return false
        if (i8 != other.i8) return false
        if (i9 != other.i9) return false
        if (i10 != other.i10) return false
        return i11 == other.i11
    }

    override fun hashCode(): Int {
        var result = i0
        result = 31 * result + i1
        result = 31 * result + i2
        result = 31 * result + i3
        result = 31 * result + i4
        result = 31 * result + i5
        result = 31 * result + i6
        result = 31 * result + i7
        result = 31 * result + i8
        result = 31 * result + i9
        result = 31 * result + i10
        result = 31 * result + i11
        return result
    }

    object Serializer : KSerializer<SHA384> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder) = decoder.decodeString().let(::SHA384)

        override fun serialize(encoder: Encoder, value: SHA384) {
            encoder.encodeString(value.toString())
        }

    }
}

private fun getSHA384() = MessageDigest.getInstance("SHA-384")!!

fun InputStream.SHA384(): SHA384 = getSHA384().hashStream(this).let(::SHA384)

fun File.SHA384() = inputStream().use { it.SHA384() }

fun Path.SHA384(vararg openOption: OpenOption) = inputStream(*openOption).use { it.SHA384() }

private const val SHA512_LENGTH = 64

@Suppress("SameParameterValue", "unused", "DuplicatedCode", "MemberVisibilityCanBePrivate")
@Serializable(SHA512.Serializer::class)
class SHA512 private constructor(
    private val i0: Int,
    private val i1: Int,
    private val i2: Int,
    private val i3: Int,
    private val i4: Int,
    private val i5: Int,
    private val i6: Int,
    private val i7: Int,
    private val i8: Int,
    private val i9: Int,
    private val i10: Int,
    private val i11: Int,
    private val i12: Int,
    private val i13: Int,
    private val i14: Int,
    private val i15: Int,
) {
    constructor(input: ByteArray) : this(
        input,
        0
    )

    internal constructor(input: ByteArray, off: Int) : this(
        getIntOffset(input, off),
        getIntOffset(input, off + 1 * 4),
        getIntOffset(input, off + 2 * 4),
        getIntOffset(input, off + 3 * 4),
        getIntOffset(input, off + 4 * 4),
        getIntOffset(input, off + 5 * 4),
        getIntOffset(input, off + 6 * 4),
        getIntOffset(input, off + 7 * 4),
        getIntOffset(input, off + 8 * 4),
        getIntOffset(input, off + 9 * 4),
        getIntOffset(input, off + 10 * 4),
        getIntOffset(input, off + 11 * 4),
        getIntOffset(input, off + 12 * 4),
        getIntOffset(input, off + 13 * 4),
        getIntOffset(input, off + 14 * 4),
        getIntOffset(input, off + 15 * 4),
    )

    constructor(input: String) : this(hex.parseHex(input))

    private fun copyInto(arr: ByteArray, off: Int) {
        putIntOffset(arr, off, i0)
        putIntOffset(arr, off + 1 * 4, i1)
        putIntOffset(arr, off + 2 * 4, i2)
        putIntOffset(arr, off + 3 * 4, i3)
        putIntOffset(arr, off + 4 * 4, i4)
        putIntOffset(arr, off + 5 * 4, i5)
        putIntOffset(arr, off + 6 * 4, i6)
        putIntOffset(arr, off + 7 * 4, i7)
        putIntOffset(arr, off + 8 * 4, i8)
        putIntOffset(arr, off + 9 * 4, i9)
        putIntOffset(arr, off + 10 * 4, i10)
        putIntOffset(arr, off + 11 * 4, i11)
        putIntOffset(arr, off + 12 * 4, i12)
        putIntOffset(arr, off + 13 * 4, i13)
        putIntOffset(arr, off + 14 * 4, i14)
        putIntOffset(arr, off + 15 * 4, i15)
    }

    fun bytes(): ByteArray = ByteArray(SHA512_LENGTH).also { copyInto(it, 0) }

    override fun toString(): String = hex.formatHex(bytes())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SHA512

        if (i0 != other.i0) return false
        if (i1 != other.i1) return false
        if (i2 != other.i2) return false
        if (i3 != other.i3) return false
        if (i4 != other.i4) return false
        if (i5 != other.i5) return false
        if (i6 != other.i6) return false
        if (i7 != other.i7) return false
        if (i8 != other.i8) return false
        if (i9 != other.i9) return false
        if (i10 != other.i10) return false
        if (i11 != other.i11) return false
        if (i12 != other.i12) return false
        if (i13 != other.i13) return false
        if (i14 != other.i14) return false
        return i15 == other.i15
    }

    override fun hashCode(): Int {
        var result = i0
        result = 31 * result + i1
        result = 31 * result + i2
        result = 31 * result + i3
        result = 31 * result + i4
        result = 31 * result + i5
        result = 31 * result + i6
        result = 31 * result + i7
        result = 31 * result + i8
        result = 31 * result + i9
        result = 31 * result + i10
        result = 31 * result + i11
        result = 31 * result + i12
        result = 31 * result + i13
        result = 31 * result + i14
        result = 31 * result + i15
        return result
    }

    object Serializer : KSerializer<SHA512> {
        override val descriptor: SerialDescriptor
            get() = serialDescriptor<String>()

        override fun deserialize(decoder: Decoder) = decoder.decodeString().let(::SHA512)

        override fun serialize(encoder: Encoder, value: SHA512) {
            encoder.encodeString(value.toString())
        }

    }
}

private fun getSHA512() = MessageDigest.getInstance("SHA-512")!!

fun InputStream.SHA512(): SHA512 = getSHA512().hashStream(this).let(::SHA512)

fun File.SHA512() = inputStream().use { it.SHA512() }

fun Path.SHA512(vararg openOption: OpenOption) = inputStream(*openOption).use { it.SHA512() }
