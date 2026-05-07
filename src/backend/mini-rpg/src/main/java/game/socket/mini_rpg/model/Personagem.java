package game.socket.mini_rpg.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Personagem {
    private String nome;
    private Classe tipo;
    
    private double hpAtual;
    private double manaAtual;
    private double vigorAtual; 
    private StatusEfeito statusEfeito = StatusEfeito.NORMAL;
    
    private double hpMax;
    private double velocidadeAtaque;
    private double velocidadeMovimento;
    private double dano;
    private double defense;
    private SpecialSkill specialSkill;
    
    private int potions;
    private double vigorTotal;
    private double vigorPorAtaque;
    private double vigorPorDefesa;

    public Personagem(String nome, Classe tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.hpMax = tipo.getHpBase();
        this.hpAtual = tipo.getHpBase();
        this.manaAtual = tipo.getManaBase();
        this.vigorTotal = 10.0; 
        this.vigorAtual = 10.0; // Inicia cheio
        this.potions = 5; // Defina a quantidade de poçoes(Cada uma cura 50 de vida)
        this.velocidadeAtaque = tipo.getVelAtqBase();
        this.velocidadeMovimento = tipo.getVelMovi();
        this.dano = tipo.getDanoBase();
        this.defense = tipo.getDefesaBase();
        this.vigorPorAtaque = tipo.getVigorPorAtaque();
        this.vigorPorDefesa = tipo.getVigorPorDefesa();
        this.specialSkill = tipo.getSkillPadrao();
    }
}