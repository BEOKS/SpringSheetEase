
package com.beoks.springwebexcel.controller;

import com.beoks.springwebexcel.model.Pet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/pet")
public class PetRestController {

    @GetMapping
    List<Pet> get() {
        return Pet.getDummy();
    }
}
