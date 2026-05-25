package co.edu.javeriana.as.personapp.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Profession {
	private Integer identification;
	private String name;
	private String description;
	private List<Study> studies;

	public Profession(Integer identification, String name) {
		this.identification = Objects.requireNonNull(identification, "identification is required");
		this.name = requireText(name, "name is required");
		this.description = "";
		this.studies = new ArrayList<>();
	}

	public void rename(String name) {
		this.name = requireText(name, "name is required");
	}

	public void updateDescription(String description) {
		this.description = description != null ? description : "";
	}

	public void replaceStudies(List<Study> studies) {
		this.studies = studies != null ? new ArrayList<>(studies) : new ArrayList<>();
	}

	public void addStudy(Study study) {
		if (study != null) {
			if (this.studies == null) {
				this.studies = new ArrayList<>();
			}
			this.studies.add(study);
		}
	}

	public List<Study> getStudies() {
		if (studies == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(studies);
	}

	private String requireText(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}
}
