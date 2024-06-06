package lepdv.todolistweb.integration.controller;

import lepdv.todolistweb.Constants;
import lepdv.todolistweb.integration.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static lepdv.todolistweb.dto.RegisterDto.Fields.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
class AuthControllerIT /*extends IntegrationTestBase*/ {

    private final MockMvc mockMvc;


    @Test
    void loginPage_shouldStatus200AngGetLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authenticate/login"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.view().name("auth/login")
                );
    }

    @Test
    void registrationPage_shouldStatus200AndGetRegistrationPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authenticate/registration"))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("user"),
                        MockMvcResultMatchers.view().name("auth/registration")
                );
    }

    @Test
    void register_shouldStatusRedirect_whenDataIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/authenticate/registration")
                        .file(new MockMultipartFile("image", "lock.png", "multipart/form-data", Constants.BYTE_ARRAY))
                        .param(username, Constants.REGISTER_DTO.getUsername())
                        .param(dateOfBirth, Constants.REGISTER_DTO.getDateOfBirth().toString())
                        .param(password, Constants.REGISTER_DTO.getPassword())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/logout")
                );
    }

    @Test
    void register_shouldRedirectRegistrationPage_whenDataIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/authenticate/registration")
                        .file(new MockMultipartFile("image", "lock.png", "multipart/form-data", Constants.BYTE_ARRAY))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param(dateOfBirth, Constants.REGISTER_DTO.getDateOfBirth().toString())
                        .param(password, Constants.REGISTER_DTO.getPassword())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().is2xxSuccessful(),
                        MockMvcResultMatchers.model().attributeExists("user"),
                        MockMvcResultMatchers.view().name("auth/registration")
                );
    }
}