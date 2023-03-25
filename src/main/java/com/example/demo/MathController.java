package com.example.demo;

import com.example.demo.exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {

    private static final String template = "Hello, %s!";
    private static final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        
        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value");
        
        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    @RequestMapping(value = "/subtraction/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double subtraction(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value");

        return convertToDouble(numberOne) - convertToDouble(numberTwo);
    }

    @RequestMapping(value = "/multiplication/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double multiplication(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value");

        return convertToDouble(numberOne) * convertToDouble(numberTwo);
    }

    @RequestMapping(value = "/division/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double division(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value");

        return convertToDouble(numberOne) / convertToDouble(numberTwo);
    }

    @RequestMapping(value = "/average/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double average(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value");

        return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
    }

    @RequestMapping(value = "/square-root/{number}", method = RequestMethod.GET)
    public Double squareRoot(@PathVariable(value = "number") String number) throws Exception {

        if(!isNumeric(number))
            throw new UnsupportedMathOperationException("Please set a numeric value");

        return Math.sqrt(convertToDouble(number));
    }

    private Double convertToDouble(String strNumber) {
        String number = strNumber.replace(",", ".");

        return Double.parseDouble(number);
    }

    private boolean isNumeric(String strNumber) {
        if (Objects.isNull(strNumber)) return false;

        String number = strNumber.replace(",", ".");

        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
