package ProductService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<Map<String,Object>> exceptionClass(ProductServiceCustomException ex){
        Map map=new HashMap<>();
        map.put("Message",ex.getMessage());
        map.put("Status", HttpStatus.NOT_FOUND);
        map.put("Success",false);

        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductQuantityException.class)
    public ResponseEntity<Map<String,Object>> exceptionClass(ProductQuantityException ex){
        Map map=new HashMap<>();
        map.put("Message",ex.getMessage());
        map.put("Status", HttpStatus.NO_CONTENT);
        map.put("Success",false);

        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }


}
