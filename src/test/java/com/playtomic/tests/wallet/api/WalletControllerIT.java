package com.playtomic.tests.wallet.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class WalletControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WalletRepository walletRepository;

    @Before
    public void setUp() {
        walletRepository.save(new Wallet(1L, BigDecimal.valueOf(100)));
    }

    @Test
    public void getById_200() throws Exception {
        mvc.perform(get("/wallets/{id}", "1"))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    public void charge_200() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "1")
              .content("{\"amount\" : \"10\", \"type\" : \"CHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().isOk());
    }

    @Test
    public void charge_biggerThanBalance_400() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "1")
              .content("{\"amount\" : \"111\", \"type\" : \"CHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().isBadRequest());
    }

    @Test
    public void charge_negativeAmount_400() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "1")
              .content("{\"amount\" : \"-1\", \"type\" : \"CHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().isBadRequest());
    }

    @Test
    public void charge_notExistingWalletId_404() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "2")
              .content("{\"amount\" : \"10\", \"type\" : \"CHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().isNotFound());
    }

    @Test
    public void recharge_200() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "1")
              .content("{\"amount\" : \"10\", \"type\" : \"CHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().isOk());
    }

    @Test
    public void recharge_negativeAmount_400() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "1")
              .content("{\"amount\" : \"-1\", \"type\" : \"CHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().isBadRequest());
    }

    @Test
    public void recharge_notExistingWalletId_404() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "2")
              .content("{\"amount\" : \"10\", \"type\" : \"CHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().isNotFound());
    }

    @Test
    public void recharge_smallerThreshold_500() throws Exception {
        mvc.perform(post("/wallets/{id}/movements", "1")
              .content("{\"amount\" : \"9\", \"type\" : \"RECHARGE\"}")
              .contentType("application/json"))
           .andDo(print())
           .andExpect(status().is5xxServerError());
    }
}
