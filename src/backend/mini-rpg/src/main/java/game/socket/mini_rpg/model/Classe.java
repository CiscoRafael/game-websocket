package game.socket.mini_rpg.model;

import lombok.Getter;

@Getter
public enum Classe {
    MAGO(70.0, 30.0, 5.0, 1.5, 0.8, 150.0, 1.5, 2, new IncendiarSkill()),
    GUERREIRO(120.0, 25.0, 15.0, 20.0, 1.0, 60.0, 2.5, 2.5, new PerfuraDefesaSkill()),
    TANK(200.0, 15.0, 20.0, 0.8, 0.5, 40.0, 3, 1, new ParrySkill()),
    CLERIGO(100.0, 15.0, 12.0, 1.2, 0.9, 120.0, 2.0, 1.5, new ClerigoSkill()),
    LADINO(80.0, 18.0, 8.0, 2.5, 1.5, 30.0, 4, 2, new EsquivaSkill());

    private final double hpBase;
    private final double danoBase;
    private final double defesaBase;
    private final double velAtqBase;
    private final double velMovi;
    private final double manaBase;
    private final double vigorPorAtaque;
    private final double vigorPorDefesa;

    private final SpecialSkill skillPadrao;

    Classe(double hp, double dano, double defesa, double velAtq, double velMovi, double mana, double vigorPorAtaque, double vigorPorDefesa, SpecialSkill skillPadrao) {
        this.hpBase = hp;
        this.danoBase = dano;
        this.defesaBase = defesa;
        this.velAtqBase = velAtq;
        this.velMovi = velMovi;
        this.manaBase = mana;
        this.vigorPorAtaque = vigorPorAtaque;
        this.vigorPorDefesa = vigorPorDefesa;
        this.skillPadrao = skillPadrao;
    }

    public SpecialSkill getSkillPadrao() { return skillPadrao; }
}