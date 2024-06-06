package lepdv.todolistweb.integration.controller;

import lepdv.todolistweb.integration.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
class StartControllerIT /*extends IntegrationTestBase*/ {

    private final MockMvc mockMvc;


    @Test
    void startPage_shouldStatus200AndGetStartPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/start-page"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("start_page"));
    }

}