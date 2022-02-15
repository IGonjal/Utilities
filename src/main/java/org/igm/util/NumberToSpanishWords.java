package org.igm.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * This class is used to convert numbers to spanish. Please note than
 * in europe the convention is that 1000 millions are not a billion but
 * one thousand millions. A european billion is equivalent to a million
 * millions, or an american trillion. When in the code is written
 * "quintillions" it is referred to the magnitude of hundreds of european
 * trillions.
 */
public class NumberToSpanishWords {

  //Public
  public static final String CONJUNCTION = " con ";
  public static final String DECIMAL_SEPARATOR = ".";

  //Private
  private static final String SCAPE = "\\";
  private static final String ZERO = "cero";
  private static final String MINUS = "menos";
  private static final String FORMAT_MASK = "000000000000000000000";

  // the position 0 is never visited but is added for making numbers match with its value
  private static final String[] NUM_NAMES = {
      "", "un", "dos", "tres", "cuatro", "cinco",
      "seis", "siete", "ocho", "nueve",
      "diez", "once", "doce", "trece", "catorce",
      "quince", "diecis\u00E9is", "diecisiete", "dieciocho", "diecinueve", "veinte",
      "veintiun", "veintid\u00F3s", "veintitr\u00E9s", "veinticuatro", "veinticinco",
      "veintis\u00E9is", "veintisiete", "veintiocho", "veintinueve"};

  private static final String[] TENS_NAMES = {
      "", "diez", "veinte", "treinta", "cuarenta", "cincuenta",
      "sesenta", "setenta", "ochenta", "noventa", "ciento"};

  private static final String[] HUNDREDS_NAMES = {
      "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos",
      "seiscientos", "setecientos", "ochocientos", "novecientos"};


  private static final String[] POWER_NAMES = {
      "mil", "millones", "mil", "billones", "mil", "trillones"};


  private static final String[] SINGLE_POWER_NAMES = {
      "mil", "un mill\u00F3n", "mil", "un bill\u00F3n", "mil", "un trill\u00F3n"};

  private NumberToSpanishWords(){
  }

