package com.amt.dflipflop.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldDisplayUserOrders() throws Exception {
        this.mockMvc.perform(get("/user/orders")).andExpect(status().isOk());
    }

    @Test
    public void shouldDisplayUserAddresses() throws Exception {
        this.mockMvc.perform(get("/user/addresses")).andExpect(status().isOk());
    }

    @Test
    public void shouldDisplayUserAddAddress() throws Exception {
        this.mockMvc.perform(get("/user/add-address")).andExpect(status().isOk());
    }

}