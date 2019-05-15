package com.zrpg.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import com.zrpg.GameManager;
import com.zrpg.characters.*;

public class MapDisplay extends JPanel {

    private Display aff; // Référence à l'objet JFrame d'affichage
    private GameManager gm; // Référence à la classe principale

    private BufferedImage worldImage; // Espace de dessin
    private ImgLib imgLib;
    private ColorLib colorLib; // Couleurs prédéfinies

    private int caseHoveredX = 0; // X de la case survolée
    private int caseHoveredY = 0; // Y de la case survolée

    private int caseClickedX = 0;
    private int caseClickedY = 0;


    public MapDisplay(Display aff, GameManager gm) {
        this.aff = aff;
        this.gm = gm;
        this.colorLib = new ColorLib();
        this.imgLib = new ImgLib();

        worldImage = new BufferedImage(aff.res * gm.sizeX, aff.res * gm.sizeY, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(aff.res * gm.sizeX, aff.res * gm.sizeY));


        // Détection du clic
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { // Le bouton de la souris est pressé
                int x = e.getX(); // Récupération des coordonnées du clic
                int y = e.getY();

                caseClickedX = x / aff.res; // Calcul de la case cliquée
                caseClickedY = y / aff.res;
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { // Le bouton de la souris est relâché
                if (gm.mode == "game") {
                    int x = e.getX();
                    int y = e.getY();

                    int caseX = x / aff.res;
                    int caseY = y / aff.res;

                    if (caseX == caseClickedX && caseY == caseClickedY) { // Si la souris est restée sur la même case depuis que le clic est enfoncé
                        gm.clickHandle(caseX, caseY); // Appel d'une fonction de la classe maîtresse pour savoir si ce clic a des conséquences sur le jeu
                        repaint(); // Recalcul des éléments du canvas mis à jour
                    }
                } else if (gm.mode == "title"){
                    gm.clickTitleScreen("play");
                }
            }
        });

        // Détection du mouvement de souris
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX(); // Récupération des coordonnées du clic
                int y = e.getY();


                int caseX = x / aff.res;
                int caseY = y / aff.res;

                if (gm.overlay[caseHoveredY][caseHoveredX] == 3)
                    gm.overlay[caseHoveredY][caseHoveredX] = 2;

                caseHoveredX = caseX;
                caseHoveredY = caseY;

                if (gm.overlay[caseY][caseX] == 2)
                    gm.overlay[caseY][caseX] = 3;

                repaint();

            }
        });
    }

    private void drawOverlay(Graphics g) {
        for (int y = 0; y < gm.sizeY; y++) {
            for (int x = 0; x < gm.sizeX; x++) {
                fillTile(x, y, gm.overlay[y][x], g);
            }
        }
    }

    private void fillTile(int x, int y, int color, Graphics g) {
        if (y >= 0 && y < gm.sizeY && x >= 0 && x < gm.sizeX && color != 0) {
            g.setColor(colorLib.getBgColor(color)); // Choix de la couleur de peinture
            g.fillRect(aff.res * x, aff.res * y, aff.res, aff.res);
        }
    }

    private void drawBackground(Graphics g) {
        for (int y = 0; y < gm.sizeY; y++) {
            for (int x = 0; x < gm.sizeX; x++) {
                g.drawImage(imgLib.getBgImage(gm.background[y][x]), x * aff.res, y * aff.res, aff.res, aff.res, null);
            }
        }
    }

    private void drawChars(Graphics g, Player player) {
        int imgWidth;
        int imgHeight;

        for (int i = 0; i < player.characters.size(); i++) {
            PblCharacter character = player.characters.get(i); // Récupération du personnage

            // Positionnement et affichage du personnage
            if (character.isAlive()) {
                if (character.idle.getWidth() >= character.idle.getHeight()) {
                    imgWidth = aff.res;
                    imgHeight = (character.idle.getHeight() * aff.res) / character.idle.getWidth();
                } else {
                    imgHeight = aff.res;
                    imgWidth = (character.idle.getWidth() * aff.res) / character.idle.getHeight();
                }

                // Dessin du personnage
                g.drawImage(character.idle, character.getPosX() * aff.res + (aff.res - imgWidth) / 2,
                        character.getPosY() * aff.res + (aff.res - imgHeight) / 2,
                        imgWidth, imgHeight, null);

                // Dessin du fond bleu de la barre de vie
                g.setColor(colorLib.getColor("Light blue"));
                g.fillRoundRect(aff.res * character.getPosX(),      // x
                        (int) (aff.res * (character.getPosY() - 0.2)), // y

                        aff.res,                                       // Largeur
                        (int) (aff.res * 0.2),                         // Hauteur

                        (int) (aff.res * 0.08),                        // Arrondi x
                        (int) (aff.res * 0.08));                       // Arrondi y

                // Dessin du rouge de la barre de vie
                g.setColor(colorLib.getColor("Red"));
                g.fillRoundRect((int) (aff.res * (character.getPosX() + 0.05)),
                        (int) (aff.res * (character.getPosY() - 0.15)),

                        (int)(aff.res * 0.9 * character.hp/character.hpMax),
                        (int) (aff.res * 0.12),

                        (int) (aff.res * 0.08),
                        (int) (aff.res * 0.08));


            }
        }
    }

    private void drawTitleScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0, aff.res * gm.sizeX, aff.res * gm.sizeY);
        g.drawImage(imgLib.loadImage("Imgs/Logo.png"),aff.res * gm.sizeX / 10, aff.res * gm.sizeY / 10, aff.res * gm.sizeX * 9/10, aff.res * gm.sizeX / 2, null);
    }

    public void paint(Graphics g) {
        Graphics gw = worldImage.getGraphics(); // Espace de dessin

        if (gm.mode == "game") {
            drawBackground(gw);
            drawOverlay(gw);

            drawChars(gw, gm.players[0]);
            drawChars(gw, gm.players[1]);

        } else if (gm.mode == "title") {
            drawTitleScreen(gw);
        }

        g.drawImage(worldImage, 0, 0, null); // Affichage de l'image créée sur le Panel
    }
}
