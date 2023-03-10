package com.inenergis.controller.asset;

import com.inenergis.controller.assetTopology.ElementData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementDataTest {


    @InjectMocks
    private DeviceDetailsController deviceDetailsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void placeElementByPosition() {

        int numElements = 9;

        final ElementData elementData0 = ElementData.builder().position(0).label("an element").build();
        elementData0.placeElementByPosition(numElements);

        final ElementData elementData1 = ElementData.builder().position(1).label("an element").build();
        elementData1.placeElementByPosition(numElements);

        final ElementData elementData2 = ElementData.builder().position(2).label("an element").build();
        elementData2.placeElementByPosition(numElements);

        final ElementData elementData3 = ElementData.builder().position(3).label("an element").build();
        elementData3.placeElementByPosition(numElements);

        final ElementData elementData4 = ElementData.builder().position(4).label("an element").build();
        elementData4.placeElementByPosition(numElements);

        final ElementData elementData5 = ElementData.builder().position(5).label("an element").build();
        elementData5.placeElementByPosition(numElements);

        final ElementData elementData6 = ElementData.builder().position(6).label("an element").build();
        elementData6.placeElementByPosition(numElements);

        final ElementData elementData7 = ElementData.builder().position(7).label("an element").build();
        elementData7.placeElementByPosition(numElements);

        final ElementData elementData8 = ElementData.builder().position(8).label("an element").build();
        elementData8.placeElementByPosition(numElements);


        assertEquals(elementData0.getSourceX().intValue(),0);
        assertEquals(elementData1.getSourceX().intValue(),1);
        assertEquals(elementData2.getSourceX().intValue(),2);
        assertEquals(elementData3.getSourceX().intValue(),0);
        assertEquals(elementData4.getSourceX().intValue(),1);
        assertEquals(elementData5.getSourceX().intValue(),2);
        assertEquals(elementData6.getSourceX().intValue(),0);
        assertEquals(elementData7.getSourceX().intValue(),1);
        assertEquals(elementData8.getSourceX().intValue(),2);

        assertEquals(elementData0.getSourceY().intValue(),0);
        assertEquals(elementData1.getSourceY().intValue(),0);
        assertEquals(elementData2.getSourceY().intValue(),0);
        assertEquals(elementData3.getSourceY().intValue(),1);
        assertEquals(elementData4.getSourceY().intValue(),1);
        assertEquals(elementData5.getSourceY().intValue(),1);
        assertEquals(elementData6.getSourceY().intValue(),2);
        assertEquals(elementData7.getSourceY().intValue(),2);
        assertEquals(elementData8.getSourceY().intValue(),2);

    }
}