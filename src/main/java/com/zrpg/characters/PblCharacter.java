package com.zrpg.characters;

import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class PblCharacter {

	public String name; // Nom du personnage
	private boolean alive; // Est-ce que le personnage est en vie
	public String type; // Classe du personnage

	private int posX; // colonne sur la map
	private int posY; // ligne

	public int hp; // Points de vie restants
	public int hpMax; // Points de vie totaux
	public int mp; // Points de magie restants
	public int mpMax; // Points de vie totaux
	public int defense; // Points de défense/bouclier
	public int attack; // Points de dégats moyen

	public int range; // Portée d'attaque en nb de cases
	public int speed; // Portée de déplacement
	public int precision; // Variation des dégats d'attaque, l'attaque va de attack - precision à attack + precision

	public BufferedImage idle;

	public PblCharacter(String name, int posX, int posY, String type, int hp, int mp, int defense, int attack, int range, int precision, int speed){
		this.name = name;
		this.type = type;
		this.alive = true;

		this.posX = posX;
		this.posY = posY;

		this.hp = hp;
		this.hpMax = hp;
		this.mp = mp;
		this.mpMax = mp;
		this.defense = defense;

		this.attack = attack;
		this.range = range;
		this.speed = speed;
		this.precision = precision;

		try {
			String filename = "Sprites/"+ this.type + "/idle.png";
			idle = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
		} catch (Exception e) {
			System.out.println("Image non trouvée");
		}
	}

	public String toString(){
		return this.name + " a encore " + this.hp + "hp";
	}

	public void weaken(int damage){
		this.hp -= damage;

		if (this.hp <= 0) {
			this.alive = false;
		}
	}

	public void attack(PblCharacter adversary){
		int damage = this.attack;

		/*
		Ta formule empêche la compilation, à revoir...

		int damage = (this.attack*4 - adversary.defense*2) * (1 - (100-this.precision)/100 * (0.5 + (int)(Math.random())));
		*/

		/*
		Formule de base, à améliorer en regardant ce lien: http://www.rpgmakervx-fr.com/t21422-formules-de-degats
		*/

		adversary.weaken(damage);
	}

	/**** GETTERS & SETTERS ****/
	public boolean isAlive () {
		return this.alive;
	}

	public int getPosX() {
		return this.posX;
	}

	public int getPosY() {
		return this.posY;
	}

	public void moveTo(int x, int y) {
		this.posX = x;
		this.posY = y;
	}
	/***************************/
}