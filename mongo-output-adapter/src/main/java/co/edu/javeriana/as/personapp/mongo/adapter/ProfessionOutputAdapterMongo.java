package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

	@Autowired
	private ProfesionRepositoryMongo profesionRepositoryMongo;

	@Autowired
	private ProfesionMapperMongo profesionMapperMongo;

	@Override
	public List<Profession> find() {
		log.debug("Into find on Adapter MongoDB for professions");
		return profesionRepositoryMongo.findAll().stream().map(profesionMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}
}
