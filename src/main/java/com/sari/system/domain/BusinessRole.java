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
    COLABORADOR_ENVIA_CORREO("Colaborador que envía el correo"),
    RESPONSABLE_ADMINISTRACION("Responsable de Administración"),
    AREA_GENERADO("El área que lo haya generado"),
    RESPONSABLE_LOGISTICA("Responsable de Logística"),
    RESPONSABLE_SEGURIDAD_HIGIENE("Responsable de seguridad e higiene (vocero)"),
    RESPONSABLE_RH("Responsable de RH"),
    RESPONSABLE_SEGURIDAD("Responsable de seguridad"),

    PRODUCCION(
            "Producción"
    );

    private final String label;


}
