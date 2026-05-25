package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

	public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
		TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
		telefonoModelCli.setNumero(phone.getNumber());
		telefonoModelCli.setCompania(phone.getCompany());
		telefonoModelCli.setDuenio(phone.getOwner() != null ? phone.getOwner().getIdentification() : null);
		return telefonoModelCli;
	}

	public Phone fromAdapterCliToDomain(TelefonoModelCli telefonoModelCli, Person owner) {
		return new Phone(telefonoModelCli.getNumero(), telefonoModelCli.getCompania(), owner);
	}
}