  private static String convertNumberDifferentToZero(final String _number) {
    final StringBuilder ret = new StringBuilder();

    BigDecimal number = new BigDecimal(_number);
    boolean isNegative = number.compareTo(BigDecimal.ZERO) < 0;
    if (isNegative) {
      number = number.multiply(BigDecimal.valueOf(-1l));
    }

    final String maskedNumber = new DecimalFormat(FORMAT_MASK).format(number);

    // XXXnnnnnnnnnnnnnnnnnn  (American quintillions, european trillions)
    final int quintillions = Integer.parseInt(maskedNumber.substring(0, 3));
    // nnnXXXnnnnnnnnnnnnnnn (European thousands of billions)
    final int quadrillions = Integer.parseInt(maskedNumber.substring(3, 6));
    // nnnnnnXXXnnnnnnnnnnnn
    final int trillions = Integer.parseInt(maskedNumber.substring(6, 9));
    // nnnnnnnnnXXXnnnnnnnnn
    final int billions = Integer.parseInt(maskedNumber.substring(9, 12));
    // nnnnnnnnnnnnXXXnnnnnn
    final int millions = Integer.parseInt(maskedNumber.substring(12, 15));
    // nnnnnnnnnnnnnnnXXXnnn
    final int thousands = Integer.parseInt(maskedNumber.substring(15, 18));
    // nnnnnnnnnnnnnnnnnnXXX
    final int hundreds = Integer.parseInt(maskedNumber.substring(18, 21));

    final String result = new StringBuilder()
        .append(convertPower(quintillions, 5)).append(' ')
        .append(convertPowerThousands(quadrillions, 4, trillions)).append(' ')
        .append(convertPower(trillions, 3)).append(' ')
        .append(convertPowerThousands(billions, 2, millions)).append(' ')
        .append(convertPower(millions, 1)).append(' ')
        .append(convertPower(thousands, 0)).append(' ')
        .append(convertLessThanOneThousand(hundreds))
        .toString();

    if (isNegative) {
      ret.append(MINUS).append(' ');
    }

    // remove extra spaces
    ret.append(result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ").trim());

    return ret.toString();
  }

  private static String convertPowerGeneralCase(final int _number,
      final int _power, final int lesserPoweredNumber) {
    final StringBuilder ret = new StringBuilder();
    if (_number != 0) {
      ret.append(convertLessThanOneThousand(_number))
          .append(' ')
          .append(POWER_NAMES[_power]);
      if (lesserPoweredNumber == 0) {
        ret.append(" ")
            .append(POWER_NAMES[_power - 1]);
      }
    }
    return ret.toString();
  }

  private static String convertLessThanOneThousand(final int _number) {
    final StringBuilder ret = new StringBuilder();
    if (_number == 100) {
      ret.append("cien");
    } else {
      ret.append(HUNDREDS_NAMES[_number / 100])
          .append(' ')
          .append(convertLessThanOneHundred(_number % 100));
    }
    return ret.toString();
  }

  private static String convertLessThanOneHundred(final int _number) {
    final StringBuilder ret = new StringBuilder();

    if (_number < 30) {
      ret.append(" ").append(NUM_NAMES[_number]);
    } else {
      ret.append(" ").append(TENS_NAMES[_number / 10]);
      final String num = NUM_NAMES[_number % 10];
      if (!"".equals(num)) {
        ret.append(" y ").append(NUM_NAMES[_number % 10]);
      }
    }
    return ret.toString();
  }

  /*
   * This method takes care of european millions, billions, trillions, etc. Is important to
   * know that doesn't convert thousands of millions, as in english one hundred millions is
   * une billion but in spanish, one hundred millions are one hundred millions
   */
  private static String convertPower(final int _number,
      final int _power) {
    return _number == 1
        ? SINGLE_POWER_NAMES[_power]
        : convertPowerGeneralCase(_number, _power, -1);
  }

  /*
   * This method takes care of the hundreds of millions that in european notation are not billions
   */
  private static String convertPowerThousands(final int _number, final int _power,
      final int lesserNumber) {
    StringBuilder ret = new StringBuilder();

    if (_number == 1) {
      ret.append(SINGLE_POWER_NAMES[_power]);
      ret.append(" ");
      if (lesserNumber <= 0) {
        ret.append(POWER_NAMES[_power -1]);
      }
    } else {
      ret.append(convertPowerGeneralCase(_number, _power, lesserNumber));
    }
    return ret.toString();
  }

  /**
   * This method parses the two digits located at left-most position.
   *
   * @param decimal
   * @return
   */
  private static String parseDecimalPart(String decimal) {

    decimal = decimal.substring(0, decimal.length() < 2 ? decimal.length() : 2);

    Integer retValue = Integer.valueOf(decimal);
    String concat = "";
    if (retValue < 10) {
      if (!decimal.startsWith("0")) {
        retValue *= 10;
      } else {
        concat = ZERO + " ";
      }
    }

    if (retValue > 0) {
      return concat + convertFormattedString(retValue.toString());
    }
    return "";
  }

  private static String convertFormattedString(final String _number) {
    final StringBuilder ret = new StringBuilder();
    //Is big decimal as the numer could be really big
    if (new BigDecimal(_number).compareTo(BigDecimal.ZERO) == 0) {
      return ZERO;
    }
    ret.append(convertNumberDifferentToZero(_number));
    if (_number.endsWith("1") && !_number.endsWith("11")) {
      ret.append("o");
    }
    return ret.toString();
  }

  /**
   * <p>
   * This method converts a number contained on a string to  words in spanish. For example, 1 will
   * be translated to "UNO", and 1.23 to "UNO CON VEINTITRÉS". The decission beyond receiving the
   * number as String is that some  primitive implementations of big numbers in Java depends on
   * binary fractions which would lead to errors on translating numbers
   * <br/>
   * It's important to say that any digit beyond the second on decimals will be truncated so this
   * number: 1.00999999, will be considered as 1, and translated as "UNO", and that 1.109 will be
   * translated as "UNO CON DIEZ" other examples:
   * <br/>
   * 10.9999 -> 10.99 -> DIEZ CON NOVENTA Y NUEVE<br/>
   * 16.1 -> 16.10 -> DIECISÉIS CON DIEZ CON diez<br/>
   * Another important detail is that "1,01" will be translated as "UNO CON CERO UNO" in order to
   * avoid ambiguity. For negative numbers, these are some examples:
   * <br/>
   * -00 -> CERO <br/>
   * -11 -> MENOS ONCE <br/>
   * -0.5 -> MENOS CERO CON CINCO <br/>
   * It's also a major fact to know that the american billions aren't the same than european ones.
   * This code will translate the number 1000000000 as "MIL MILLONES" and not as a billion, the
   * same standard is followed for billions and trillions.<br/>
   * The Biggest number able to be parsed
   * will be:<br/>
   * 999999999999999999999.99, "NOVECIENTOS NOVENTA Y NUEVE TRILLONES NOVECIENTOS NOVENTA Y NUEVE 
   * MIL NOVECIENTOS NOVENTA Y NUEVE BILLONES NOVECIENTOS NOVENTA Y NUEVE MIL NOVECIENTOS NOVENTA 
   * Y NUEVE MILLONES NOVECIENTOS NOVENTA Y NUEVE MIL NOVECIENTOS NOVENTA Y NUEVE CON NOVENTA Y 
   * NUEVE<br/>
   * </p>
   * @param _number the number to be parsed
   * @return the number parsed as words
   */
  public static String convert(final String _number) {
    StringBuilder stringified = new StringBuilder();

    if (!isStringAcceptedAsNumber(_number)) {
      throw new NumberFormatException("the number format is not accepted");
    }
    String[] parts = _number.split(SCAPE + DECIMAL_SEPARATOR);
    int numberOfZeros = FORMAT_MASK.length() - parts[0].length();

    StringBuilder formattedString = new StringBuilder();
    if(parts[0].startsWith("-")) {
      formattedString.append("-");
      formattedString.append(new String(new char[numberOfZeros]).replace('\0', '0'));
      formattedString.append(parts[0].substring(1));
    } else {
      formattedString.append(new String(new char[numberOfZeros]).replace('\0', '0'));
      formattedString.append(parts[0]);
    }

    stringified.append(convertFormattedString(formattedString.toString()));

    if (parts.length > 1) {
      if (parts[1].length() > 2) {
        parts[1] = parts[1].substring(0, 2);
      }
      String decimal = parseDecimalPart(parts[1]);
      if (!decimal.isEmpty()) {
        stringified.append(CONJUNCTION).append(decimal);
      }
    }
    return stringified.toString().toUpperCase(Locale.getDefault());
  }

  /**
   * This method returns true or false depending on wether a number can be parsed by this class or
   * not. The number must have one of these formats 0,00 - 0 - 0,0000000 And it's integer part
   * cannot exceed from 21 characters so 999999999999999999999,99 will be the last number eligible
   * to be parsed
   *
   * @param _number
   * @return true if the string is a number susceptible to be parsed
   */
  public static boolean isStringAcceptedAsNumber(final String _number) {
    if( _number.matches("[-]?[0-9]+([" + SCAPE + DECIMAL_SEPARATOR + "]([0-9]+))?")) {
      String numberAux = _number;
      if (_number.contains(DECIMAL_SEPARATOR)) {
        numberAux = _number.split(SCAPE + DECIMAL_SEPARATOR)[0];
      }
      return numberAux.length() <= FORMAT_MASK.length();
    }
    return false;
  }
}
