package tripSB.model;

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
public class TripRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TripRepository tripRepository;

    @Before
    public void deleteAllBeforeTests() throws Exception {
        tripRepository.deleteAll();
    }

    @Test
    public void shouldCreateTrip() throws Exception {

        mockMvc.perform(post("/trips").content(
                "{\"destination\": \"Plock\", \"abroad\":\"false\"}"))
                .andExpect(status().isCreated()).andExpect(
                header().string("Location", containsString("trips/")));

    }

    @Test
    public void shouldPartiallyUpdateTrip() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/trips").content(
                "{\"destination\": \"Plock\", \"abroad\":\"false\"}"))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(
                patch(location).content("{\"destination\": \"Bydgoszcz\"}")).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.destination").value("Bydgoszcz")).andExpect(
                jsonPath("$.abroad").value("false"));
    }

    @Test
    public void shouldDeleteTrip() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/trips").content(
                "{\"destination\": \"Plock\", \"abroad\":\"false\"}"))
                .andExpect(status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }
}