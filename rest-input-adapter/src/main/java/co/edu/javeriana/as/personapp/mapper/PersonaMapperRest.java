package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		Person person = new Person(
				parseInteger(request.getDni()),
				request.getFirstName(),
				request.getLastName(),
				parseGender(request.getSex()));
		person.updateAge(parseNullableInteger(request.getAge()));
		return person;
	}

	private Integer parseInteger(String value) {
		return Integer.parseInt(value);
	}

	private Integer parseNullableInteger(String value) {
		return value == null || value.isBlank() ? null : Integer.parseInt(value);
	}

	private Gender parseGender(String value) {
		if (value == null) {
			return Gender.OTHER;
		}
		if ("M".equalsIgnoreCase(value) || "MALE".equalsIgnoreCase(value)) {
			return Gender.MALE;
		}
		if ("F".equalsIgnoreCase(value) || "FEMALE".equalsIgnoreCase(value)) {
			return Gender.FEMALE;
		}
		return Gender.OTHER;
	}
		
}
