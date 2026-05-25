package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_BUSCAR_POR_ID = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
					profesionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
					menuOpciones(profesionInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					profesionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
					menuOpciones(profesionInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
					profesionInputAdapterCli.historial();
					break;
				case OPCION_CREAR:
					profesionInputAdapterCli.crear(leerProfesion(keyboard, true));
					break;
				case OPCION_BUSCAR_POR_ID:
					profesionInputAdapterCli.buscar(leerEntero(keyboard, "Ingrese el id de la profesion: "));
					break;
				case OPCION_EDITAR:
					Integer idEditar = leerEntero(keyboard, "Ingrese el id de la profesion a editar: ");
					ProfesionModelCli profesionEditar = leerProfesion(keyboard, false);
					profesionEditar.setId(idEditar);
					profesionInputAdapterCli.editar(idEditar, profesionEditar);
					break;
				case OPCION_ELIMINAR:
					profesionInputAdapterCli.eliminar(leerEntero(keyboard, "Ingrese el id de la profesion a eliminar: "));
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten numeros.");
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
		System.out.println(OPCION_CREAR + " para crear una profesion");
		System.out.println(OPCION_BUSCAR_POR_ID + " para buscar una profesion por id");
		System.out.println(OPCION_EDITAR + " para editar una profesion");
		System.out.println(OPCION_ELIMINAR + " para eliminar una profesion");
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

	private ProfesionModelCli leerProfesion(Scanner keyboard, boolean includeId) {
		ProfesionModelCli profesionModelCli = new ProfesionModelCli();
		if (includeId) {
			profesionModelCli.setId(leerEntero(keyboard, "Ingrese id de profesion: "));
		}
		System.out.print("Ingrese nombre de profesion: ");
		profesionModelCli.setNombre(keyboard.nextLine());
		System.out.print("Ingrese descripcion: ");
		profesionModelCli.setDescripcion(keyboard.nextLine());
		return profesionModelCli;
	}
}
