# Spring Sheet Ease
> This library in developing stage, we will update this to maven soon

In services run on the Spring web framework, sometimes data must be offered in Excel instead of JSON. This library simplifies the process, allowing the conversion of JSON data to Excel files for download with just one annotation, bypassing the complexity of using tools like Apache POI
# Usage
You need only append `@ExcelDownload` annotation to Controller Method.
```java
@RestController
@RequestMapping("/pet")
public class PetController {
    
    //this will response JSON
    @GetMapping
    List<Pet> get(){
        return Pet.getDummy();
    }
    
    //this will response test.xlsx file
    @GetMapping("/excel")
    @ExcelDownload(fileName = "test.xlsx")
    List<Pet> getExcel(){
        return Pet.getDummy();
    }
}
```
You can see the result in [resource](./src/test/resources) directory