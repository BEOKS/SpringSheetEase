# Spring Sheet Ease

Spring Sheet Ease is a powerful library designed to bridge the gap between JSON data and Excel files within applications built on the Spring web framework. It offers a straightforward solution for services requiring data presentation in Excel format, simplifying the conversion process by eliminating the need for direct interaction with complex libraries like Apache POI. With just one annotation, developers can easily convert JSON data into downloadable Excel files, enhancing data portability and user experience.

## Features

- **Simplified Excel File Creation:** Generate Excel files from JSON data with a single annotation.
- **Customizable Options:** Control file naming, password protection, and data flattening to suit your needs.
- **No Direct Apache POI Dependency:** Avoid the complexity of directly using Apache POI for Excel file generation.

## Getting Started

### Prerequisites

Ensure you have a Spring-based project ready for integrating Spring Sheet Ease.

### Installation

Add the following dependency to your project's `pom.xml` file to include Spring Sheet Ease:

```xml
<dependency>
  <groupId>com.beoks</groupId>
  <artifactId>spring-web-excel</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Usage

To enable Excel file download functionality, append the `@ExcelDownload` annotation to a controller method as demonstrated below:

```java
@RestController
@RequestMapping("/pet")
public class PetController {
    
    // Responds with JSON data
    @GetMapping
    List<Pet> get(){
        return Pet.getDummy();
    }
    
    // Responds with an Excel file named 'test.xlsx'
    @GetMapping("/excel")
    @ExcelDownload(fileName = "test.xlsx", password = "password123!", useFlatten = false)
    List<Pet> getExcel(){
        return Pet.getDummy();
    }
}
```

### Examples

Explore the provided examples in the [resource directory](./src/test/resources) for insights on implementation and output.

## Configuration Options

Customize the behavior of your Excel file generation with the following options:

| Option      | Description                                                                                                                                                                                                                                         |
|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `filename`  | Sets the Excel file name (must have a `.xlsx` suffix).                                                                                                                                                                                              |
| `password`  | Secures the Excel file with a password. Leave as an empty string (`""`) for no encryption. Default is an empty string.                                                                                                                              |
| `useFlatten`| Determines the structure of nested JSON data in the Excel file. <br>**False:** Displays nested data as a JSON string.<br>**True:** Flattens nested data, placing each item in a separate cell.                                                      |

## Contributing

Contributions to Spring Sheet Ease are welcome! Please refer to our contributing guidelines for detailed information on how you can contribute to the project.

## License

Spring Sheet Ease is open-source software licensed under the [MIT license](LICENSE).

---

This revised README provides a more structured approach to presenting the information, making it easier for users to understand the library's purpose, setup, and configuration. Including sections like "Getting Started," "Configuration Options," and "Contributing" can also encourage community engagement and provide clear guidance on how to use the library effectively.