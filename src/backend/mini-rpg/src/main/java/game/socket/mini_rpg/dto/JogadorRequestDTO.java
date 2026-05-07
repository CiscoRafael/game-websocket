package game.socket.mini_rpg.dto;

import game.socket.mini_rpg.model.Classe;

public record JogadorRequestDTO (
    String nome,
    Classe tipo
){
    
}
