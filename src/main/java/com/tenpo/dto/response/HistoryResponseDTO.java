package com.tenpo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryResponseDTO {

    private Integer id_record;

    private String endpoint;

    private String user;

    private Date date;

    private String https;

    private String message;

}
