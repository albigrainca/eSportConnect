package fr.uha.grainca.esc.ui.event

import java.text.SimpleDateFormat
import java.util.Date

class UIConverter {

    companion object {
        fun convert(date: Date): String {
            val sdf = SimpleDateFormat("dd/MMM/yyyy")
            return sdf.format(date)
        }

        fun secondConvert(date: Date): String {
            val sdf = SimpleDateFormat("dd MMM yyyy")
            return sdf.format(date)
        }
    }

}