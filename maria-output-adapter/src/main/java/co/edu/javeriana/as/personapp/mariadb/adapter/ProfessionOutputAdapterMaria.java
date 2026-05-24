package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMaria")
@Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

	@Autowired
	private ProfesionRepositoryMaria profesionRepositoryMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	@Override
	public List<Profession> find() {
		log.debug("Into find on Adapter MariaDB for professions");
		return profesionRepositoryMaria.findAll().stream().map(profesionMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}
}
