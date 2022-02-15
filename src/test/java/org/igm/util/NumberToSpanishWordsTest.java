package org.igm.util;


import static org.igm.util.NumberToSpanishWords.convert;
import static org.igm.util.NumberToSpanishWords.isStringAcceptedAsNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.Silent.class)
public class NumberToSpanishWordsTest {

  private static String CONJUNCTION = NumberToSpanishWords.CONJUNCTION.toUpperCase();

  // Please, notice that the variable CONJUNCTION from NumberToSpanishWords has been statically
  // imported along with the convert method
  @Test
  public void oneCipher() {
    String oneCiphers[] = {"cero", "uno", "dos", "tres", "cuatro",
        "cinco", "seis", "siete", "ocho", "nueve"};

    assertEquals(convert("0"), "CERO");
    assertEquals(convert("0.12"), "CERO" + CONJUNCTION + "DOCE");

    for (int i = 0; i < oneCiphers.length; i++) {
      assertEquals(convert(Integer.toString(i)), oneCiphers[i].toUpperCase());
    }
  }

  @Test
  public void someTwoCiphers() {
    String[][] numbers = new String[][]{
        {"diez", "10"},
        {"trece", "13"},
        {"catorce", "14"},
        {"quince", "15"},
        {"veinte", "20"},
        {"veintidós", "22"},
        {"treinta y tres", "33"},
        {"setenta y cinco", "75"},
        {"noventa y nueve", "99"}
    };

    evaluate(numbers);
  }

  @Test
  public void decimals() {
    String[][] numbers = new String[][]{
        {"cero" + CONJUNCTION + "doce", "0.12"},
        {"once", "11.0"},
        {"doce", "12.000"},
        {"trece" + CONJUNCTION + "sesenta y uno", "13.6101"},
        {"catorce", "14.001"},
        {"veintiuno" + CONJUNCTION + "cero siete", "21.07"},
        {"treinta y tres" + CONJUNCTION + "setenta y cinco", "33.750003"},
        {"catorce" + CONJUNCTION + "sesenta", "14.6"},
        {"quince" + CONJUNCTION + "treinta", "15.3000"},
        {"noventa y nueve" + CONJUNCTION + "diez", "99.10"}
    };

    evaluate(numbers);
  }


  @Test
  public void biggerNumbers() {
    String[][] numbers = new String[][]{
        {"cien", "100"},
        {"mil", "1000"},
        {"MIL DOSCIENTOS ONCE" + CONJUNCTION + "ONCE", "1211.11"},
        {"mil cuatrocientos treinta y uno", "1431"},
        {"un millón novecientos treinta y cinco mil "
            + "cuatrocientos veintisiete" + CONJUNCTION + "noventa y tres", "1935427.93"},
        {"un trillón", "1000000000000000000"},
        {"tres trillones ciento siete mil tres billones ciento un mil ciento un millones",
            "3107003101101000000"},
        {"novecientos noventa y nueve trillones", "999000000000000000000"},
        {"mil billones", "000001000000000000000"},
        {"dos mil billones", "000002000000000000000"},
        {"novecientos noventa y nueve mil billones", "999000000000000000"},
        {"novecientos noventa y nueve trillones "
            + "novecientos noventa y nueve mil novecientos noventa y nueve billones "
            + "novecientos noventa y nueve mil novecientos noventa y nueve millones "
            + "novecientos noventa y nueve mil novecientos noventa y nueve"
            + CONJUNCTION + "noventa y nueve", "999999999999999999999.99"}

    };
    evaluate(numbers);
  }

  @Test(expected = NumberFormatException.class)
  public void stringAsANumber() {
    convert("it's me, an error");
  }

  @Test(expected = NumberFormatException.class)
  public void tooLongNumber() {
    convert("1000000000000000000000");
  }

  @Test
  public void negativeNumber() {
    assertEquals("MENOS CIEN",convert("-100"));
    assertEquals("MENOS ONCE",convert("-11"));

    assertEquals("CERO",convert("-00"));
  }

  private void evaluate(String[][] numbers) {
    for (String[] line : numbers) {
      assertEquals(convert(line[1]), line[0].toUpperCase());
    }
  }

  @Test
  public void matchesTest() {
    //Valid numbers
    assertTrue(isStringAcceptedAsNumber("12.00123213"));
    assertTrue(isStringAcceptedAsNumber("-12.00123213"));
    assertTrue(isStringAcceptedAsNumber("12"));
    assertTrue(isStringAcceptedAsNumber("999999999999999999999"));
    assertTrue(isStringAcceptedAsNumber("999999999999999999999.99"));

    //Invalid (is an alphabetic character)
    assertFalse(isStringAcceptedAsNumber("a"));
    //Invalid (There is a comma but no decimal part)
    assertFalse(isStringAcceptedAsNumber("54."));
    //Invalid (The separator is a dot)
    assertFalse(isStringAcceptedAsNumber("724,3"));
    //Valid (Last number to be valid by length)
    assertTrue(isStringAcceptedAsNumber("999999999999999999999"));
    //Invalid (First number to be invalid)
    assertFalse(isStringAcceptedAsNumber("1000000000000000000000"));
    //Invalid (Multiple decimal parts)
    assertFalse(isStringAcceptedAsNumber("5.11.11"));
  }
}
