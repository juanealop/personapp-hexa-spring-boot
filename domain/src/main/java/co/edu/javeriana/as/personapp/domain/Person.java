package co.edu.javeriana.as.personapp.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Getter;

@Getter
public class Person {
	private Integer identification;
	private String firstName;
	private String lastName;
	private Gender gender;
	private Integer age;
	private List<Phone> phoneNumbers;
	private List<Study> studies;

	public Person() {
		this.phoneNumbers = new ArrayList<>();
		this.studies = new ArrayList<>();
	}

	public Person(Integer identification, String firstName, String lastName, Gender gender) {
		this();
		this.identification = Objects.requireNonNull(identification, "identification is required");
		this.firstName = requireText(firstName, "firstName is required");
		this.lastName = requireText(lastName, "lastName is required");
		this.gender = Objects.requireNonNull(gender, "gender is required");
	}

	public void updateBasicInfo(String firstName, String lastName, Gender gender) {
		this.firstName = requireText(firstName, "firstName is required");
		this.lastName = requireText(lastName, "lastName is required");
		this.gender = Objects.requireNonNull(gender, "gender is required");
	}

	public void updateAge(Integer age) {
		if (age != null && age < 0) {
			throw new IllegalArgumentException("age must be greater than or equal to 0");
		}
		this.age = age;
	}

	public void replacePhoneNumbers(List<Phone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers != null ? new ArrayList<>(phoneNumbers) : new ArrayList<>();
	}

	public void replaceStudies(List<Study> studies) {
		this.studies = studies != null ? new ArrayList<>(studies) : new ArrayList<>();
	}

	public void addPhone(Phone phone) {
		if (phone != null) {
			this.phoneNumbers.add(phone);
		}
	}

	public void addStudy(Study study) {
		if (study != null) {
			this.studies.add(study);
		}
	}

	public List<Phone> getPhoneNumbers() {
		return Collections.unmodifiableList(phoneNumbers);
	}

	public List<Study> getStudies() {
		return Collections.unmodifiableList(studies);
	}

	public Boolean isValidAge() {
		return this.age == null || this.age >= 0;
	}

	private String requireText(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}
}
