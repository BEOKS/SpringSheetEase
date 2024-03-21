package com.beoks.springwebexcel;

import com.beoks.springwebexcel.model.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class JsonFlatterTest {

    @Test
    void test() throws Exception {
        List<String> dummy = List.of("tom","choco","bori");
        System.out.println("dummy = " + new ObjectMapper().writeValueAsString(dummy));
        Map<String, String> stringObjectMap = JsonFlatter.flattenJson(dummy);
        System.out.println("stringObjectMap = " + new ObjectMapper().writeValueAsString(stringObjectMap));
    }
}
