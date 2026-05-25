package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMongo estudiosRepositoryMongo;

	@Autowired
	private EstudiosMapperMongo estudiosMapperMongo;

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MongoDB for studies");
		return estudiosMapperMongo
				.fromAdapterToDomain(estudiosRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study)));
	}

	@Override
	public Boolean delete(Integer personIdentification, Integer professionIdentification) {
		log.debug("Into delete on Adapter MongoDB for studies");
		String studyId = mapStudyId(personIdentification, professionIdentification);
		estudiosRepositoryMongo.deleteById(studyId);
		return estudiosRepositoryMongo.findById(studyId).isEmpty();
	}

	@Override
	public List<Study> find() {
		log.debug("Into find on Adapter MongoDB for studies");
		return estudiosRepositoryMongo.findAll().stream().map(estudiosMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer personIdentification, Integer professionIdentification) {
		log.debug("Into findById on Adapter MongoDB for studies");
		String studyId = mapStudyId(personIdentification, professionIdentification);
		if (estudiosRepositoryMongo.findById(studyId).isEmpty()) {
			return null;
		}
		return estudiosMapperMongo.fromAdapterToDomain(estudiosRepositoryMongo.findById(studyId).get());
	}

	private String mapStudyId(Integer personIdentification, Integer professionIdentification) {
		return personIdentification + "-" + professionIdentification;
	}
}
