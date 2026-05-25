package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

@Mapper
public class PersonaMapperCli {

	public PersonaModelCli fromDomainToAdapterCli(Person person) {
		PersonaModelCli personaModelCli = new PersonaModelCli();
		personaModelCli.setCc(person.getIdentification());
		personaModelCli.setNombre(person.getFirstName());
		personaModelCli.setApellido(person.getLastName());
		personaModelCli.setGenero(person.getGender().toString());
		personaModelCli.setEdad(person.getAge());
		return personaModelCli;
	}

	public Person fromAdapterCliToDomain(PersonaModelCli personaModelCli) {
		Person person = new Person(
				personaModelCli.getCc(),
				personaModelCli.getNombre(),
				personaModelCli.getApellido(),
				parseGender(personaModelCli.getGenero()));
		person.updateAge(personaModelCli.getEdad());
		return person;
	}

	private Gender parseGender(String genero) {
		if (genero == null) {
			return Gender.OTHER;
		}
		if ("M".equalsIgnoreCase(genero) || "MALE".equalsIgnoreCase(genero)) {
			return Gender.MALE;
		}
		if ("F".equalsIgnoreCase(genero) || "FEMALE".equalsIgnoreCase(genero)) {
			return Gender.FEMALE;
		}
		return Gender.OTHER;
	}
}
