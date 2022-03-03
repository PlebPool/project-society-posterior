package project.society.data.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class DTO<ID> {
    @Id @Column("_id")
    protected final ID id;

    public DTO(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }
}
