package swapnil.meta.utils

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {
  private val dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

  def toFormattedString(date: Date): String = {
    dateFormat.format(date)
  }

  def parse(formattedString: String): Date = {
    dateFormat.parse(formattedString)
  }
}
