package game.socket.mini_rpg.model;

public interface SpecialSkill {
    String getNome();
    double getMana();
    double getVigor();
    void usar(Personagem usuario, Personagem alvo);
}
