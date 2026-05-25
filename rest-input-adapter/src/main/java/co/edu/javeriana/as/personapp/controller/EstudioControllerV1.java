package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {

	@Autowired
	private EstudioInputAdapterRest estudioInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EstudioResponse> estudios(@PathVariable String database) {
		log.info("Into estudios REST API");
		return estudioInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse crearEstudio(@RequestBody EstudioRequest request) {
		log.info("Into create study REST API");
		return estudioInputAdapterRest.crearEstudio(request);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse estudio(@PathVariable String database, @PathVariable Integer personId,
			@PathVariable Integer professionId) {
		log.info("Into findOne study REST API");
		return estudioInputAdapterRest.findEstudio(database.toUpperCase(), personId, professionId);
	}

	@ResponseBody
	@PutMapping(path = "/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse editarEstudio(@PathVariable Integer personId, @PathVariable Integer professionId,
			@RequestBody EstudioRequest request) {
		log.info("Into edit study REST API");
		return estudioInputAdapterRest.editarEstudio(personId, professionId, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse eliminarEstudio(@PathVariable String database, @PathVariable Integer personId,
			@PathVariable Integer professionId) {
		log.info("Into delete study REST API");
		return estudioInputAdapterRest.eliminarEstudio(database.toUpperCase(), personId, professionId);
	}
}
