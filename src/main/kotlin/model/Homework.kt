package model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URLDecoder
import kotlin.text.Charsets.UTF_8

/**
 * 作业项
 */
@Serializable
data class Homework(
    @Serializable(UrlSerializer::class)
    @SerialName("url")
    val url: String,
    @SerialName("sort")
    val sort: String,
    @SerialName("semester")
    val semester: Int,
    @SerialName("workStatus")
    val workStatus: String,
    @SerialName("publishDateTime")
    val publishDateTime: String
) {
    private class UrlSerializer : KSerializer<String> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor(toString(), PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: String): Unit =
            encoder.encodeString(value)

        override fun deserialize(decoder: Decoder): String =
            URLDecoder.decode(decoder.decodeString().substringAfter("&url="), UTF_8)
    }
}