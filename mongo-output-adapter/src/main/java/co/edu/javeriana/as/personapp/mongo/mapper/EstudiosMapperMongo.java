package co.edu.javeriana.as.personapp.mongo.mapper;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import lombok.NonNull;

@Mapper
public class EstudiosMapperMongo {

	public EstudiosDocument fromDomainToAdapter(Study study) {
		EstudiosDocument estudio = new EstudiosDocument();
		estudio.setId(validateId(study.getPerson().getIdentification(), study.getProfession().getIdentification()));
		estudio.setPrimaryPersona(validatePrimaryPersona(study.getPerson()));
		estudio.setPrimaryProfesion(validatePrimaryProfesion(study.getProfession()));
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		return estudio;
	}

	private String validateId(@NonNull Integer identificationPerson, @NonNull Integer identificationProfession) {
		return identificationPerson + "-" + identificationProfession;
	}

	private PersonaDocument validatePrimaryPersona(@NonNull Person person) {
		if (person == null) {
			return new PersonaDocument();
		}
		PersonaDocument personaDocument = new PersonaDocument();
		personaDocument.setId(person.getIdentification());
		personaDocument.setNombre(person.getFirstName());
		personaDocument.setApellido(person.getLastName());
		personaDocument.setGenero(mapGender(person.getGender()));
		personaDocument.setEdad(person.getAge());
		return personaDocument;
	}

	private ProfesionDocument validatePrimaryProfesion(@NonNull Profession profession) {
		if (profession == null) {
			return new ProfesionDocument();
		}
		ProfesionDocument profesionDocument = new ProfesionDocument();
		profesionDocument.setId(profession.getIdentification());
		profesionDocument.setNom(profession.getName());
		profesionDocument.setDes(profession.getDescription());
		return profesionDocument;
	}

	private LocalDate validateFecha(LocalDate graduationDate) {
		return graduationDate != null ? graduationDate : null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosDocument estudiosDocument) {
		Study study = new Study(
				mapPerson(estudiosDocument.getPrimaryPersona()),
				mapProfession(estudiosDocument.getPrimaryProfesion()));
		study.registerGraduation(validateGraduationDate(estudiosDocument.getFecha()));
		study.updateUniversityName(validateUniversityName(estudiosDocument.getUniver()));
		return study;
	}

	private LocalDate validateGraduationDate(LocalDate fecha) {
		return fecha != null ? fecha : null;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}

	private String mapGender(Gender gender) {
		return gender == Gender.FEMALE ? "F" : gender == Gender.MALE ? "M" : " ";
	}

	private Gender mapGender(String genero) {
		return "F".equals(genero) ? Gender.FEMALE : "M".equals(genero) ? Gender.MALE : Gender.OTHER;
	}

	private Person mapPerson(PersonaDocument personaDocument) {
		if (personaDocument == null) {
			return new Person();
		}
		Person person = new Person(
				personaDocument.getId(),
				personaDocument.getNombre(),
				personaDocument.getApellido(),
				mapGender(personaDocument.getGenero()));
		person.updateAge(personaDocument.getEdad());
		return person;
	}

	private Profession mapProfession(ProfesionDocument profesionDocument) {
		if (profesionDocument == null) {
			return new Profession(0, "UNKNOWN");
		}
		Profession profession = new Profession(profesionDocument.getId(), profesionDocument.getNom());
		profession.updateDescription(profesionDocument.getDes());
		return profession;
	}
}