package org.example.model;


import java.util.*;


public class AdministradorTurnos {
    private List<Jugador> jugadores;
    private Jugador actual;
    private Queue<Jugador> turnos ;

    public AdministradorTurnos(List<Jugador> jugadores) {
        this.generarOrdenDePaso(jugadores);
        this.jugadores = jugadores;
        this.turnos = this.colaDeTurnos();
    }
    public Jugador getTurnoActual() {
        return actual;
    }



    private void generarOrdenDePaso(List<Jugador> jugadores){
        Collections.shuffle(jugadores);
    }

    public Queue<Jugador> colaDeTurnos() {
        Queue<Jugador> turnos = new ArrayDeque<>();
        assert this.jugadores != null;
        for (Jugador jugador : this.jugadores) {
            turnos.offer(jugador);
        }
        return turnos;
    }

    private boolean perdio(Jugador jugador){
        return jugador.getEstado() == Jugador.Estado.Quiebra;
    }

    public void avanzarTurno() {
        Jugador jugador = turnos.poll();
        if (!perdio(jugador)) {
            turnos.offer(jugador);
            this.actual = jugador;
            return;
        }
        System.out.println(jugador.getNombre() + " perdio!");
        this.jugadores.remove(jugador);
        avanzarTurno();
    }
    public int tirarDados(){
        Random random = new Random();
        return random.nextInt(2,12);
    }


}
