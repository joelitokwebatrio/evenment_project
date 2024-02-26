package com.webatrio.atrio.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webatrio.atrio.events.dto.EventDTO;
import com.webatrio.atrio.events.services.eventservice.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@WebMvcTest(controllers = EventController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
//@ContextConfiguration(classes = SecurityConfiguration.class)
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

    @Test
    void createEvent() throws Exception {
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

    }

    @Test
    void updateEvent() {
    }

    @Test
    void cancelEvent() {
    }

    @Test
    void getAllEvent() {
    }
}