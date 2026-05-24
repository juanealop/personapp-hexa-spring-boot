package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Phone;

@Port
public interface PhoneInputPort {

	void setPersintence(PhoneOutputPort phonePersintence);

	List<Phone> findAll();
}
