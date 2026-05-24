package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;

@Mapper
public class EstudioMapperCli {

	public EstudioModelCli fromDomainToAdapterCli(Study study) {
		EstudioModelCli estudioModelCli = new EstudioModelCli();
		estudioModelCli.setPersona(study.getPerson() != null ? study.getPerson().getIdentification() : null);
		estudioModelCli.setProfesion(study.getProfession() != null ? study.getProfession().getIdentification() : null);
		estudioModelCli.setFechaGraduacion(study.getGraduationDate());
		estudioModelCli.setUniversidad(study.getUniversityName());
		return estudioModelCli;
	}
}
