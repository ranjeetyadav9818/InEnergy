package com.inenergis.test.controller.validator;

import com.inenergis.controller.validator.CommaSeparatedNumberValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.faces.validator.ValidatorException;


public class CommaSeparatedNumberValidatorTest {

    CommaSeparatedNumberValidator validator = new CommaSeparatedNumberValidator();

    @Test
    public void testJustNumber(){
        validator.validate(null,null,"14354843");
    }

    @Test
    public void testNumberWithCommas(){
        validator.validate(null,null,"14354843,23423,23423");
    }

    @Test
    public void testChar(){
        try{
            validator.validate(null,null,"14354843,23423,a,23423");
            Assertions.fail("Validator exception should be sent");
        }catch (ValidatorException e){

        }
    }
}
