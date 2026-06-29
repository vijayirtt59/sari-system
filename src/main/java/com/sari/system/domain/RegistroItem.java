package com.sari.system.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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

}
