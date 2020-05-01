/*
basics.scala

Basic epidemic modelling examples

*/

object Basics {

  def main(args: Array[String]): Unit = {
    println(breeze.stats.distributions.Poisson(10).sample(5))
  }

}
