package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return  DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			if(setPersonOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			}else {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
			
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PersonaResponse>();
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
		try {
			String database = setPersonOutputPortInjection(request.getDatabase());
			Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
			return personaMapperRest.fromDomainToAdapterRest(person, database);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getDni(), request.getFirstName(), request.getLastName(), request.getAge(), request.getSex(), request.getDatabase(), e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getDni(), request.getFirstName(), request.getLastName(), request.getAge(), request.getSex(), request.getDatabase(), "Error creating person");
		}
	}

	public PersonaResponse findPersona(String database, Integer identification) {
		try {
			String selectedDatabase = setPersonOutputPortInjection(database);
			Person person = personInputPort.findOne(identification);
			return personaMapperRest.fromDomainToAdapterRest(person, selectedDatabase);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), "", "", "", "", database, e.getMessage());
		}
	}

	public PersonaResponse editarPersona(Integer identification, PersonaRequest request) {
		try {
			String database = setPersonOutputPortInjection(request.getDatabase());
			Person person = personInputPort.edit(identification, personaMapperRest.fromAdapterToDomain(request));
			return personaMapperRest.fromDomainToAdapterRest(person, database);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), request.getFirstName(), request.getLastName(), request.getAge(), request.getSex(), request.getDatabase(), e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), request.getFirstName(), request.getLastName(), request.getAge(), request.getSex(), request.getDatabase(), "Error editing person");
		}
	}

	public PersonaResponse eliminarPersona(String database, Integer identification) {
		try {
			String selectedDatabase = setPersonOutputPortInjection(database);
			personInputPort.drop(identification);
			return new PersonaResponse(String.valueOf(identification), "", "", "", "", selectedDatabase, "OK");
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), "", "", "", "", database, e.getMessage());
		}
	}

	private PersonaResponse errorResponse(String dni, String firstName, String lastName, String age, String sex, String database, String status) {
		return new PersonaResponse(
			dni == null ? "" : dni,
			firstName == null ? "" : firstName,
			lastName == null ? "" : lastName,
			age == null ? "" : age,
			sex == null ? "" : sex,
			database == null ? "" : database,
			status);
	}

}
