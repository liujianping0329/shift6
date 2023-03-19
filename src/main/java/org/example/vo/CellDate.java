package org.example.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CellDate {
    Date date;
    String dateStr;

    Integer yobi;
}
