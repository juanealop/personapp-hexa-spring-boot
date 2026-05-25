package co.edu.javeriana.as.personapp.domain;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Study {
	private Person person;
	private Profession profession;
	private LocalDate graduationDate;
	private String universityName;

	public Study(Person person, Profession profession) {
		this.person = Objects.requireNonNull(person, "person is required");
		this.profession = Objects.requireNonNull(profession, "profession is required");
	}

	public void assignPerson(Person person) {
		this.person = Objects.requireNonNull(person, "person is required");
	}

	public void assignProfession(Profession profession) {
		this.profession = Objects.requireNonNull(profession, "profession is required");
	}

	public void registerGraduation(LocalDate graduationDate) {
		this.graduationDate = graduationDate;
	}

	public void updateUniversityName(String universityName) {
		this.universityName = universityName != null ? universityName : "";
	}
}
