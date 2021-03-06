package views.html

object Attributes {

	def classValue(prop: scala.collection.immutable.Map[String, String]): String = {

		prop.getOrElse("class", "");
	}

	def datas(prop: scala.collection.immutable.Map[String, String]): String = {

		prop.iterator.filter(entry => {

			entry._1.equals("title") ||
			entry._1.startsWith("aria-") ||
			entry._1.startsWith("data-")
		}).map(entry => {

			if(entry._2 == null){

				entry._1
			}else{

				entry._1 + "='" + entry._2 + "'"
			}
		}).mkString(" ");
	}
}
