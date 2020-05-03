/*
basics.scala

Basic epidemic modelling examples

*/

import com.cibo.evilplot._
import com.cibo.evilplot.plot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import com.cibo.evilplot.plot.renderers.PointRenderer
import com.cibo.evilplot.numeric.Point
import com.cibo.evilplot.colors.HTMLNamedColors._

object Basics {

  def main(args: Array[String]): Unit = {

    case class Pop(S: Double, I: Double, R: Double)

    val p0 = Pop(1.0e7, 2.0, 0.0)

    def S2I(beta: Double)(p: Pop): Pop = {
      val si = beta * p.S * p.I
      p.copy(S = p.S - si, I = p.I + si)
    }

    def I2R(gamma: Double)(p: Pop): Pop = {
      val ir = gamma * p.I
      p.copy(I = p.I - ir, R = p.R + ir)
    }

    def update(beta: Double, gamma: Double)(p: Pop): Pop =
      I2R(gamma)(S2I(beta)(p))

    val beta = 5.0e-8; val gamma = 0.1
    val pop = Stream.iterate(p0)(update(beta, gamma))

    val spoints = pop.zipWithIndex.
      map{case (pt, t) => Point(t, pt.S)}.
      take(100).toList
    val ipoints = pop.zipWithIndex.
      map{case (pt, t) => Point(t, pt.I)}.
      take(100).toList
    val rpoints = pop.zipWithIndex.
      map{case (pt, t) => Point(t, pt.R)}.
      take(100).toList

    val plt = Overlay(
      ScatterPlot.series(spoints, "S", dodgerBlue),
      ScatterPlot.series(ipoints, "I", crimson),
      ScatterPlot.series(rpoints, "R", green)
    ).xAxis()
      .yAxis()
      .overlayLegend()
      .frame()
      .title("SIR model")
      .xLabel("time")
      .yLabel("Number of individuals")
      .render()
    displayPlot(plt)
  }

}
