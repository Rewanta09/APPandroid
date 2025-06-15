package com.example.mygragment

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.junit.Assert.*

class DashboardApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var dashboardApi: DashboardApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dashboardApi = retrofit.create(DashboardApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test successful dashboard data retrieval`() = runBlocking {
        // Arrange
        val mockResponse = """
            {
                "entities": [
                    {
                        "species": "African Elephant",
                        "scientificName": "Loxodonta africana",
                        "habitat": "Savanna",
                        "diet": "Herbivore",
                        "conservationStatus": "Vulnerable",
                        "averageLifespan": 60,
                        "description": "The largest land animal, known for its intelligence, social behavior, and distinctive trunk."
                    },
                    {
                        "species": "Komodo Dragon",
                        "scientificName": "Varanus komodoensis",
                        "habitat": "Tropical savanna",
                        "diet": "Carnivore",
                        "conservationStatus": "Vulnerable",
                        "averageLifespan": 30,
                        "description": "The largest living species of lizard, found in the Indonesian islands."
                    },
                    {
                        "species": "Emperor Penguin",
                        "scientificName": "Aptenodytes forsteri",
                        "habitat": "Antarctic coast",
                        "diet": "Carnivore",
                        "conservationStatus": "Near Threatened",
                        "averageLifespan": 20,
                        "description": "The tallest and heaviest of all living penguin species, known for its extreme adaptations to its Antarctic habitat."
                    }
                ],
                "entityTotal": 3
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json")
        )

        // Act
        val keypass = "animals"
        val response = dashboardApi.getEntities(keypass)

        // Assert
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())

        val dashboardData = response.body()!!

        // Print actual count and data for debugging
        println("Actual data count: ${dashboardData.entities.size}")
        println("Data: ${dashboardData.entities}")

        // Match entityTotal and list size with mock JSON (3 entities)
        assertEquals(3, dashboardData.entityTotal)
        assertEquals(3, dashboardData.entities.size)

        // Validate first entity
        val firstEntity = dashboardData.entities[0]
        assertNotNull(firstEntity)
        assertEquals("African Elephant", firstEntity.species)
        assertEquals("Loxodonta africana", firstEntity.scientificName)
        assertEquals("Savanna", firstEntity.habitat)
        assertEquals("Herbivore", firstEntity.diet)
        assertEquals("Vulnerable", firstEntity.conservationStatus)
        assertEquals(60, firstEntity.averageLifespan)
        assertNotNull(firstEntity.description)

        // Validate second entity
        val secondEntity = dashboardData.entities[1]
        assertEquals("Komodo Dragon", secondEntity.species)
        assertEquals("Vulnerable", secondEntity.conservationStatus)

        // Validate third entity
        val thirdEntity = dashboardData.entities[2]
        assertEquals("Emperor Penguin", thirdEntity.species)
        assertEquals("Near Threatened", thirdEntity.conservationStatus)

        // Verify the request
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertTrue(recordedRequest.path!!.contains("/dashboard/animals"))
        assertTrue(recordedRequest.path!!.contains("keypass=$keypass"))
    }

    @Test
    fun `test dashboard with invalid keypass returns unauthorized`() = runBlocking {
        // Arrange
        val errorResponse = """
            {
                "error": "Invalid keypass"
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody(errorResponse)
                .addHeader("Content-Type", "application/json")
        )

        // Act
        val invalidKeypass = "invalid_keypass"
        val response = dashboardApi.getEntities(invalidKeypass)

        // Assert
        assertEquals(401, response.code())
        assertFalse(response.isSuccessful)
        assertNotNull(response.errorBody())

        // Verify the request
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertTrue(recordedRequest.path!!.contains("keypass=$invalidKeypass"))
    }

    @Test
    fun `test dashboard with empty entities list`() = runBlocking {
        // Arrange
        val emptyResponse = """
            {
                "entities": [],
                "entityTotal": 0
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(emptyResponse)
                .addHeader("Content-Type", "application/json")
        )

        // Act
        val keypass = "valid_keypass"
        val response = dashboardApi.getEntities(keypass)

        // Assert
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())

        val dashboardData = response.body()!!
        assertEquals(0, dashboardData.entityTotal)
        assertTrue(dashboardData.entities.isEmpty())

        // Verify the request
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertTrue(recordedRequest.path!!.contains("/dashboard/animals"))
    }

    @Test
    fun `test dashboard server error handling`() = runBlocking {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        // Act
        val keypass = "test_keypass"
        val response = dashboardApi.getEntities(keypass)

        // Assert
        assertEquals(500, response.code())
        assertFalse(response.isSuccessful)
        assertNotNull(response.errorBody())

        // Verify the request was still made
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
    }
}
