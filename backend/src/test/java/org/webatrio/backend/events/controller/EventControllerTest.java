package org.webatrio.backend.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.services.eventservice.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = EventController.class)
@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventDTO eventDTO;

    @BeforeEach
    void setup() {

        eventDTO = EventDTO.builder()
                .title("rama")
                .description("nous somme la pour le faire")
                .status(true)
                .organiserName("joel")
                .numberOfParticipants(27)
                .place("paris")
                .startEventDate(LocalDateTime.of(LocalDate.of(1995, 2, 2), LocalTime.of(11, 11)))
                .endEventDate(LocalDateTime.of(LocalDate.of(2000, 2, 2), LocalTime.of(11, 11)))
                .build();
    }

//    @Test
//    void createEvent() throws Exception {
//        //given
//        Mockito.when(eventService.addEvent(eventDTO)).thenReturn(eventDTO);
//
//        //when
//        ResultActions response = mockMvc.perform(post("/api/event/addEvent")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(eventDTO)));
//
//        //then
//        response.andExpect(MockMvcResultMatchers.status().isCreated());
//
//    }
//
//    @Test
//    void updateEvent() {
//    }
//
//    @Test
//    void cancelEvent() {
//    }
//
//    @Test
//    void getAllEvent() {
//    }
}