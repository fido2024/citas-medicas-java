// Actividad10Patrones.java
// Java 8+ (probado en OpenJDK 25)
// Para Compilar: javac Actividad10Patrones.java
// Para Ejecutar: java Actividad10Patrones

import java.util.ArrayList;
import java.util.List;

// ---------------------------- 
//  Patron Creacional: Factory Method (Clase Base Cita) 
// ----------------------------
// Facilita extender el sistema a nuevas citas sin modificar el codigo existente

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
        // copia para evitar ConcurrentModification si un observador modifica la lista
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

// ----------------------------
// Tipos de Cita
// ----------------------------
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

// ----------------------------
// Factory Method (Creador)
// ----------------------------
// Cada creador define crearCita() y devuelve el tipo de Cita que corresponde

abstract class CreadorCita {
    public abstract Cita crearCita();
}

class CreadorGeneral extends CreadorCita {
    private final String nombre;
    private final String horario;

    public CreadorGeneral(String nombre, String horario) {
        this.nombre = nombre;
        this.horario = horario;
    }

    @Override
    public Cita crearCita() {
        return new CitaGeneral(nombre, horario);
    }
}

class CreadorEspecialista extends CreadorCita {
    private final String nombre;
    private final String horario;
    private final String especialidad;

    public CreadorEspecialista(String nombre, String horario, String especialidad) {
        this.nombre = nombre;
        this.horario = horario;
        this.especialidad = especialidad;
    }

    @Override
    public Cita crearCita() {
        return new CitaEspecialista(nombre, horario, especialidad);
    }
}

// ----------------------------
// Patron Decorator
// ----------------------------
// DecoradorCita encapsula una cita real para añadir funciones extra sin modificar su clase
// Todas las operaciones importantes se delegan a "citaReal"
// El decorador solo agrega capacidades adicionales como sms o email

abstract class DecoradorCita extends Cita {

    // Esta es la cita original 
    protected final Cita citaReal;

    public DecoradorCita(Cita citaReal) {
        super(citaReal.getNombrePaciente(), citaReal.horario);
        this.citaReal = citaReal;
    }

    @Override
    public void adjuntar(Observador o) { 
        citaReal.adjuntar(o); 
    }

    @Override
    public void separar(Observador o) { 
        citaReal.separar(o); 
    }

    @Override
    public void cambiarEstado(String nuevoEstado) { 
        citaReal.cambiarEstado(nuevoEstado); 
    }

    @Override
    public String info() { 
        return citaReal.info(); 
    }
}

class NotificacionSMS extends DecoradorCita {

    public NotificacionSMS(Cita citaReal) {
        super(citaReal);
    }

    @Override
    public String info() {
        return citaReal.info() + " + Notificacion SMS";
    }

    public void enviarSms(String mensaje) {
        System.out.println("[SMS a " + citaReal.getNombrePaciente() + "]: " + mensaje);
    }
}

class RecordatorioEmail extends DecoradorCita {

    public RecordatorioEmail(Cita citaReal) {
        super(citaReal);
    }

    @Override
    public String info() {
        return citaReal.info() + " + Recordatorio por email";
    }

    public void enviarEmail(String mensaje) {
        System.out.println("[Email a " + citaReal.getNombrePaciente() + "]: " + mensaje);
    }
}

// ----------------------------
// Patron Observer
// ----------------------------
// permite que la cita notifique automaticamenta a quienes esten suscritos
// Los observadores reaccionan a los cambios sin que la Cita dependa directamente de ellos
// Facilita desacoplar notificaciones y mantener el sistema flexible

interface Observador {
    void actualizar(Cita sujeto);
}

class ObservadorPaciente implements Observador {
    @Override
    public void actualizar(Cita sujeto) {
        System.out.println("(Paciente) " + sujeto.getNombrePaciente() + ", su cita ahora esta: " + sujeto.getEstado());
    }
}

class ObservadorRecepcion implements Observador {
    @Override
    public void actualizar(Cita sujeto) {
        System.out.println("(Recepcion) Actualizar registro: " + sujeto.getNombrePaciente() + " -> " + sujeto.getEstado());
    }
}

// ----------------------------
// Ejecucion por Terminal
// ----------------------------
public class Actividad10Patrones {
    public static void main(String[] args) {
        System.out.println("=== Reservas de Citas con Patrones ===");

        // Factory: los creadores reciben datos y crean la instancia con crearCita()
        CreadorCita creadorA = new CreadorGeneral("Mariana Diaz", "2025-11-20 10:00");
        CreadorCita creadorB = new CreadorEspecialista("Roberto Cruz", "2025-11-21 15:30", "Dermatologia");

        Cita cita1 = creadorA.crearCita();
        Cita cita2 = creadorB.crearCita();

        // Observer: creo mis obeservadores y luego registro observadores simples
        Observador paciente = new ObservadorPaciente();
        Observador recepcion = new ObservadorRecepcion();

        cita1.adjuntar(paciente);
        cita1.adjuntar(recepcion);

        cita2.adjuntar(paciente);
        cita2.adjuntar(recepcion);

        // Decorator: agrega comportamiento por instancia (sms/email)
        cita1 = new NotificacionSMS(cita1);
        cita2 = new RecordatorioEmail(cita2);

        // Mostrar informacion inicial
        System.out.println(cita1.info());
        System.out.println(cita2.info());
        System.out.println();

        // Cambiar estado -> notifican observadores y luego utilizo el decorador
        System.out.println("-- Confirmando la primera cita --");
        cita1.cambiarEstado("confirmada");
        if (cita1 instanceof NotificacionSMS) {
            ((NotificacionSMS) cita1).enviarSms("Su cita ha sido confirmada. Por favor llegue 10 minutos antes.");
        }

        System.out.println("\n-- Cancelando la segunda cita --");
        cita2.cambiarEstado("cancelada");
        if (cita2 instanceof RecordatorioEmail) {
            ((RecordatorioEmail) cita2).enviarEmail("Su cita ha sido cancelada. Contacte para reprogramar.");
        }
    }
}
