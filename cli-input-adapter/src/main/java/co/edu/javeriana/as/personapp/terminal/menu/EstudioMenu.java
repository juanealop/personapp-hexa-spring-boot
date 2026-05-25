package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.time.LocalDate;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudioMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_BUSCAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
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
					estudioInputAdapterCli.setStudyOutputPortInjection("MARIA");
					menuOpciones(estudioInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					estudioInputAdapterCli.setStudyOutputPortInjection("MONGO");
					menuOpciones(estudioInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
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
					estudioInputAdapterCli.historial();
					break;
				case OPCION_CREAR:
					estudioInputAdapterCli.crear(leerEstudio(keyboard, true));
					break;
				case OPCION_BUSCAR:
					Integer ccBuscar = leerEntero(keyboard, "Ingrese id de persona: ");
					Integer profBuscar = leerEntero(keyboard, "Ingrese id de profesion: ");
					estudioInputAdapterCli.buscar(ccBuscar, profBuscar);
					break;
				case OPCION_EDITAR:
					Integer ccEditar = leerEntero(keyboard, "Ingrese id de persona del estudio a editar: ");
					Integer profEditar = leerEntero(keyboard, "Ingrese id de profesion del estudio a editar: ");
					EstudioModelCli estudioEditar = leerEstudio(keyboard, false);
					estudioEditar.setPersona(ccEditar);
					estudioEditar.setProfesion(profEditar);
					estudioInputAdapterCli.editar(ccEditar, profEditar, estudioEditar);
					break;
				case OPCION_ELIMINAR:
					Integer ccEliminar = leerEntero(keyboard, "Ingrese id de persona del estudio a eliminar: ");
					Integer profEliminar = leerEntero(keyboard, "Ingrese id de profesion del estudio a eliminar: ");
					estudioInputAdapterCli.eliminar(ccEliminar, profEliminar);
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
		System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
		System.out.println(OPCION_CREAR + " para crear un estudio");
		System.out.println(OPCION_BUSCAR + " para buscar un estudio");
		System.out.println(OPCION_EDITAR + " para editar un estudio");
		System.out.println(OPCION_ELIMINAR + " para eliminar un estudio");
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

	private EstudioModelCli leerEstudio(Scanner keyboard, boolean includeIds) {
		EstudioModelCli estudioModelCli = new EstudioModelCli();
		if (includeIds) {
			estudioModelCli.setPersona(leerEntero(keyboard, "Ingrese id de persona: "));
			estudioModelCli.setProfesion(leerEntero(keyboard, "Ingrese id de profesion: "));
		}
		System.out.print("Ingrese fecha de graduacion (yyyy-MM-dd, vacio para null): ");
		String fechaGraduacion = keyboard.nextLine();
		estudioModelCli.setFechaGraduacion(fechaGraduacion == null || fechaGraduacion.isBlank()
				? null
				: LocalDate.parse(fechaGraduacion));
		System.out.print("Ingrese universidad: ");
		estudioModelCli.setUniversidad(keyboard.nextLine());
		return estudioModelCli;
	}
}
