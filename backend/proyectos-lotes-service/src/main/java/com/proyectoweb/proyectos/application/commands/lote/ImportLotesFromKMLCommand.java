package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record ImportLotesFromKMLCommand(
        UUID proyectoId,
        MultipartFile kmlFile,
        String manzanaDefault,
        java.math.BigDecimal precioDefault
) implements Command<List<LoteDto>> {
}
