package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;

@Mapper
public class EstudiosMapperMaria {

	public EstudiosEntity fromDomainToAdapter(Study study) {
		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
		estudioPK.setCcPer(study.getPerson().getIdentification());
		estudioPK.setIdProf(study.getProfession().getIdentification());
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosPK(estudioPK);
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		return estudio;
	}

	private Date validateFecha(LocalDate graduationDate) {
		return graduationDate != null
				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
				: null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		Study study = new Study(
				mapPerson(estudiosEntity.getPersona()),
				mapProfession(estudiosEntity.getProfesion()));
		study.registerGraduation(validateGraduationDate(estudiosEntity.getFecha()));
		study.updateUniversityName(validateUniversityName(estudiosEntity.getUniver()));
		return study;
	}

	private LocalDate validateGraduationDate(Date fecha) {
		return fecha != null ? fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}

	private Person mapPerson(PersonaEntity personaEntity) {
		if (personaEntity == null) {
			return new Person();
		}
		Person person = new Person(
				personaEntity.getCc(),
				personaEntity.getNombre(),
				personaEntity.getApellido(),
				mapGender(personaEntity.getGenero()));
		person.updateAge(personaEntity.getEdad());
		return person;
	}

	private Profession mapProfession(ProfesionEntity profesionEntity) {
		if (profesionEntity == null) {
			return new Profession(0, "UNKNOWN");
		}
		Profession profession = new Profession(profesionEntity.getId(), profesionEntity.getNom());
		profession.updateDescription(profesionEntity.getDes());
		return profession;
	}

	private Gender mapGender(Character genero) {
		return genero == 'F' ? Gender.FEMALE : genero == 'M' ? Gender.MALE : Gender.OTHER;
	}
}