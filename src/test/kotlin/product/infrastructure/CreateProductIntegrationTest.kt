package product.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import product.infrastructure.controller.dto.request.CreateProductRequest

@SpringBootTest
@AutoConfigureMockMvc
class CreateProductIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should create a product successfully`() {
        val request = CreateProductRequest("Test Product", 100.0, 50)
        val requestPayload = ObjectMapper().writeValueAsString(request)

        val result = mockMvc.perform(
            post("/api/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload)
        )
            .andExpect(status().isCreated)
            .andReturn()

        val responseContent = result.response.contentAsString
        val responseJson = ObjectMapper().readTree(responseContent)

        assertEquals("Test Product", responseJson["name"].asText())
        assertEquals(100.0, responseJson["price"].asDouble())
        assertEquals(50, responseJson["stockQuantity"].asInt())
    }
}
