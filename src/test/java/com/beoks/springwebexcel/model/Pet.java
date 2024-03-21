package com.beoks.springwebexcel.model;

import com.beoks.springwebexcel.CustomLocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pet {
    protected String name;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    private PetType type;
    private Owner owner;

    public Pet(String name, LocalDate birthDate, PetType type, Owner owner) {
        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
        this.owner = owner;
    }

    public static List<Pet> getDummy(){
        // Creating Owners
        Owner owner1 = new Owner("John", "123 Street");
        Owner owner2 = new Owner("Sarah", "456 Avenue");
        Owner owner3 = new Owner("Robert", "789 Road");

        // Creating Friends List
        List<Owner> friends = new ArrayList<>();
        friends.add(owner2);
        friends.add(owner3);
        owner1.setFriends(friends);

        // Creating Pets
        Pet pet1 = new Pet("Max", LocalDate.of(2018, 1, 1), PetType.DOG, owner1);
        Pet pet2 = new Pet("Bella", LocalDate.of(2019, 1, 1), PetType.CAT, owner1);
        Pet pet3 = new Pet("Charlie", LocalDate.of(2017, 1, 1), PetType.DOG, owner2);

        // Adding Pets to List
        List<Pet> petList = new ArrayList<>();
        petList.add(pet1);
        petList.add(pet2);
        petList.add(pet3);

        // Printing Pets
        petList.forEach(pet -> System.out.println("Pet: " + pet.getName() + ", Owner: " + pet.getOwner().getName()));
        return petList;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public PetType getType() {
        return type;
    }

    public Owner getOwner() {
        return owner;
    }
}
