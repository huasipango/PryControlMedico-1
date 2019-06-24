package com.medico.app.web.controllers;

import com.medico.app.web.models.entities.DetalleReceta;
import com.medico.app.web.models.entities.Dosis;
import com.medico.app.web.models.services.IDetalleRecetaService;
import com.medico.app.web.models.services.IDosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/detallereceta")
public class DetalleRecetaController {

    @Autowired
    private IDetalleRecetaService service;

    @Autowired
    private IDosisService dosisService;

    @GetMapping(value = "/create")
    public String create(Model model){
        DetalleReceta detalleReceta = new DetalleReceta();
        model.addAttribute("detalleReceta", detalleReceta);
        return "detallereceta/form";
    }

    @PostMapping(value = "/save")
    public String save(DetalleReceta detalleReceta, Model model){
        try {
            service.save(detalleReceta);
        }catch (Exception ex){
            model.addAttribute("error: ", ex.toString());
        }
        return "redirect:/receta/list";
    }

    @GetMapping(value = "/retrieve/{id}")
    public String retrieve(@PathVariable(value = "id") Integer id, Model model
        , RedirectAttributes redirectAttributes){
        DetalleReceta detalleReceta = service.findById(id);
        List<Dosis> dosis = dosisService.findByIdDetalleReceta(id);
        List<Dosis> dosisTomadas = dosisService.findNotTakenPills(id);
        if(dosisTomadas.size()==0) {
            //service.setDetalleRecetaInactiveStatus(id);
        }
        model.addAttribute("detallereceta", detalleReceta);
        model.addAttribute("listaDosis", dosis);
        return "detallereceta/card";
    }

    @GetMapping(value = "/update/{id}")
    public String update(@PathVariable(value = "id") Integer id, Model model){
        DetalleReceta detalleReceta = service.findById(id);
        model.addAttribute("detallereceta", detalleReceta);
        return "detallereceta/form";
    }

    @GetMapping(value = "/delete")
    public String delete(@PathVariable(value = "id") Integer id, Model model){
        try {
            service.delete(id);
        }catch (Exception ex){
            model.addAttribute("error:", ex.toString());
        }
        return "redirect:/detallereceta/list";
    }

    @GetMapping(value = "/list")
    public String list(Model model){
        List<DetalleReceta> detalles = service.findAll();
        model.addAttribute("lista",detalles);
        return "detallereceta/list";
    }
    
}
