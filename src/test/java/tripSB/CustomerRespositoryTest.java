package tripSB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerRespositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRespository customerRespository;

    @Before
    public void deleteAllBeforeTests() throws Exception {
        customerRespository.deleteAll();
    }

    @Test
    public void shouldCreateCustomer() throws Exception {

        mockMvc.perform(post("/customers").content(
                "{\"firstName\": \"Maciej\", \"lastName\":\"Zakoscielny\", \"city\":\"Gdansk\"," +
                        " \"zipCode\":\"88-123\", \"phoneNumber\":\"78787878\", \"foreigner\":\"false\"}"))
                .andExpect(status().isCreated()).andExpect(
                header().string("Location", containsString("customers/")));

    }

    @Test
    public void shouldPartiallyUpdateCustomer() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/customers").content(
                "{\"firstName\": \"Maciej\", \"lastName\":\"Zakoscielny\", \"city\":\"Gdansk\"," +
                        " \"zipCode\":\"88-123\", \"phoneNumber\":\"78787878\", \"foreigner\":\"false\"}"))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(
                patch(location).content("{\"firstName\": \"Janek\"}")).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.firstName").value("Janek")).andExpect(
                jsonPath("$.lastName").value("Zakoscielny"));
    }

    @Test
    public void shouldDeleteCustomer() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/customers").content(
                "{\"firstName\": \"Maciej\", \"lastName\":\"Zakoscielny\", \"city\":\"Gdansk\"," +
                        " \"zipCode\":\"88-123\", \"phoneNumber\":\"78787878\", \"foreigner\":\"false\"}"))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }
}