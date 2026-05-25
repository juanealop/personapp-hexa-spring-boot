package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperRest profesionMapperRest;

	private ProfessionInputPort professionInputPort;

	private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Invalid database option: " + dbOption);
	}

	public List<ProfesionResponse> historial(String database) {
		log.info("Into historial Profesion in Input Adapter REST");
		try {
			String selectedDatabase = setProfessionOutputPortInjection(database);
			return professionInputPort.findAll().stream()
					.map(profession -> profesionMapperRest.fromDomainToAdapterRest(profession, selectedDatabase))
					.collect(Collectors.toList());
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public ProfesionResponse crearProfesion(ProfesionRequest request) {
		try {
			String database = setProfessionOutputPortInjection(request.getDatabase());
			Profession profession = professionInputPort.create(profesionMapperRest.fromAdapterToDomain(request));
			return profesionMapperRest.fromDomainToAdapterRest(profession, database);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getId(), request.getName(), request.getDescription(), request.getDatabase(),
					e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getId(), request.getName(), request.getDescription(), request.getDatabase(),
					"Error creating profession");
		}
	}

	public ProfesionResponse findProfesion(String database, Integer identification) {
		try {
			String selectedDatabase = setProfessionOutputPortInjection(database);
			Profession profession = professionInputPort.findOne(identification);
			return profesionMapperRest.fromDomainToAdapterRest(profession, selectedDatabase);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), "", "", database, e.getMessage());
		}
	}

	public ProfesionResponse editarProfesion(Integer identification, ProfesionRequest request) {
		try {
			request.setId(String.valueOf(identification));
			String database = setProfessionOutputPortInjection(request.getDatabase());
			Profession profession = professionInputPort.edit(identification,
					profesionMapperRest.fromAdapterToDomain(request));
			return profesionMapperRest.fromDomainToAdapterRest(profession, database);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), request.getName(), request.getDescription(),
					request.getDatabase(), e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), request.getName(), request.getDescription(),
					request.getDatabase(), "Error editing profession");
		}
	}

	public ProfesionResponse eliminarProfesion(String database, Integer identification) {
		try {
			String selectedDatabase = setProfessionOutputPortInjection(database);
			professionInputPort.drop(identification);
			return new ProfesionResponse(String.valueOf(identification), "", "", selectedDatabase, "OK");
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(identification), "", "", database, e.getMessage());
		}
	}

	private ProfesionResponse errorResponse(String id, String name, String description, String database, String status) {
		return new ProfesionResponse(id == null ? "" : id, name == null ? "" : name,
				description == null ? "" : description, database == null ? "" : database, status);
	}
}
