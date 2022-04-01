package project.society.web.timeblocks.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Service;
import project.society.data.dao.GenericReactiveDAO;
import project.society.data.dao.ReactiveDAOService;
import project.society.data.dto.HasId;
import project.society.utility.ObjectToByteArrayAndBack;
import project.society.web.timeblocks.model.dto.TimeBlockDay;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.DayOfWeek;

@Service
public class TimeBlockDayDAOService extends ReactiveDAOService<TimeBlockDay, String> {
    public static final String TABLE_NAME = "time_block_days";
    Class<TimeBlockDayDTO> dtoClass = TimeBlockDayDTO.class;

    public TimeBlockDayDAOService(GenericReactiveDAO genericReactiveDAO) {
        super(genericReactiveDAO);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return super.genericReactiveDAO.existsById(id, dtoClass);
    }

    @Override
    public Mono<TimeBlockDay> findOneById(String s) {
        return super.genericReactiveDAO.findOneById(s, dtoClass).map(TimeBlockDayDTO::getAsTimeBlockDay);
    }

    @Override
    public Flux<TimeBlockDay> findById(String s) {
        return super.genericReactiveDAO.findById(s, dtoClass).map(TimeBlockDayDTO::getAsTimeBlockDay);
    }

    @Override
    public Mono<TimeBlockDay> save(TimeBlockDay item) {
        return super.genericReactiveDAO.save(new TimeBlockDayDTO(item), dtoClass)
                .map(TimeBlockDayDTO::getAsTimeBlockDay);
    }

    @Override
    public Mono<Integer> deleteById(String s) {
        return super.genericReactiveDAO.deleteById(s, dtoClass);
    }

    @Override
    public Flux<TimeBlockDay> idLike(String idToMatch) {
        return super.genericReactiveDAO.idLike(idToMatch, dtoClass)
                .map(TimeBlockDayDTO::getAsTimeBlockDay);
    }

    @Table(TABLE_NAME)
    private static class TimeBlockDayDTO implements HasId<String> {
        @Id @Column("uuid") String id;
        @Column("user_id") String userId;
        @Column("day_of_week") Integer dayOfWeek;
        @Column("time_block_list") byte[] timeBlockList; // Serialized.

        public TimeBlockDayDTO(TimeBlockDay timeBlockDay) {
            this.id = timeBlockDay.getId();
            this.userId = timeBlockDay.getUserId();
            this.dayOfWeek = timeBlockDay.getDayOfWeek().getValue();
            try {
                this.timeBlockList = ObjectToByteArrayAndBack.objectToByteArray(timeBlockDay.getTimeBlocks());
            } catch (IOException e) {
                throw new RuntimeException("Failed to serialize time blocks");
            }
        }

        public TimeBlockDay getAsTimeBlockDay() {
            try {
                return new TimeBlockDay(this.getId(), DayOfWeek.of(this.getDayOfWeek()),
                        ObjectToByteArrayAndBack.byteArrayToObject(this.timeBlockList));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Failed to deserialize time block list.");
            }
        }

        @Override
        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek.getValue();
        }

        public byte[] getTimeBlockList() {
            return timeBlockList;
        }

        public void setTimeBlockList(byte[] timeBlockList) {
            this.timeBlockList = timeBlockList;
        }
    }
}
