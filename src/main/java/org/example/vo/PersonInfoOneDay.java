package org.example.vo;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PersonInfoOneDay {
    String date;
    List<PersonInfo> labels=new ArrayList<>();
}
