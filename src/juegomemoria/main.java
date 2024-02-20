package juegomemoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

public class main extends JFrame {
    private JButton[] botones;
    private String[] palabras;
    private int puntuacion;
    private int intentos;
    private Timer timer;
    private int primerBoton = -1;
    private boolean bloquearInput = false;

    public main() {
        setTitle("Juego de Memoria");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        palabras = new String[]{"jaen","cordoba","sevilla","huelva","cadiz","malaga","granada","almeria"};
        puntuacion = 0;
        intentos = 0;

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridLayout(4, 4));

        botones = new JButton[16];
        for (int i = 0; i < 16; i++) {
            botones[i] = new JButton();
            botones[i].addActionListener(new BotonListener());
            add(botones[i]);
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ocultarBotones();
            }
        });

        iniciarJuego();
    }

    private void iniciarJuego() {
        String[] palabrasBarajadas = barajarPalabras();
        String[] ciudades = new String[16];

        for (int i = 0; i < 8; i++) {
            ciudades[i] = palabrasBarajadas[i];
            ciudades[i + 8] = palabrasBarajadas[i];
        }

        Collections.shuffle(Arrays.asList(ciudades));

        for (int i = 0; i < 16; i++) {
            botones[i].setText("");
            botones[i].setEnabled(true);
            botones[i].putClientProperty("palabra", ciudades[i]);
        }

        bloquearInput = false;
    }

    private String[] barajarPalabras() {
        String[] palabrasBarajadas = Arrays.copyOf(palabras, palabras.length);
        Collections.shuffle(Arrays.asList(palabrasBarajadas));
        return palabrasBarajadas;
    }

    private class BotonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (bloquearInput) {
                return;
            }

            JButton boton = (JButton) e.getSource();
            mostrarPalabra(boton);
            comprobarJugada(boton);
        }
    }

    private void mostrarPalabra(JButton boton) {
        String palabra = (String) boton.getClientProperty("palabra");
        boton.setText(palabra);
    }

    private void ocultarBotones() {
        for (int i = 0; i < botones.length; i++) {
            botones[i].setText("");
            botones[i].setEnabled(true);
        }

        primerBoton = -1;
        bloquearInput = false;
        timer.stop();
    }

    private void comprobarJugada(JButton boton) {
        boton.setEnabled(false);

        if (primerBoton == -1) {
            primerBoton = Arrays.asList(botones).indexOf(boton);
        } else {
            int segundoBoton = Arrays.asList(botones).indexOf(boton);

            String palabra1 = (String) botones[primerBoton].getClientProperty("palabra");
            String palabra2 = (String) boton.getClientProperty("palabra");

            if (palabra1.equals(palabra2)) {
                puntuacion += 10;
                JOptionPane.showMessageDialog(this, "¡Coincidencia! Ganaste 10 puntos.");
                botones[primerBoton].setEnabled(false);
                boton.setEnabled(false);
            } else {
                bloquearInput = true;
                timer.start();
                reiniciarPuntuacion(); // Reinicia la puntuación en caso de error
            }

            if (puntuacion == 80) {
                JOptionPane.showMessageDialog(this, "¡Felicidades! Ganaste el juego con " + puntuacion + " puntos.");
                reiniciarJuego();
            }

            primerBoton = -1;
        }

        intentos++;
    }

    private void reiniciarJuego() {
        puntuacion = 0;
        intentos = 0;
        iniciarJuego();
    }

    private void reiniciarPuntuacion() {
        puntuacion = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new main().setVisible(true);
            }
        });
    }
}