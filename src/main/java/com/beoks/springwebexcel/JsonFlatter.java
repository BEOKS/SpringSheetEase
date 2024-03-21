package com.beoks.springwebexcel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonFlatter {
    public static Map<String, String> flattenJson(Object object) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        return flattenJsonNode(objectMapper.valueToTree(object));
    }
    public static Map<String, String> flattenJson(String jsonStr) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return flattenJsonNode(objectMapper.readTree(jsonStr));
    }

    private static Map<String, String> flattenJsonNode(JsonNode rootNode) {
        Map<String, String> flattenedJsonMap = new LinkedHashMap<>();
        flatten(rootNode, flattenedJsonMap, null);
        return flattenedJsonMap;
    }

    private static void flatten(JsonNode node, Map<String, String> flatMap, String currentPath) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
            String prefix = currentPath != null ? currentPath + "." : "";
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                flatten(entry.getValue(), flatMap, prefix + entry.getKey());
            }
        } else if (node.isArray()) {
            int index = 0;
            for (JsonNode arrayItem : node) {
                flatten(arrayItem, flatMap, currentPath + "[" + index++ + "]");
            }
        } else {
            if (node.isValueNode()) {
                flatMap.put(currentPath, node.asText());
            }
        }
    }
}
