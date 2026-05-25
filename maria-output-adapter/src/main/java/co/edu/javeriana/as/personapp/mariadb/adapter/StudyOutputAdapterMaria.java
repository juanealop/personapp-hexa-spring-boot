package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMaria estudiosRepositoryMaria;

	@Autowired
	private EstudiosMapperMaria estudiosMapperMaria;

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MariaDB for studies");
		return estudiosMapperMaria
				.fromAdapterToDomain(estudiosRepositoryMaria.save(estudiosMapperMaria.fromDomainToAdapter(study)));
	}

	@Override
	public Boolean delete(Integer personIdentification, Integer professionIdentification) {
		log.debug("Into delete on Adapter MariaDB for studies");
		EstudiosEntityPK studyPk = new EstudiosEntityPK(professionIdentification, personIdentification);
		estudiosRepositoryMaria.deleteById(studyPk);
		return estudiosRepositoryMaria.findById(studyPk).isEmpty();
	}

	@Override
	public List<Study> find() {
		log.debug("Into find on Adapter MariaDB for studies");
		return estudiosRepositoryMaria.findAll().stream().map(estudiosMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer personIdentification, Integer professionIdentification) {
		log.debug("Into findById on Adapter MariaDB for studies");
		EstudiosEntityPK studyPk = new EstudiosEntityPK(professionIdentification, personIdentification);
		if (estudiosRepositoryMaria.findById(studyPk).isEmpty()) {
			return null;
		}
		return estudiosMapperMaria.fromAdapterToDomain(estudiosRepositoryMaria.findById(studyPk).get());
	}
}
