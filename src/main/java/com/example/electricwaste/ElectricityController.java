package com.example.electricwaste;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ElectricityController {
    private final ElectricityService electricityService;

    public ElectricityController(ElectricityService electricityService) {
        this.electricityService = electricityService;
    }

    @GetMapping("/appliances")
    public List<ApplianceConfig> getAppliances() {
        return electricityService.getAppliances();
    }

    @PutMapping("/appliances")
    public List<ApplianceConfig> saveAppliances(@RequestBody List<ApplianceConfig> appliances) {
        return electricityService.saveAppliances(appliances);
    }

    @GetMapping("/readings")
    public List<Reading> getReadings() {
        return electricityService.getRecentReadings();
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return electricityService.getSummary();
    }

    @PostMapping("/simulator/tick")
    public List<Reading> generateSample() {
        return electricityService.generateManualCycle();
    }
}

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException exception) {
        return Map.of("error", exception.getMessage());
    }
}
