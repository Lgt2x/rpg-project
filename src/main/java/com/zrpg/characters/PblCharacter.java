package com.zrpg.characters;

import java.awt.image.BufferedImage;
import com.zrpg.display.ImgLib;

public class PblCharacter {

	private boolean alive; // Est-ce que le personnage est en vie
	private String type; // Classe du personnage
	private boolean melee; // Vrai si le personnage n'a pas la capacité d'attaquer à travers les obstacles

	private int posX; // colonne sur la map
	private int posY; // ligne

	private int hp; // Points de vie restants
	private int hpMax; // Points de vie totaux
	private int defense; // Points de défense/bouclier
	private int attack; // Points de dégats moyen

	private int range; // Portée d'attaque en nb de cases
	private int speed; // Portée de déplacement
	private int precision; // Variation des dégats d'attaque, l'attaque va de attack - precision à attack + precision

	private ImgLib imgLib; // Référence à la classe de gestion d'images
	private BufferedImage currentSprite; // Sprite affiché à l'écran à tout instant, modifié lors de l'attaque
	private boolean facesLeft; // Vrai si le personnage est tourné vers la gauche

	public PblCharacter(int posX, int posY, String type, int hp, int defense, int attack, int range, int precision, int speed, boolean facesLeft, boolean melee){
		this.type = type;
		this.melee = melee;
		this.alive = true;

		this.posX = posX;
		this.posY = posY;

		this.hp = hp;
		this.hpMax = hp;
		this.defense = defense;

		this.attack = attack;
		this.range = range;
		this.speed = speed;
		this.precision = precision;

		this.facesLeft = facesLeft;
		this.imgLib = new ImgLib();
		this.currentSprite = imgLib.loadImage("Chars/" + this.type + "/idle.png"); // Chargement de l'image de base du personnage
	}

	public String toString(){
		return this.type + " a encore " + this.hp + "hp";
	}

	/**
	 * Affaiblit le personnage d'un nombre de points de vie donné
	 * @param damage Points de vie enlevés
	 */
	private void weaken(int damage){
		this.hp -= damage;

		if (this.hp <= 0) {
			this.alive = false;
		}
	}

	/**
	 * Calcule les dégâts et attaque un adversaire avec l'attaque de base
	 * @param adversary Ennemi attaqué
	 */
	public void attack(PblCharacter adversary){
		int damage = (int)((double)(this.attack) * (20f/(20f+(double)(adversary.defense)))); // f(x) = 20/(20+x) donne f(0) = 1 et f(50) ~= 0.3, réduit donc l'attaque avec proportion
		adversary.weaken(damage);
	}

	public void changeSprite(String sprite) {
		this.currentSprite = imgLib.loadImage("Chars/" + this.type + "/" + sprite + ".png");
	}

	/**
	 * Déplace le personnage vers la case indiquée
	 * @param x Colonne d'arrivée
	 * @param y Ligne d'arrivée
	 */
	public void moveTo(int x, int y) {
		if (this.posX < x) {
			this.facesLeft = false;
		} else if (this.posX > x) {
			this.facesLeft = true;
		}

		this.posX = x;
		this.posY = y;
	}

	public boolean isAlive () { return this.alive; }
	public boolean isFacingLeft() { return this.facesLeft; }
	public boolean isMelee() { return this.melee; }
	public int getPosX() 	{ return this.posX; }
	public int getPosY() 	{ return this.posY; }
	public int getHp() 		{ return this.hp; }
	public int getHpMax() 	{ return this.hpMax; }
	public int getRange() 	{ return this.range; }
	public int getSpeed()	{ return this.speed; }
	public BufferedImage getCurrentSprite() { return this.currentSprite; }
}
