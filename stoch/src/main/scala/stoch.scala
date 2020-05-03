/*
stoch.scala

Illustrate simulation of an SIR model

*/

object SIR {

  import smfsb._
  import breeze.linalg._
  import breeze.numerics._


  import com.cibo.evilplot._
  import com.cibo.evilplot.plot._
  import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
  import com.cibo.evilplot.numeric.Point
  import com.cibo.evilplot.colors.Color

  def plotTs[S: State](ts: Ts[S], title: String = ""): Unit = {
    val times = (ts map (_._1)).toArray
    val idx = 0 until ts(0)._2.toDvd.length
    val states = ts map (_._2)
    val stateArrays = idx.map(i => (states map (_.toDvd.apply(i))).toArray)
    val colors = Color.getGradientSeq(idx.length)
    val plts = idx map (i => {
      val pop = stateArrays(i)
      val points = (times zip pop).map{case (t,x) => Point(t, x)}
      LinePlot.series(points, "", colors(i))
    })
    val plt = Overlay(plts:_*)
      .xAxis()
      .yAxis()
      .frame()
      .title(title)
      .xLabel("time")
      .yLabel("Number of individuals")
      .render()
    displayPlot(plt)
  }
  
  def main(args: Array[String]): Unit = {

    val dMod = SpnModels.sir[IntState]()
    val stepSIRds = Step.gillespie(dMod)
    val tsSIRds = Sim.ts(DenseVector(100,5,0), 0.0, 10.0, 0.05, stepSIRds)

    plotTs(tsSIRds, "Gillespie simulation of the SIR model")


  }

}
