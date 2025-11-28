package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * This file implements a Custom KSerializer for the BookWorkDto class. Its primary purpose is to
 * handle inconsistent data types coming from the API.
 * The API sometimes returns the "description" field as a simple String, and other times as a
 * complex JSON Object (e.g., {"value": "some description"}).
 * This custom serializer normalizes both cases into a single String property.
 */
object BookWorkDtoSerializer: KSerializer<BookWorkDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(BookWorkDto::class.simpleName!!) {
            // The buildClassSerialDescriptor defines the structure of the JSON this serializer
            // expects to handle
            element<String?>("description") // Define the field we are interested in
        }

    // The serialize function controls how the object is converted back into JSON
    override fun serialize(
        encoder: Encoder,
        value: BookWorkDto
    ) = encoder.encodeStructure(descriptor) {
        // Takes the description property from the BookWorkDto and writes it as a string.
        value.description?.let {
            encodeStringElement(descriptor, 0, it)
        }
    }

    override fun deserialize(decoder: Decoder): BookWorkDto = decoder.decodeStructure(descriptor){
        // The decodeStructure allows to start reading the JSON object
        var description: String? = null

        // Loops through the fields in the JSON
        while(true){
            when(val index = decodeElementIndex(descriptor)){
                // When index is 0 it means the parser found the "description" field
                0 -> {
                    val jsonDecoder = decoder as? JsonDecoder ?:
                        throw SerializationException("Can be applied to JSON only")
                    // The decodeJsonElement reads the field as a generic JsonElement. This allows
                    // us to check the type of the element and handle it accordingly.
                    val element = jsonDecoder.decodeJsonElement()
                    description = if(element is JsonObject){
                        // If the API sent descripction as a JSON object, use decodeFromJsonElement
                        // to parse it into a temporary DescriptionDto object and then extract the
                        // value from it.
                        decoder.json.decodeFromJsonElement<DescriptionDto>(
                            element = element,
                            deserializer = DescriptionDto.serializer()
                        ).value
                    } else if (element is JsonPrimitive && element.isString ){
                        // If the API sent description as a string, just use the content property
                        // of the JsonPrimitive
                        element.content
                    } else {
                        // If it's neither a JSON object nor a string, set the field to null
                        null
                    }
                }
                CompositeDecoder.DECODE_DONE -> break
                else -> throw SerializationException("Unexpected index $index")
            }
        }
        return@decodeStructure BookWorkDto(description)
    }

}