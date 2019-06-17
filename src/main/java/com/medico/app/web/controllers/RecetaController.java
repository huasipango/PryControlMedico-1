package com.medico.app.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.medico.app.web.models.entities.*;
import com.medico.app.web.models.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/receta")
@SessionAttributes({"receta","title"})
public class RecetaController {

	//Servicio => Inyección de dependencias
	@Autowired
	private IRecetaService service;
	@Autowired
	private IDetalleRecetaService detalleservice;

	@Autowired
	private IMedicoService srvMedico;

	@Autowired
	private IPacienteService srvPaciente;

	@Autowired
	private IMedicamentoService srvMedicamento;
		
	@GetMapping(value="/create")
	public String create(Model model) {
		Receta receta = new Receta();
		model.addAttribute("title","Registro de nueva receta");
		model.addAttribute("receta",receta);
		
		List<Paciente> pacientes = srvPaciente.findAll();
		List<Medico> medicos = srvMedico.findAll();
		
		model.addAttribute("pacientes",pacientes);
		model.addAttribute("medicos",medicos);
		
		return "receta/form";
	}
	
	@PostMapping(value="/save")
	public String save(@Valid Receta receta, BindingResult result, 
			Model model, RedirectAttributes message, 
			SessionStatus session) {
		try {
			
			if(result.hasErrors()) {        		
        		return "receta/form";
        	} 
			
        	String msg = receta.getIdreceta() == null ? "La receta ha sido creada." : "La receta ha sido actualizada.";
        	service.save(receta);        	
            session.setComplete();            
            message.addFlashAttribute("success", msg);
		}
		catch(Exception ex){
			message.addFlashAttribute("error", ex.toString());
		}
		return "redirect:/receta/list";
	}
	
	
	@GetMapping(value="/retrieve/{id}")
	public String retrieve(@PathVariable(value="id")Integer id, 
			Model model) {
		Receta receta = service.findById(id);
		List<DetalleReceta> detalles = receta.getDetalles();
		int [] numdosis = new int[detalles.size()];
		int cont=0;
		for(DetalleReceta detalle:detalles){
			List<Dosis> dosis=detalle.getDosis();
			for (Dosis aux:dosis){
				if(aux.getEstado()==1){
					numdosis[cont]++;
				}
			}
			cont++;
		}
		model.addAttribute("receta",receta);
		model.addAttribute("detalles",detalles);
		model.addAttribute("numdosis",numdosis);
		model.addAttribute("title","Informacion Receta");
		return "receta/card";
	}
	
	@GetMapping(value="/update/{id}")
	public String update(@PathVariable(value="id")Integer id,
			Model model) {
		Receta receta = service.findById(id);
		model.addAttribute("receta",receta);
		return "receta/form";
	}
	
	@GetMapping(value="/delete/{id}")
	public String delete(@PathVariable(value="id")Integer id, 
			Model model) {
		try {
			service.delete(id);
		}
		catch(Exception ex){			
		}				
		return "redirect:/receta/list";
	}	
	
	@GetMapping(value="/list")
	public String list(Model model) {
		List<Receta> recetas = service.findAll();
		model.addAttribute("title","Listado de Recetas");
		model.addAttribute("lista", recetas);
		return "receta/list";		
	}
	
	@PostMapping(value="/addDetail", produces="application/json")
	public @ResponseBody List<DetalleReceta> addDetalleReceta(
			@RequestBody DetalleReceta detail, @SessionAttribute(name="receta") Receta receta) {
		if(receta.getDetalles() == null)
		{
			List<DetalleReceta> detalles = new ArrayList<>();
			receta.setDetalles(detalles);
		}
		
		Medicamento medicamento = srvMedicamento.findById(detail.getMedicamentoId());
		detail.setMedicamento(medicamento);
		receta.getDetalles().add(detail);
    	return receta.getDetalles();
    }
	
	
	
	


}
