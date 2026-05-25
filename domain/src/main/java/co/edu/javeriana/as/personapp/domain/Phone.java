package co.edu.javeriana.as.personapp.domain;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Phone {
	private String number;
	private String company;
	private Person owner;

	public Phone(String number, String company, Person owner) {
		this.number = requireText(number, "number is required");
		this.company = requireText(company, "company is required");
		this.owner = owner;
	}

	public void updateNumber(String number) {
		this.number = requireText(number, "number is required");
	}

	public void updateCompany(String company) {
		this.company = requireText(company, "company is required");
	}

	public void assignOwner(Person owner) {
		this.owner = owner;
	}

	private String requireText(String value, String message) {
		if (Objects.isNull(value) || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}
}
