package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

	@Autowired
	private TelefonoRepositoryMaria telefonoRepositoryMaria;

	@Autowired
	private TelefonoMapperMaria telefonoMapperMaria;

	@Override
	public List<Phone> find() {
		log.debug("Into find on Adapter MariaDB for phones");
		return telefonoRepositoryMaria.findAll().stream().map(telefonoMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}
}
