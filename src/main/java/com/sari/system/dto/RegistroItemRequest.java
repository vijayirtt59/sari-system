package com.sari.system.dto;

import com.sari.system.domain.BusinessRole;
import lombok.Data;

@Data
public class RegistroItemRequest {

    private String codigo;

    private String nombre;

    private String almacenamiento;

    private String tiempoRetencion;

    private BusinessRole responsableResguardo;

}
