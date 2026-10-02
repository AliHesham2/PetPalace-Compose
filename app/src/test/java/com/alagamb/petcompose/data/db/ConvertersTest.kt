package com.alagamb.petcompose.data.db

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun list_roundTrips() {
        val json = converters.listToJson(listOf(1, 2, 3))
        assertEquals("[1,2,3]", json)
        assertEquals(listOf(1, 2, 3), converters.jsonToList(json!!))
    }

    @Test
    fun emptyAndNullLists() {
        assertEquals("[]", converters.listToJson(emptyList()))
        assertEquals(emptyList<Int>(), converters.jsonToList("[]"))
        assertNull(converters.listToJson(null))
    }

    @Test
    fun readsValuesWrittenByGson() {
        assertEquals(listOf(4, -5, 6), converters.jsonToList("[4, -5, 6]"))
        assertEquals(emptyList<Int>(), converters.jsonToList("null"))
    }
}
