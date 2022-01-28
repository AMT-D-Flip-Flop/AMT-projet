package com.amt.dflipflop.Controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amazonaws.services.s3.AmazonS3;
import com.amt.dflipflop.AmazonConfig;
import com.amt.dflipflop.Services.CategoryService;
import com.amt.dflipflop.Services.ProductSelectionService;
import com.amt.dflipflop.Services.ProductService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StoreController.class)
public class StoreControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AmazonS3 amazonS3;

    @Test
    public void shouldDisplayStore() throws Exception {
        this.mockMvc.perform(get("/store")).andExpect(status().isOk());
    }

    @Test
    public void shouldDisplayProduct() throws Exception {
        this.mockMvc.perform(get("/store/product/1")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldDisplayAddProduct() throws Exception {
        this.mockMvc.perform(get("/store/add-product")).andDo(print()).andExpect(status().isForbidden());
    }
}