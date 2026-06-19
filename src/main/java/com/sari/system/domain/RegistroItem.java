package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroItem {


    @Id
    @GeneratedValue
    private Long id;

    private String codigo;

    private String nombre;

    private String almacenamiento;

    private String tiempoRetencion;

    @Enumerated(EnumType.STRING)
    private BusinessRole responsableResguardo;

    @ManyToOne
    private Pro pro;

}
