package com.attornatus.attornatus.resources;

import com.attornatus.attornatus.dto.AddressDTO;
import com.attornatus.attornatus.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AddressResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingIdAdmin;
    private Long existingIdClient;
    private Long nonExistingId;

    private String adminUsername;
    private String adminPassword;
    private String clientUsername;
    private String clientPassword;

    @BeforeEach
    void setUp() throws Exception {
        existingIdAdmin = 1L;
        existingIdClient = 2L;
        nonExistingId = 1000L;
        adminUsername = "Bob Brow";
        adminPassword = "123456";
        clientUsername = "Maria Green";
        clientPassword = "123456";
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldReturnUnathorizedWhenNoTokenGiven() throws Exception {

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdClient)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldForbiddenWhenClientAuthorized() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(false);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdAdmin)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldNotFoundWhenAdminAuthorized() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(false);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", nonExistingId)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldReturnTwoAddressExceptionWhenAdminAuthorized() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(true);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdAdmin)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotAcceptable());
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldInsertAddressCurrentUserWhenClientAuthorized() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(false);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.publicPlace").value("Rua Senador Pompeu"));
        result.andExpect(jsonPath("$.cep").value("60025-000"));
        result.andExpect(jsonPath("$.number").value(454));
        result.andExpect(jsonPath("$.city").value("Fortaleza"));
        result.andExpect(jsonPath("$.mainAddress").value(false));
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldUnproccessableEntityWhenAdminAuthorizedInvalidCity() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("     ");
        addressDTO.setMainAddress(false);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldUnproccessableEntityWhenAdminAuthorizedInvalidNumber() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(null);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(false);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldUnproccessableEntityWhenAdminAuthorizedInvalidCep() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-0001");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(false);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertAddressOfPersonWithAdminProfileOrCurrentUserShouldInsertAdrressWhenAdminAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(false);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(post("/addresses/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.publicPlace").value("Rua Senador Pompeu"));
        result.andExpect(jsonPath("$.cep").value("60025-000"));
        result.andExpect(jsonPath("$.number").value(454));
        result.andExpect(jsonPath("$.city").value("Fortaleza"));
    }

    @Test
    public void informMainAddressShouldReturnUnathorizedWhenNoTokenGiven() throws Exception {

        ResultActions result = mockMvc.perform(put("/addresses/{id}/mainaddress", existingIdClient)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void informMainAddressShouldReturnForbiddenWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(true);
        addressDTO.setUserId(1L);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(put("/addresses/{id}/mainaddress", 1)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void informMainAddressShouldReturnNotAcceptableWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(true);
        addressDTO.setUserId(2L);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(put("/addresses/{id}/mainaddress", 1)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotAcceptable());
    }

    @Test
    public void informMainAddressShouldInformMainAddressCurrentUserWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(true);
        addressDTO.setUserId(2L);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(put("/addresses/{id}/mainaddress", 4)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.publicPlace").value("Rua Senador Pompeu"));
        result.andExpect(jsonPath("$.cep").value("60025-000"));
        result.andExpect(jsonPath("$.number").value(454));
        result.andExpect(jsonPath("$.city").value("Fortaleza"));
        result.andExpect(jsonPath("$.mainAddress").value(true));
    }

    @Test
    public void informMainAddressShouldReturnNotAcceptableWhenAdminAuthenticatedInvalidMainAddress() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(true);
        addressDTO.setUserId(1L);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(put("/addresses/{id}/mainaddress", 1)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotAcceptable());
    }

    @Test
    public void informMainAddressShouldReturnUnproccessableEntityWhenAdminAuthenticatedInvalidCep() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-0001");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(true);
        addressDTO.setUserId(2L);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(put("/addresses/{id}/mainaddress", 4)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void informMainAddressShouldReturnInformMainAddressWhenAdminAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPublicPlace("Rua Senador Pompeu");
        addressDTO.setCep("60025-000");
        addressDTO.setNumber(454);
        addressDTO.setCity("Fortaleza");
        addressDTO.setMainAddress(true);
        addressDTO.setUserId(2L);

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        ResultActions result = mockMvc.perform(put("/addresses/{id}/mainaddress", 4)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.publicPlace").value("Rua Senador Pompeu"));
        result.andExpect(jsonPath("$.cep").value("60025-000"));
        result.andExpect(jsonPath("$.number").value(454));
        result.andExpect(jsonPath("$.city").value("Fortaleza"));
        result.andExpect(jsonPath("$.mainAddress").value(true));
    }
}
