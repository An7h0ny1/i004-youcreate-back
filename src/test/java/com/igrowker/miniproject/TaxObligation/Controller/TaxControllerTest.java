package com.igrowker.miniproject.TaxObligation.Controller;

import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Service.TaxNotificationService;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.match.ContentRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(TaxController.class)
@WithMockUser
public class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaxService taxService;

    //@MockBean
    //private TaxNotificationService taxNotificationService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTaxesForUser() throws Exception {
        Long userId = 1L;
        List<TaxDTO> mockTaxes = List.of(new TaxDTO("IVA", 3000, 13, LocalDate.of(2024,12,04), "Pending", "pay", 400, 0));

        when(taxService.getTaxesForUser(userId)).thenReturn(mockTaxes);

        mockMvc.perform(get("/api/v1/taxes/taxes/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].amount").value(3000))
                .andExpect(jsonPath("$[0].percentage").value(13))
                .andExpect(jsonPath("$[0].LocalDate").value(LocalDate.of(2024,12,04)))
                .andExpect(jsonPath("$[0].status").value("Pending"))
                .andExpect(jsonPath("$[0].action").value("pay"))
                .andExpect(jsonPath("$[0].TaxDue").value(400))
                .andExpect(jsonPath("$[0].amountPaid").value(0));

    }

}
