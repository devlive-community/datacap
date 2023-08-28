package io.edurt.datacap.captcha;

import io.edurt.datacap.captcha.entity.CaptchaEntity;
import io.edurt.datacap.captcha.entity.ResultEntity;
import io.edurt.datacap.captcha.enums.CalculateEnum;
import io.edurt.datacap.captcha.enums.NumberEnum;

import java.awt.Color;

public class CaptchaUtils
{
    /**
     * Generates a random background color.
     *
     * @return a Color object representing the generated background color
     */
    public static Color getBackgroundColor()
    {
        int color = (int) (Math.random() * 255);
        int _color = (int) (Math.random() * 255);
        int color_ = (int) (Math.random() * 255);
        return new Color(color, _color, color_);
    }

    /**
     * Generates a CaptchaEntity object with random calculation numbers.
     *
     * @return A CaptchaEntity object with randomly generated firstNumber, lastNumber,
     * calc, and number values.
     */
    private static CaptchaEntity generateCalcNumber()
    {
        int firstNumber;
        int lastNumber;
        int calc;
        int number;
        number = (int) (Math.random() * 2);
        firstNumber = (int) (Math.random() * 10);
        lastNumber = (int) (Math.random() * 10);
        calc = (int) (Math.random() * 4);
        /**
         * When the calculation method is subtraction by 1
         * The minuend cannot be less than the subtrahend, when it is less than, the calculation data will be regenerated 1 - 9
         */
        while (calc == 1 && firstNumber - lastNumber < 0) {
            lastNumber = (int) (Math.random() * 10);
        }
        /**
         * When the calculation method is division by 3
         * > The dividend cannot be 0, when it is 0, the calculation data will be regenerated
         */
        while (calc == 3 && lastNumber == 0) {
            lastNumber = (int) (Math.random() * 10);
        }
        /**
         * When the calculation method is division by 3 and the dividend is greater than 0
         * > When the dividend cannot be divisible by the divisor, regenerate the calculation data
         * > When the divisor is equal to 0, add 1 to the divisor
         */
        while (calc == 3 && (firstNumber % lastNumber) > 0) {
            lastNumber = (int) (Math.random() * 10);
            if (lastNumber == 0) {
                lastNumber = 1;
            }
        }
        CaptchaEntity entity = new CaptchaEntity();
        entity.setFirstNumber(firstNumber);
        entity.setLastNumber(lastNumber);
        entity.setCalc(calc);
        entity.setNumber(number);
        return entity;
    }

    /**
     * Generates a CaptchaEntity object representing a calculated image.
     *
     * @return A CaptchaEntity object containing the calculated image.
     */
    public static ResultEntity generateCalculateImage()
    {
        CaptchaEntity entity = generateCalcNumber();
        int calc = entity.getCalc();
        int number = entity.getNumber();
        int firstNumber = entity.getFirstNumber();
        int lastNumber = entity.getLastNumber();
        String _calc = null;
        String _firstNumber = null;
        String _lastNumber = null;
        String equals = null;
        // display 0 Arabic numerals
        if (number == 0) {
            _firstNumber = String.valueOf(firstNumber);
            _lastNumber = String.valueOf(lastNumber);
            _calc = getCalculateEn(calc, calc);
            equals = CalculateEnum.EQUAL.getValueEn();
        }
        // Display 1 Chinese uppercase character
        if (number == 1) {
            for (NumberEnum numberEnum : NumberEnum.values()) {
                if (numberEnum.getValueEn().equals(String.valueOf(firstNumber))) {
                    _firstNumber = numberEnum.getValueZh();
                }
                if (numberEnum.getValueEn().equals(String.valueOf(lastNumber))) {
                    _lastNumber = numberEnum.getValueZh();
                }
            }
            _calc = getCalculateZh(calc, number);
            equals = CalculateEnum.EQUAL.getValueZh();
        }
        String[] result = new String[] {_firstNumber, _calc, _lastNumber, equals, "?"};
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setExpression(result);
        resultEntity.setResult(generateCalculateResult(entity));
        return resultEntity;
    }

    /**
     * Generates the result of a calculation based on the given CaptchaEntity.
     *
     * @param entity the CaptchaEntity containing the calculation details
     * @return the result of the calculation
     */
    private static Integer generateCalculateResult(CaptchaEntity entity)
    {
        int calc = entity.getCalc();
        int firstNumber = entity.getFirstNumber();
        int lastNumber = entity.getLastNumber();
        int result = 0;
        switch (calc) {
            case 0:
                result = firstNumber + lastNumber;
                break;
            case 1:
                result = firstNumber - lastNumber;
                break;
            case 2:
                result = firstNumber * lastNumber;
                break;
            case 3:
                result = firstNumber / lastNumber;
                break;
        }
        return result;
    }

    /**
     * Returns the English value of a given calculation method.
     *
     * @param calc the calculation method (0: addition, 1: subtraction, 2: multiplication, 3: division, 4: equal)
     * @param number the number to perform the calculation on
     * @return the English value of the calculation method
     */
    private static String getCalculateEn(int calc, int number)
    {
        String result = null; // Calculation method: 0 addition, 1 subtraction, 2 multiplication, 3 division, 4 equal
        switch (calc) {
            case 0:
                result = CalculateEnum.ADD.getValueEn();
                break;
            case 1:
                result = CalculateEnum.SUBTRACT.getValueEn();
                break;
            case 2:
                result = CalculateEnum.MULTIPLY.getValueEn();
                break;
            case 3:
                result = CalculateEnum.DIVIDE.getValueEn();
                break;
            case 4:
                result = CalculateEnum.EQUAL.getValueEn();
                break;
        }
        return result;
    }

    /**
     * Retrieves the corresponding Chinese value based on the calculation type and number.
     *
     * @param calc the calculation type
     * @param number the number
     * @return the corresponding Chinese value
     */
    private static String getCalculateZh(int calc, int number)
    {
        String result = null;
        switch (calc) {
            case 0:
                result = CalculateEnum.ADD.getValueZh();
                break;
            case 1:
                result = CalculateEnum.SUBTRACT.getValueZh();
                break;
            case 2:
                result = CalculateEnum.MULTIPLY.getValueZh();
                break;
            case 3:
                result = CalculateEnum.DIVIDE.getValueZh();
                break;
            case 4:
                result = CalculateEnum.EQUAL.getValueZh();
                break;
        }
        return result;
    }
}
