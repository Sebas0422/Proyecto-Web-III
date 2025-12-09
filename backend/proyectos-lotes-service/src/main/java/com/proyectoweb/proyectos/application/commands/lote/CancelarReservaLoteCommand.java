package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;

import java.util.UUID;

public record CancelarReservaLoteCommand(UUID loteId) implements Command<Void> {
}
