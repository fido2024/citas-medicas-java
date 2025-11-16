# Módulo de Gestión de Citas Médicas (Patrones de Diseño)

Este proyecto corresponde a la Actividad 10 del curso de Arquitectura de Software.  
El objetivo es demostrar la aplicación práctica de tres patrones de diseño:

- **Factory Method** (patrón creacional)  
- **Decorator** (patrón estructural)  
- **Observer** (patrón de comportamiento)  

Todo el desarrollo está realizado en **Java (OpenJDK 25)** y se ejecuta desde **terminal**, sin interfaz gráfica.

---

## 🧩 Descripción general del proyecto

El sistema simula la gestión básica de citas médicas.  
Permite:

- Crear citas generales o con especialista  
- Agregar observadores que reciben actualizaciones cuando la cita cambia de estado  
- Añadir funciones extra como enviar SMS o recordatorios por correo electrónico  

El código está escrito en español para facilitar su comprensión y explicación.

---

## 📐 Patrones de diseño implementados

### ✔ Factory Method  
Se usa para crear los distintos tipos de citas a través de clases creadoras (`CreadorGeneral` y `CreadorEspecialista`), evitando el acoplamiento directo con las clases concretas.

### ✔ Decorator  
Permite agregar comportamientos adicionales a una cita ya existente, como:
- Notificación por SMS (`NotificacionSMS`)
- Recordatorio por email (`RecordatorioEmail`)

Esto evita modificar la clase base de la cita.

### ✔ Observer  
Gestiona la notificación automática a los interesados cuando una cita cambia su estado.  
Los observadores implementados son:
- `ObservadorPaciente`
- `ObservadorRecepcion`

---

## ▶️ Cómo ejecutar el programa

### **1. Compilar**

javac Actividad10Patrones.java

2. Ejecutar

java Actividad10Patrones

Requisitos
Java 8 o superior (funciona correctamente en OpenJDK 25)

No requiere dependencias externas

📁 Archivos principales

Actividad10Patrones.java   # Código fuente principal
README.md                  # Documento actual

✍️ Autor
Fidel Vasquez Carata

📚 Nota
El proyecto fue desarrollado con fines educativos siguiendo las pautas de la Actividad 10 del curso.
Se priorizó la simplicidad, claridad y la correcta aplicación de los patrones de diseño requeridos.