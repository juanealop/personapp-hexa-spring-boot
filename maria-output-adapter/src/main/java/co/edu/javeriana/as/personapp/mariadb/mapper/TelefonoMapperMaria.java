package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

@Mapper
public class TelefonoMapperMaria {

	public TelefonoEntity fromDomainToAdapter(Phone phone) {
		TelefonoEntity telefonoEntity = new TelefonoEntity();
		telefonoEntity.setNum(phone.getNumber());
		telefonoEntity.setOper(phone.getCompany());
		telefonoEntity.setDuenio(validateDuenio(phone.getOwner()));
		return telefonoEntity;
	}

	private PersonaEntity validateDuenio(Person owner) {
		if (owner == null) {
			return new PersonaEntity();
		}
		PersonaEntity personaEntity = new PersonaEntity();
		personaEntity.setCc(owner.getIdentification());
		personaEntity.setNombre(owner.getFirstName());
		personaEntity.setApellido(owner.getLastName());
		personaEntity.setGenero(mapGender(owner.getGender()));
		personaEntity.setEdad(owner.getAge());
		return personaEntity;
	}

	public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
		return new Phone(
				telefonoEntity.getNum(),
				telefonoEntity.getOper(),
				validateOwner(telefonoEntity.getDuenio()));
	}

	private Person validateOwner(PersonaEntity duenio) {
		if (duenio == null) {
			return null;
		}
		Person owner = new Person(
				duenio.getCc(),
				duenio.getNombre(),
				duenio.getApellido(),
				mapGender(duenio.getGenero()));
		owner.updateAge(duenio.getEdad());
		return owner;
	}

	private Character mapGender(Gender gender) {
		return gender == Gender.FEMALE ? 'F' : gender == Gender.MALE ? 'M' : ' ';
	}

	private Gender mapGender(Character genero) {
		return genero == 'F' ? Gender.FEMALE : genero == 'M' ? Gender.MALE : Gender.OTHER;
	}
}