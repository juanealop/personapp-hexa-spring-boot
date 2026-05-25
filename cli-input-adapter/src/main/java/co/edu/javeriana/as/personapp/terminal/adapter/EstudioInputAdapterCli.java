package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

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
	private EstudioMapperCli estudioMapperCli;

	private StudyInputPort studyInputPort;
	private PersonOutputPort personOutputPort;
	private ProfessionOutputPort professionOutputPort;

	public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			personOutputPort = personOutputPortMaria;
			professionOutputPort = professionOutputPortMaria;
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			personOutputPort = personOutputPortMongo;
			professionOutputPort = professionOutputPortMongo;
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Estudio in Input Adapter");
		studyInputPort.findAll().stream().map(estudioMapperCli::fromDomainToAdapterCli).forEach(System.out::println);
	}

	public void crear(EstudioModelCli estudioModelCli) {
		try {
			Person person = resolvePerson(estudioModelCli.getPersona());
			Profession profession = resolveProfession(estudioModelCli.getProfesion());
			Study study = studyInputPort.create(estudioMapperCli.fromAdapterCliToDomain(estudioModelCli, person, profession));
			System.out.println(estudioMapperCli.fromDomainToAdapterCli(study));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void buscar(Integer personIdentification, Integer professionIdentification) {
		try {
			Study study = studyInputPort.findOne(personIdentification, professionIdentification);
			System.out.println(estudioMapperCli.fromDomainToAdapterCli(study));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void editar(Integer personIdentification, Integer professionIdentification, EstudioModelCli estudioModelCli) {
		try {
			Person person = resolvePerson(estudioModelCli.getPersona());
			Profession profession = resolveProfession(estudioModelCli.getProfesion());
			Study study = studyInputPort.edit(personIdentification, professionIdentification,
					estudioMapperCli.fromAdapterCliToDomain(estudioModelCli, person, profession));
			System.out.println(estudioMapperCli.fromDomainToAdapterCli(study));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void eliminar(Integer personIdentification, Integer professionIdentification) {
		try {
			System.out.println("Eliminado: " + studyInputPort.drop(personIdentification, professionIdentification));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	private Person resolvePerson(Integer personId) throws NoExistException {
		if (personId == null) {
			throw new NoExistException("Person id is required");
		}
		Person person = personOutputPort.findById(personId);
		if (person == null) {
			throw new NoExistException("The person with id " + personId + " does not exist into db");
		}
		return person;
	}

	private Profession resolveProfession(Integer professionId) throws NoExistException {
		if (professionId == null) {
			throw new NoExistException("Profession id is required");
		}
		Profession profession = professionOutputPort.findById(professionId);
		if (profession == null) {
			throw new NoExistException("The profession with id " + professionId + " does not exist into db");
		}
		return profession;
	}
}
