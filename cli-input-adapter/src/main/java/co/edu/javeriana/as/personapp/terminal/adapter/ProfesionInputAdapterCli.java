package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

	private ProfessionInputPort professionInputPort;

	public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Profesion in Input Adapter");
		professionInputPort.findAll().stream().map(profesionMapperCli::fromDomainToAdapterCli).forEach(System.out::println);
	}

	public void crear(ProfesionModelCli profesionModelCli) {
		Profession profession = professionInputPort.create(profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
		System.out.println(profesionMapperCli.fromDomainToAdapterCli(profession));
	}

	public void buscar(Integer identification) {
		try {
			Profession profession = professionInputPort.findOne(identification);
			System.out.println(profesionMapperCli.fromDomainToAdapterCli(profession));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void editar(Integer identification, ProfesionModelCli profesionModelCli) {
		try {
			Profession profession = professionInputPort.edit(identification,
					profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
			System.out.println(profesionMapperCli.fromDomainToAdapterCli(profession));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}

	public void eliminar(Integer identification) {
		try {
			System.out.println("Eliminado: " + professionInputPort.drop(identification));
		} catch (NoExistException e) {
			log.warn(e.getMessage());
		}
	}
}
