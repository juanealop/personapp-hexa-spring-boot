package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_BUSCAR_POR_ID = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MODULOS:
					isValid = true;
					break;
				case PERSISTENCIA_MARIADB:
					personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			}  catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
					isValid = true;
					break;
				case OPCION_VER_TODO:
					personaInputAdapterCli.historial();					
					break;
				case OPCION_CREAR:
					personaInputAdapterCli.crear(leerPersona(keyboard, true));
					break;
				case OPCION_BUSCAR_POR_ID:
					personaInputAdapterCli.buscar(leerEntero(keyboard, "Ingrese el id de la persona: "));
					break;
				case OPCION_EDITAR:
					Integer idEditar = leerEntero(keyboard, "Ingrese el id de la persona a editar: ");
					PersonaModelCli personaEditar = leerPersona(keyboard, false);
					personaEditar.setCc(idEditar);
					personaInputAdapterCli.editar(idEditar, personaEditar);
					break;
				case OPCION_ELIMINAR:
					personaInputAdapterCli.eliminar(leerEntero(keyboard, "Ingrese el id de la persona a eliminar: "));
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (RuntimeException e) {
				String detail = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
				log.warn("Error en operacion de personas: " + detail);
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_CREAR + " para crear una persona");
		System.out.println(OPCION_BUSCAR_POR_ID + " para buscar una persona por id");
		System.out.println(OPCION_EDITAR + " para editar una persona");
		System.out.println(OPCION_ELIMINAR + " para eliminar una persona");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opción: ");
			return Integer.parseInt(keyboard.nextLine());
		} catch (RuntimeException e) {
			log.warn("Solo se permiten numeros.");
			return leerOpcion(keyboard);
		}
	}

	private Integer leerEntero(Scanner keyboard, String mensaje) {
		try {
			System.out.print(mensaje);
			return Integer.parseInt(keyboard.nextLine());
		} catch (RuntimeException e) {
			log.warn("Solo se permiten numeros.");
			return leerEntero(keyboard, mensaje);
		}
	}

	private PersonaModelCli leerPersona(Scanner keyboard, boolean includeId) {
		PersonaModelCli personaModelCli = new PersonaModelCli();
		if (includeId) {
			personaModelCli.setCc(leerEntero(keyboard, "Ingrese cc: "));
		}
		System.out.print("Ingrese nombre: ");
		personaModelCli.setNombre(keyboard.nextLine());
		System.out.print("Ingrese apellido: ");
		personaModelCli.setApellido(keyboard.nextLine());
		System.out.print("Ingrese genero (M/F/OTHER): ");
		personaModelCli.setGenero(keyboard.nextLine());
		System.out.print("Ingrese edad (vacio para null): ");
		String edad = keyboard.nextLine();
		if (edad == null || edad.isBlank()) {
			personaModelCli.setEdad(null);
		} else {
			try {
				personaModelCli.setEdad(Integer.parseInt(edad));
			} catch (RuntimeException e) {
				log.warn("Edad invalida, se asigna null");
				personaModelCli.setEdad(null);
			}
		}
		return personaModelCli;
	}

}
