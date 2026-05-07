package game.socket.mini_rpg.model;

public class EsquivaSkill implements SpecialSkill {
    public String getNome() { return "Esquiva, otário!"; }
    public double getMana() { return 0.00; }
    public double getVigor() {return 6.00; }

    @Override
    public void usar(Personagem usuario, Personagem alvo) {
        usuario.setStatusEfeito(StatusEfeito.ESQUIVA);
    }
}