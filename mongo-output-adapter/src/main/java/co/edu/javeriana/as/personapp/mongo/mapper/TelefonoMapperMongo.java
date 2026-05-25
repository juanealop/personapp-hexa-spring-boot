package co.edu.javeriana.as.personapp.mongo.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

@Mapper
public class TelefonoMapperMongo {

	public TelefonoDocument fromDomainToAdapter(Phone phone) {
		TelefonoDocument telefonoDocument = new TelefonoDocument();
		telefonoDocument.setId(phone.getNumber());
		telefonoDocument.setOper(phone.getCompany());
		telefonoDocument.setPrimaryDuenio(validateDuenio(phone.getOwner()));
		return telefonoDocument;
	}

	private PersonaDocument validateDuenio(Person owner) {
		if (owner == null) {
			return new PersonaDocument();
		}
		PersonaDocument personaDocument = new PersonaDocument();
		personaDocument.setId(owner.getIdentification());
		personaDocument.setNombre(owner.getFirstName());
		personaDocument.setApellido(owner.getLastName());
		personaDocument.setGenero(mapGender(owner.getGender()));
		personaDocument.setEdad(owner.getAge());
		return personaDocument;
	}

	public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
		return new Phone(
				telefonoDocument.getId(),
				telefonoDocument.getOper(),
				validateOwner(telefonoDocument.getPrimaryDuenio()));
	}

	private Person validateOwner(PersonaDocument duenio) {
		if (duenio == null) {
			return null;
		}
		Person owner = new Person(
				duenio.getId(),
				duenio.getNombre(),
				duenio.getApellido(),
				mapGender(duenio.getGenero()));
		owner.updateAge(duenio.getEdad());
		return owner;
	}

	private String mapGender(Gender gender) {
		return gender == Gender.FEMALE ? "F" : gender == Gender.MALE ? "M" : " ";
	}

	private Gender mapGender(String genero) {
		return "F".equals(genero) ? Gender.FEMALE : "M".equals(genero) ? Gender.MALE : Gender.OTHER;
	}
}