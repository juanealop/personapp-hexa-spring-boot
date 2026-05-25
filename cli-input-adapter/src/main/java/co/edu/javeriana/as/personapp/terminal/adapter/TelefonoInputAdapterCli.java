package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private TelefonoMapperCli telefonoMapperCli;

	private PhoneInputPort phoneInputPort;
	private PersonOutputPort personOutputPort;

	public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			personOutputPort = personOutputPortMaria;
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			personOutputPort = personOutputPortMongo;
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Telefono in Input Adapter");
		phoneInputPort.findAll().stream().map(telefonoMapperCli::fromDomainToAdapterCli).forEach(System.out::println);
	}

	public void crear(TelefonoModelCli telefonoModelCli) {
		try {
			Person owner = resolveOwner(telefonoModelCli.getDuenio());
			Phone phone = phoneInputPort.create(telefonoMapperCli.fromAdapterCliToDomain(telefonoModelCli, owner));
			System.out.println(telefonoMapperCli.fromDomainToAdapterCli(phone));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void buscar(String number) {
		try {
			Phone phone = phoneInputPort.findOne(number);
			System.out.println(telefonoMapperCli.fromDomainToAdapterCli(phone));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void editar(String number, TelefonoModelCli telefonoModelCli) {
		try {
			Person owner = resolveOwner(telefonoModelCli.getDuenio());
			Phone phone = phoneInputPort.edit(number, telefonoMapperCli.fromAdapterCliToDomain(telefonoModelCli, owner));
			System.out.println(telefonoMapperCli.fromDomainToAdapterCli(phone));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void eliminar(String number) {
		try {
			System.out.println("Eliminado: " + phoneInputPort.drop(number));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	private Person resolveOwner(Integer ownerId) throws NoExistException {
		if (ownerId == null) {
			throw new NoExistException("Owner id is required");
		}
		Person owner = personOutputPort.findById(ownerId);
		if (owner == null) {
			throw new NoExistException("The owner with id " + ownerId + " does not exist into db");
		}
		return owner;
	}
}
