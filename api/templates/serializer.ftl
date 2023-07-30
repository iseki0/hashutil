package space.iseki.hashutil

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class ${typename}Serializer : KSerializer<${typename}> {
    override val descriptor: SerialDescriptor
        get() = serialDescriptor<String>()

    override fun deserialize(decoder: Decoder): ${typename} = decoder.decodeString().let(::${typename})

    override fun serialize(encoder: Encoder, value: ${typename}) {
        encoder.encodeString(value.toString())
    }

}