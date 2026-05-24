package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

	@Autowired
	private TelefonoRepositoryMongo telefonoRepositoryMongo;

	@Autowired
	private TelefonoMapperMongo telefonoMapperMongo;

	@Override
	public List<Phone> find() {
		log.debug("Into find on Adapter MongoDB for phones");
		return telefonoRepositoryMongo.findAll().stream().map(telefonoMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}
}
