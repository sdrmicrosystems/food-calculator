package com.fc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.exception.PriceIsNotGreaterThanZeroException;
import com.fc.model.Product;
import com.fc.model.ProductPriceChangeDTO;
import com.fc.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.fc.utils.ProductTestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldRejectAccessingEndPointsWhenUserIsAnonymous() throws Exception {
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldReturnOkResponseWhenCallGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(createDummyProduct("apple", "apple", new BigDecimal(10))));

        mockMvc.perform(
                get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("apple")));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldReturnOkResponseWhenCallGetOneProduct() throws Exception {
        when(productService.getOneProduct(1L)).thenReturn(createDummyProduct("apple", "apple", new BigDecimal(10)));

        mockMvc.perform(
                get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is("apple")));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldForbiddenRequestForPostEndPointForUserRole() throws Exception {
        Product product = createDummyProduct("apple", "apple", new BigDecimal(10));
        when(productService.createNewProduct(product)).thenReturn(product);

        String json = objectMapper.writeValueAsString(product);

        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnOkResponseWhenCreateTheProduct() throws Exception {
        Product product = createDummyProduct("apple", "apple", new BigDecimal(10));
        when(productService.createNewProduct(product)).thenReturn(product);

        String json = objectMapper.writeValueAsString(product);

        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is("apple")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnOkResponseWhenDeleteTheProduct() throws Exception {
        mockMvc.perform(
                delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldRejectDeleteRequestForUser() throws Exception {
        mockMvc.perform(
                delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldRejectPriceChangeRequestForUser() throws Exception {
        ProductPriceChangeDTO dto = new ProductPriceChangeDTO(1L, BigDecimal.TEN);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                post("/products/priceChange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnProductWhenCallPriceChangeRequestForAdmin() throws Exception {

        ProductPriceChangeDTO dto = new ProductPriceChangeDTO(1L, BigDecimal.TEN);
        Product product = createDummyProduct("apple", "apple desc", new BigDecimal(10));

        when(productService.changePrice(1L, BigDecimal.TEN)).thenReturn(product);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                post("/products/priceChange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is("apple")))
                .andExpect(jsonPath("description", is("apple desc")))
                .andExpect(jsonPath("price", is(10)))
        ;
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldRejectPriceChangeRequestWhenPriceIsZero() throws Exception {
        ProductPriceChangeDTO dto = new ProductPriceChangeDTO(1L, BigDecimal.ZERO);

        when(productService.changePrice(1L, BigDecimal.ZERO)).thenThrow(PriceIsNotGreaterThanZeroException.class);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                post("/products/priceChange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnProductWhenCallUpdateEndPoint() throws Exception {

        Product product = createDummyProduct("apple", "apple desc", new BigDecimal(100));
        Product updatedProduct = createDummyProduct("apple", "apple desc", new BigDecimal(100));

        when(productService.updateProduct(product, 1L)).thenReturn(updatedProduct);

        String json = objectMapper.writeValueAsString(product);

        mockMvc.perform(
                put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is("apple")))
                .andExpect(jsonPath("description", is("apple desc")))
                .andExpect(jsonPath("price", is(100)))
        ;
    }

}
