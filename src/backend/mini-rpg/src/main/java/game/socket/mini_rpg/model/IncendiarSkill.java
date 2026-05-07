package game.socket.mini_rpg.model;

public class IncendiarSkill implements SpecialSkill {
    public String getNome() { return "Toque Ígneo"; }
    public double getMana() { return 25.00; };
    public double getVigor() {return 3.00; }
    
    @Override
    public void usar(Personagem usuario, Personagem alvo) {
        alvo.setStatusEfeito(StatusEfeito.QUEIMANDO);
    }
}
