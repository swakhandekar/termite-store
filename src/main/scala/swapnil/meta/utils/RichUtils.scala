package swapnil.meta.utils

import java.util.Date

object RichUtils {

  implicit class RichString(stringDate: String) {
    def toDate: Date = {
      DateUtils.parse(stringDate)
    }
  }

  implicit class RichDate(date: Date) {
    def toFormattedDateString: String = {
      DateUtils.toFormattedString(date)
    }
  }

}
