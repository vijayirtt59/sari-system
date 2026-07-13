package com.sari.system.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessRole {


    COORDINADOR_SARI(
            "Coordinador del SARI"
    ),

    RESPONSABLE_VENTAS_COMPRAS(
            "Responsable de Ventas y Compras"
    ),

    AGENTES_VENTAS(
            "Agentes de Ventas"
    ),

    COORDINADOR_SISTEMA_SARI(
            "Coordinador de Sistema SARI"
    ),

    DIRECCION_GENERAL(
            "Dirección General"
    ),

    VENTAS("Ventas"),
    COMPRAS("Compras"),

    LOGISTICA(
            "Logística"
    ),
    RESPONSIBLE_DE_SISTEMAS("Responsable de Sistemas"),
    REPRESENTANTE_LEGAL("Representante legal (responsable de Seguridad e higiene)"),

    PRODUCCION(
            "Producción"
    );

    private final String label;


}
