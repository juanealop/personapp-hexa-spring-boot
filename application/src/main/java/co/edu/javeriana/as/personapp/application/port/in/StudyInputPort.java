package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {

	void setPersintence(StudyOutputPort studyPersintence);

	Study create(Study study);

	Study edit(Integer personIdentification, Integer professionIdentification, Study study) throws NoExistException;

	Boolean drop(Integer personIdentification, Integer professionIdentification) throws NoExistException;

	List<Study> findAll();

	Study findOne(Integer personIdentification, Integer professionIdentification) throws NoExistException;

	Integer count();
}
