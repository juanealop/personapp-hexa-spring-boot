package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

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
	private TelefonoMapperRest telefonoMapperRest;

	private PhoneInputPort phoneInputPort;
	private PersonOutputPort personOutputPort;

	private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			personOutputPort = personOutputPortMaria;
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			personOutputPort = personOutputPortMongo;
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Invalid database option: " + dbOption);
	}

	public List<TelefonoResponse> historial(String database) {
		log.info("Into historial Telefono in Input Adapter REST");
		try {
			String selectedDatabase = setPhoneOutputPortInjection(database);
			return phoneInputPort.findAll().stream().map(phone -> telefonoMapperRest.fromDomainToAdapterRest(phone, selectedDatabase))
					.collect(Collectors.toList());
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public TelefonoResponse crearTelefono(TelefonoRequest request) {
		try {
			String database = setPhoneOutputPortInjection(request.getDatabase());
			Person owner = resolveOwner(request.getOwnerId());
			Phone phone = phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request, owner));
			return telefonoMapperRest.fromDomainToAdapterRest(phone, database);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getNumber(), request.getCompany(), request.getOwnerId(), request.getDatabase(),
					e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getNumber(), request.getCompany(), request.getOwnerId(), request.getDatabase(),
					"Error creating phone");
		}
	}

	public TelefonoResponse findTelefono(String database, String number) {
		try {
			String selectedDatabase = setPhoneOutputPortInjection(database);
			Phone phone = phoneInputPort.findOne(number);
			return telefonoMapperRest.fromDomainToAdapterRest(phone, selectedDatabase);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(number, "", "", database, e.getMessage());
		}
	}

	public TelefonoResponse editarTelefono(String number, TelefonoRequest request) {
		try {
			request.setNumber(number);
			String database = setPhoneOutputPortInjection(request.getDatabase());
			Person owner = resolveOwner(request.getOwnerId());
			Phone phone = phoneInputPort.edit(number, telefonoMapperRest.fromAdapterToDomain(request, owner));
			return telefonoMapperRest.fromDomainToAdapterRest(phone, database);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(number, request.getCompany(), request.getOwnerId(), request.getDatabase(),
					e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(number, request.getCompany(), request.getOwnerId(), request.getDatabase(),
					"Error editing phone");
		}
	}

	public TelefonoResponse eliminarTelefono(String database, String number) {
		try {
			String selectedDatabase = setPhoneOutputPortInjection(database);
			phoneInputPort.drop(number);
			return new TelefonoResponse(number, "", "", selectedDatabase, "OK");
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(number, "", "", database, e.getMessage());
		}
	}

	private Person resolveOwner(String ownerId) throws NoExistException {
		if (ownerId == null || ownerId.isBlank()) {
			throw new NoExistException("Owner id is required");
		}
		Person owner = personOutputPort.findById(Integer.parseInt(ownerId));
		if (owner == null) {
			throw new NoExistException("The owner with id " + ownerId + " does not exist into db");
		}
		return owner;
	}

	private TelefonoResponse errorResponse(String number, String company, String ownerId, String database, String status) {
		return new TelefonoResponse(number == null ? "" : number, company == null ? "" : company,
				ownerId == null ? "" : ownerId, database == null ? "" : database, status);
	}
}
