package org.webatrio.eventsapp.services.participantservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webatrio.eventsapp.dao.ParticipantRepository;
import org.webatrio.eventsapp.dto.EventDTO;
import org.webatrio.eventsapp.dto.ParticipantDTO;
import org.webatrio.eventsapp.exceptions.ParticipantAlreadyExistException;
import org.webatrio.eventsapp.mappers.EventsMapper;
import org.webatrio.eventsapp.mappers.ParticipantMapper;
import org.webatrio.eventsapp.models.Participant;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.webatrio.eventsapp.utils.Utils.PARTICIPANT_ALREADY_EXIST_IN_DATABASE;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    private final EventsMapper eventsMapper;

    /**
     * Methode utilise pour ajouter un nouveau participant
     * @param participantDTO
     * @return
     * @throws ParticipantAlreadyExistException
     */
    @Override
    public ParticipantDTO addParticipant(ParticipantDTO participantDTO) throws ParticipantAlreadyExistException {
        if (Objects.nonNull(participantDTO) && Objects.nonNull(participantDTO.getEmail())) {
            Optional<Participant> participant = participantRepository.findParticipantByEmail(participantDTO.getEmail());
            if (participant.isPresent()) {
                throw new ParticipantAlreadyExistException(PARTICIPANT_ALREADY_EXIST_IN_DATABASE);
            }
        }

        Participant participant = participantRepository.save(participantMapper.mapParticipantDTOToParticipant(participantDTO));
        return participantMapper.mapParticipantToParticipantDTO(participant);
    }

    /**
     * Methode utilise pour lister l'ensembles des evenement auquelle  au quelles est enregistrer un participant.
     * @param idParticipant
     * @return
     */
    @Override
    public List<EventDTO> getParticipantEvent(long idParticipant) {
        return participantRepository.
                findAll()
                .stream()
                .filter(participant -> participant.getId().equals(idParticipant))
                .map(Participant::getEvents)
                .flatMap(Collection::stream)
                .map(eventsMapper::mapEventToEventDTO)
                .toList();
    }
}
