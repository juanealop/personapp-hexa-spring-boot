package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_BUSCAR_POR_NUMERO = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
					menuOpciones(telefonoInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
					menuOpciones(telefonoInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
					telefonoInputAdapterCli.historial();
					break;
				case OPCION_CREAR:
					telefonoInputAdapterCli.crear(leerTelefono(keyboard, true));
					break;
				case OPCION_BUSCAR_POR_NUMERO:
					System.out.print("Ingrese número de teléfono: ");
					telefonoInputAdapterCli.buscar(keyboard.nextLine());
					break;
				case OPCION_EDITAR:
					System.out.print("Ingrese número del teléfono a editar: ");
					String numeroEditar = keyboard.nextLine();
					TelefonoModelCli telefonoEditar = leerTelefono(keyboard, false);
					telefonoEditar.setNumero(numeroEditar);
					telefonoInputAdapterCli.editar(numeroEditar, telefonoEditar);
					break;
				case OPCION_ELIMINAR:
					System.out.print("Ingrese número del teléfono a eliminar: ");
					telefonoInputAdapterCli.eliminar(keyboard.nextLine());
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (RuntimeException e) {
				log.warn("Solo se permiten numeros.");
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los telefonos");
		System.out.println(OPCION_CREAR + " para crear un telefono");
		System.out.println(OPCION_BUSCAR_POR_NUMERO + " para buscar un telefono por numero");
		System.out.println(OPCION_EDITAR + " para editar un telefono");
		System.out.println(OPCION_ELIMINAR + " para eliminar un telefono");
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

	private TelefonoModelCli leerTelefono(Scanner keyboard, boolean includeNumber) {
		TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
		if (includeNumber) {
			System.out.print("Ingrese numero: ");
			telefonoModelCli.setNumero(keyboard.nextLine());
		}
		System.out.print("Ingrese compania: ");
		telefonoModelCli.setCompania(keyboard.nextLine());
		telefonoModelCli.setDuenio(leerEntero(keyboard, "Ingrese id del dueño: "));
		return telefonoModelCli;
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
}
