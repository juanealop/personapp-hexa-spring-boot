package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.domain.Profession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

	private ProfessionOutputPort professionPersintence;

	public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersintence) {
		this.professionPersintence = professionPersintence;
	}

	@Override
	public void setPersintence(ProfessionOutputPort professionPersintence) {
		this.professionPersintence = professionPersintence;
	}

	@Override
	public List<Profession> findAll() {
		log.info("Output: " + professionPersintence.getClass());
		return professionPersintence.find();
	}
}
