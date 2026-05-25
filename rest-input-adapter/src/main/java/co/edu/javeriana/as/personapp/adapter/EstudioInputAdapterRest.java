package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private EstudioMapperRest estudioMapperRest;

	private StudyInputPort studyInputPort;
	private PersonOutputPort personOutputPort;
	private ProfessionOutputPort professionOutputPort;

	private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			personOutputPort = personOutputPortMaria;
			professionOutputPort = professionOutputPortMaria;
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			personOutputPort = personOutputPortMongo;
			professionOutputPort = professionOutputPortMongo;
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Invalid database option: " + dbOption);
	}

	public List<EstudioResponse> historial(String database) {
		log.info("Into historial Estudio in Input Adapter REST");
		try {
			String selectedDatabase = setStudyOutputPortInjection(database);
			return studyInputPort.findAll().stream().map(study -> estudioMapperRest.fromDomainToAdapterRest(study, selectedDatabase))
					.collect(Collectors.toList());
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public EstudioResponse crearEstudio(EstudioRequest request) {
		try {
			String database = setStudyOutputPortInjection(request.getDatabase());
			Person person = resolvePerson(request.getPersonId());
			Profession profession = resolveProfession(request.getProfessionId());
			Study study = studyInputPort.create(estudioMapperRest.fromAdapterToDomain(request, person, profession));
			return estudioMapperRest.fromDomainToAdapterRest(study, database);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getPersonId(), request.getProfessionId(), request.getGraduationDate(),
					request.getUniversityName(), request.getDatabase(), e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getPersonId(), request.getProfessionId(), request.getGraduationDate(),
					request.getUniversityName(), request.getDatabase(), "Error creating study");
		}
	}

	public EstudioResponse findEstudio(String database, Integer personIdentification, Integer professionIdentification) {
		try {
			String selectedDatabase = setStudyOutputPortInjection(database);
			Study study = studyInputPort.findOne(personIdentification, professionIdentification);
			return estudioMapperRest.fromDomainToAdapterRest(study, selectedDatabase);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(personIdentification), String.valueOf(professionIdentification), "", "",
					database, e.getMessage());
		}
	}

	public EstudioResponse editarEstudio(Integer personIdentification, Integer professionIdentification,
			EstudioRequest request) {
		try {
			String database = setStudyOutputPortInjection(request.getDatabase());
			Person person = resolvePerson(request.getPersonId());
			Profession profession = resolveProfession(request.getProfessionId());
			Study study = studyInputPort.edit(personIdentification, professionIdentification,
					estudioMapperRest.fromAdapterToDomain(request, person, profession));
			return estudioMapperRest.fromDomainToAdapterRest(study, database);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getPersonId(), request.getProfessionId(), request.getGraduationDate(),
					request.getUniversityName(), request.getDatabase(), e.getMessage());
		} catch (RuntimeException e) {
			log.warn(e.getMessage());
			return errorResponse(request.getPersonId(), request.getProfessionId(), request.getGraduationDate(),
					request.getUniversityName(), request.getDatabase(), "Error editing study");
		}
	}

	public EstudioResponse eliminarEstudio(String database, Integer personIdentification, Integer professionIdentification) {
		try {
			String selectedDatabase = setStudyOutputPortInjection(database);
			studyInputPort.drop(personIdentification, professionIdentification);
			return new EstudioResponse(String.valueOf(personIdentification), String.valueOf(professionIdentification), "",
					"", selectedDatabase, "OK");
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return errorResponse(String.valueOf(personIdentification), String.valueOf(professionIdentification), "", "",
					database, e.getMessage());
		}
	}

	private Person resolvePerson(String personId) throws NoExistException {
		if (personId == null || personId.isBlank()) {
			throw new NoExistException("Person id is required");
		}
		Person person = personOutputPort.findById(Integer.parseInt(personId));
		if (person == null) {
			throw new NoExistException("The person with id " + personId + " does not exist into db");
		}
		return person;
	}

	private Profession resolveProfession(String professionId) throws NoExistException {
		if (professionId == null || professionId.isBlank()) {
			throw new NoExistException("Profession id is required");
		}
		Profession profession = professionOutputPort.findById(Integer.parseInt(professionId));
		if (profession == null) {
			throw new NoExistException("The profession with id " + professionId + " does not exist into db");
		}
		return profession;
	}

	private EstudioResponse errorResponse(String personId, String professionId, String graduationDate, String universityName,
			String database, String status) {
		return new EstudioResponse(personId == null ? "" : personId, professionId == null ? "" : professionId,
				graduationDate == null ? "" : graduationDate, universityName == null ? "" : universityName,
				database == null ? "" : database, status);
	}
}
