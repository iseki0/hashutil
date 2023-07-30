package space.iseki.hashutil

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class MD5Serializer : KSerializer<MD5> {
    override val descriptor: SerialDescriptor
        get() = serialDescriptor<String>()

    override fun deserialize(decoder: Decoder): MD5 = decoder.decodeString().let(::MD5)

    override fun serialize(encoder: Encoder, value: MD5) {
        encoder.encodeString(value.toString())
    }

}