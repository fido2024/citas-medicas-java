// Actividad10Patrones.java
// Java 8+ (probado en OpenJDK 25)
// Para Compilar: javac Actividad10Patrones.java
// y luego Ejecutar:   java Actividad10Patrones

import java.util.ArrayList;
import java.util.List;

/*
 * Implementacion en Java de:
 * - Factory Method (Creacional)
 * - Decorator (Estructural)
 * - Observer (Comportamiento)
 *
 */

// ----------------------------
// Patrón Creacional: Factory Method
// ----------------------------
abstract class Cita {
    protected String nombrePaciente;
    protected String horario;
    protected String estado = "reservada";
    private final List<Observador> observadores = new ArrayList<>();

    public Cita(String nombrePaciente, String horario) {
        this.nombrePaciente = nombrePaciente;
        this.horario = horario;
    }

    public void adjuntar(Observador o) {
        observadores.add(o);
    }

    public void separar(Observador o) {
        observadores.remove(o);
    }

    protected void notificarObservadores() {
        List<Observador> copia = new ArrayList<>(observadores);
        for (Observador o : copia) {
            o.actualizar(this);
        }
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        notificarObservadores();
    }

    public String getEstado() { return estado; }
    public String getNombrePaciente() { return nombrePaciente; }

    public abstract String info();
}

class CitaGeneral extends Cita {
    public CitaGeneral(String nombrePaciente, String horario) {
        super(nombrePaciente, horario);
    }

    @Override
    public String info() {
        return "Cita general para " + nombrePaciente + " a las " + horario + " (estado: " + estado + ")";
    }
}

class CitaEspecialista extends Cita {
    private String especialidad;

    public CitaEspecialista(String nombrePaciente, String horario, String especialidad) {
        super(nombrePaciente, horario);
        this.especialidad = especialidad;
    }

    @Override
    public String info() {
        return "Cita con especialista (" + especialidad + ") para " + nombrePaciente + " a las " + horario + " (estado: " + estado + ")";
    }
}

// Creador (Factory Method)
abstract class CreadorCita {
    public abstract Cita crear(String nombrePaciente, String horario, String... extras);
}

class CreadorGeneral extends CreadorCita {
    @Override
    public Cita crear(String nombrePaciente, String horario, String... extras) {
        return new CitaGeneral(nombrePaciente, horario);
    }
}

class CreadorEspecialista extends CreadorCita {
    @Override
    public Cita crear(String nombrePaciente, String horario, String... extras) {
        String esp = extras != null && extras.length > 0 ? extras[0] : "General";
        return new CitaEspecialista(nombrePaciente, horario, esp);
    }
}

// ----------------------------
// Patrón Estructural: Decorator
// ----------------------------
abstract class DecoradorCita extends Cita {
    protected Cita envuelta;

    public DecoradorCita(Cita envuelta) {
        super(envuelta.getNombrePaciente(), envuelta.horario);
        this.envuelta = envuelta;
    }

    @Override
    public void adjuntar(Observador o) {
        envuelta.adjuntar(o);
    }

    @Override
    public void separar(Observador o) {
        envuelta.separar(o);
    }

    @Override
    public void cambiarEstado(String nuevoEstado) {
        envuelta.cambiarEstado(nuevoEstado);
    }

    @Override
    public String info() {
        return envuelta.info();
    }
}

class NotificacionSMS extends DecoradorCita {
    public NotificacionSMS(Cita envuelta) {
        super(envuelta);
    }

    @Override
    public String info() {
        return envuelta.info() + " + Notificación SMS";
    }

    public void enviarSms(String mensaje) {
        System.out.println("[SMS a " + envuelta.getNombrePaciente() + "]: " + mensaje);
    }
}

class RecordatorioEmail extends DecoradorCita {
    public RecordatorioEmail(Cita envuelta) {
        super(envuelta);
    }

    @Override
    public String info() {
        return envuelta.info() + " + Recordatorio por email";
    }

    public void enviarEmail(String mensaje) {
        System.out.println("[Email a " + envuelta.getNombrePaciente() + "]: " + mensaje);
    }
}

// ----------------------------
// Patrón de Comportamiento: Observer
// ----------------------------
interface Observador {
    void actualizar(Cita sujeto);
}

class ObservadorPaciente implements Observador {
    @Override
    public void actualizar(Cita sujeto) {
        System.out.println("(Paciente) " + sujeto.getNombrePaciente() + ", su cita ahora está: " + sujeto.getEstado());
    }
}

class ObservadorRecepcion implements Observador {
    @Override
    public void actualizar(Cita sujeto) {
        System.out.println("(Recepcion) Actualizar registro: " + sujeto.getNombrePaciente() + " -> " + sujeto.getEstado());
    }
}

// ----------------------------
// ejecucion por terminal
// ----------------------------
public class Actividad10Patrones {
    public static void main(String[] args) {
        System.out.println("::: Reservas de Citas con Patrones ::: \n");
        // Creadores (Factory Method)
        CreadorCita creadorGeneral = new CreadorGeneral();
        CreadorCita creadorEsp = new CreadorEspecialista();

        // Creamos citas
        Cita citaA = creadorGeneral.crear("Lucia Rivas", "2025-11-27 09:30");
        Cita citaB = creadorEsp.crear("Esteban Morales", "2025-11-28 14:00", "Neurologia");

        // Adjuntar observadores (Observer)
        Observador paciente = new ObservadorPaciente();
        Observador recepcion = new ObservadorRecepcion();

        citaA.adjuntar(paciente);
        citaA.adjuntar(recepcion);

        citaB.adjuntar(paciente);
        citaB.adjuntar(recepcion);

        // Aplicar decoradores (Decorator)
        citaA = new NotificacionSMS(citaA);
        citaB = new RecordatorioEmail(citaB);

        // Aca se muestra la informacion inicial
        System.out.println(citaA.info());
        System.out.println(citaB.info());
        System.out.println();

        // Cambiar estados -> se notifica automaticamente
        System.out.println("-- Confirmando la primera cita --");
        citaA.cambiarEstado("confirmada");

        if (citaA instanceof NotificacionSMS) {
            ((NotificacionSMS) citaA).enviarSms("Su cita ha sido confirmada. Por favor, llegue 10 min antes.");
        }

        System.out.println("\n-- Cancelando la segunda cita --");
        citaB.cambiarEstado("cancelada");

        if (citaB instanceof RecordatorioEmail) {
            ((RecordatorioEmail) citaB).enviarEmail("Lamentamos informarle que su cita ha sido cancelada. Contactenos para reprogramar.");
        }
    }
}
