package space.iseki.hashutil

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object ${typename}Serializer : KSerializer<${typename}> {
    override val descriptor: SerialDescriptor
        get() = serialDescriptor<String>()

    override fun deserialize(decoder: Decoder): ${typename} {
        val s = decoder.decodeString()
        if (s.length != ${typename}.SIZE_IN_BYTES * 2) {
            throw kotlinx.serialization.SerializationException("Expected a " + ${typename}.SIZE_IN_BYTES * 2 + "-character hexadecimal ${typename} hash, but got " + s.length + " characters.")
        }
        return ${typename}(s)
    }

    override fun serialize(encoder: Encoder, value: ${typename}) {
        encoder.encodeString(value.toString())
    }

}