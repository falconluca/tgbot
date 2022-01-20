package me.shaohsiung.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EudbResponse extends BaseModel {
    private String message;
}
