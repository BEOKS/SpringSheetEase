# Spring Sheet Ease
In services run on the Spring web framework, sometimes data must be offered in Excel instead of JSON. This library simplifies the process, allowing the conversion of JSON data to Excel files for download with just one annotation, bypassing the complexity of using tools like Apache POI
# Usage
```xml
<dependency>
  <groupId>com.beoks</groupId>
  <artifactId>spring-web-excel</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
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
    @ExcelDownload(fileName = "test.xlsx",password = "excelFilePassword", useFlatten = false)
    List<Pet> getExcel(){
        return Pet.getDummy();
    }
}
```
You can see the result in [resource](./src/test/resources) directory

## Option
| Option     | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| filename   | Excel Filename, suffix must be .xlsx                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| password   | Excel file password, if use empty string encryption not applied, default is empty string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| useFlatten | An option to either flatten nested JSON data to place each item in a separate cell or to display nested data as a JSON string<br><br>When useFlatten is False<br>"Max"	"2018-01-01"	"DOG"	{"name":"John","address":"123 Street","friends":[{"name":"Sarah","address":"456 Avenue","friends":null},{"name":"Robert","address":"789 Road","friends":null}]}	<br><br>When useFlatten is True<br>Max	2018-01-01	DOG	John	123 Street	Sarah	456 Avenue	null	Robert	789 Road	null	<br>Bella	2019-01-01	CAT	John	123 Street	Sarah	456 Avenue	null	Robert	789 Road	null	<br>Charlie	2017-01-01	DOG	Sarah	456 Avenue	null<br> |