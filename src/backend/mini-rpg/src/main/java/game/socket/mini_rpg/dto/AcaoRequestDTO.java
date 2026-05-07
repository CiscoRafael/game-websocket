package game.socket.mini_rpg.dto;

import game.socket.mini_rpg.model.TipoAcao;

public record AcaoRequestDTO(
    String jogador, 
    TipoAcao acao
) {}