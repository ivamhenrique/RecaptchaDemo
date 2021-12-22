package br.com.henriktech.recaptchademo

object CardUtil {

    /* Método responsável por verificar se o número PAN de um cartão é válido.
    * Tal método é baseado no algoritmo de Luhn para verificar o dígito verificador.
    * @param pan String
    * @return Boolean (True ou False)
    */
    fun isValidPan(pan: String): Boolean {
        return if (isValidPanLength(pan)) {
            sumCheck(pan) % 10 == 0
        } else {
            false
        }
    }

    /* Método responsável por verificar se o comprimento do número PAN de um cartão é válido.
    * @param pan String
    * @return Boolean (True ou False)
    */
    private fun isValidPanLength(pan: String): Boolean {
        if (isNumeric(pan)) {
            return pan.length in 12..19
        }
        return false
    }

    private fun sumCheck(pan: String): Int {
        val reverse = pan.reversed()
        val even = StringBuffer()
        val odd = StringBuffer()

        //Luhn's sum check
        var sum = 0
        for (i in 1 until reverse.length step 2) {
            val mult = if (reverse[i].digitToInt() * 2 > 9)
                reverse[i].digitToInt() * 2 - 9
            else reverse[i].digitToInt() * 2
            even.append(mult)
            sum += mult
        }
        for (j in reverse.indices step 2) {
            odd.append(reverse[j])
            sum += reverse[j].digitToInt()
        }
        return sum
    }

    private fun isNumeric(msg: String): Boolean {
        return if (msg.isEmpty()) false else msg.all { Character.isDigit(it) }
    }
}