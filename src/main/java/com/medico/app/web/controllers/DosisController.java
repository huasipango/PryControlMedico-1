package com.medico.app.web.controllers;

import com.medico.app.web.models.entities.DetalleReceta;
import com.medico.app.web.models.entities.Dosis;
import com.medico.app.web.models.entities.Receta;
import com.medico.app.web.models.services.IDetalleRecetaService;
import com.medico.app.web.models.services.IDosisService;
import com.medico.app.web.models.services.IRecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value="/dosis")
@SessionAttributes({"dosis","title"})
public class DosisController {

    @Autowired
    private IDosisService service;

    @Autowired
    private IDetalleRecetaService srvDetalleReceta;

    @Autowired
    private IRecetaService srvReceta;

    @GetMapping(value="/create" )
    public String create(Model model){
        Dosis dosis=new Dosis();
        model.addAttribute("dosis",dosis);
        return "dosis/form";
    }
    @PostMapping(value="/save" )
    public String save(Dosis dosis,Model model,
                       RedirectAttributes message){
        try{
            service.save(dosis);
            Receta receta = dosis.getDetalleReceta().getReceta();
            List<Dosis> dosisTomadas = service.findNotTakenPills
                    (dosis.getDetalleReceta().getIddetalleReceta());
            if(dosisTomadas.size()==0) {//Detalle Inactivo
                srvDetalleReceta.setDetalleRecetaInactiveStatus
                        (dosis.getDetalleReceta().getIddetalleReceta());
                message.addFlashAttribute("success", "El detalle ha " +
                        "pasado ha estar inactivo.");
            }
            List<DetalleReceta> detalleRecetas =
                    srvDetalleReceta.findNotTakenDetalles(receta.getIdreceta());
            if(detalleRecetas.size()==0){//Receta Inactiva
                srvReceta.setRecetaInactiveStatus(receta.getIdreceta());
                message.addFlashAttribute("success", "La receta de" +
                        receta.getPaciente().getNombre() + " " + receta.getPaciente().getApellido()
                        + "ha pasado ha estar inactiva.");
            }
        }catch (Exception ex){
            model.addAttribute("error: ",ex.toString());
        }
        return "redirect:/receta/list";//cambio temporal
    }

    @GetMapping(value="/retrieve/{id}" )
    public String retrieve(@PathVariable(value = "id") Integer id,
                           Model model){
        Dosis dosis=service.findById(id);
        model.addAttribute("dosis",dosis);
        return "dosis/card";
    }

    @GetMapping(value="/update/{id}" )
    public String update(@PathVariable(value = "id") Integer id,
                         Model model){
        Dosis dosis=service.findById(id);
        model.addAttribute("title","Actualizar el detalle de la receta");
        model.addAttribute("dosis",dosis);
        return "dosis/form";
    }

    @GetMapping(value="/delete" )
    public String delete(@PathVariable(value = "id") Integer id,
                         Model model){
        try{
            service.delete(id);
        }catch (Exception ex){
            model.addAttribute("error: ",ex.toString());
        }
        return "redirect:/dosis/list";
    }

    @GetMapping(value="/list" )
    public String list(Model model){
        List<Dosis> dosis=service.findAll();
        model.addAttribute("lista",dosis);
        return "dosis/list";
    }
}
