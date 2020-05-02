---
title: "Part 2: Compartmental models"
author: "Darren Wilkinson"
date: "darrenjw.github.io"
---

# SIR model

## The SIR model

One of the simplest infectious disease models is the [*Susceptible-Infectious-Removed (SIR) model*](https://en.wikipedia.org/wiki/Compartmental_models_in_epidemiology). As with growth models, individuals can be considered discrete or continuous, time can be considered discrete or continuous, and the dynamics can be deterministic or stochastic. Here, again, we will consider the case of discrete time, continuous individuals and deterministic dynamics in order to keep the maths as simple as possible.

The important extension relative to the simple growth models we looked at is that we now have three compartments: *S*, *I* and *R*.

*S* denotes the susceptibles and *I* the infectious, as before, and *R* represents the individuals who are neither *S* nor *I*. These are typically individuals who were *I* but are now no longer infectious, either because they have recovered or died.

## SIR transition modelling

Suppose that at time $t$ we have $S_t$ susceptibles, $I_t$ infectious and $R_t$ recovered, how should we update these to get these to get the numbers at time $t+1$?

We assume that there are two different processes occurring: the $S\rightarrow I$ transition and the $I\rightarrow R$ transition, and that these processes are happening independently of one another.

### $S\rightarrow I$ transition

Similar to the argument used for logistic growth, we assume that the number of people infected is proportional to both the number of infectious people and the number of susceptibles, to that $\beta S_tI_t$ individuals move from $S$ to $I$. That is, $S_t$ will decrease by this amount and $I_t$ will increase by this amount.

### $I\rightarrow R$ transition

We assume that each infectious individual transitions from $I$ to $R$ at rate $\gamma$, so the number of transitions is $\gamma I_t$.

## Transitions in code

```scala mdoc
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
  
update(0.001, 0.01)(p0)
```

## Population dynamics

```scala mdoc:silent
val pop = Stream.iterate(p0)(update(1.0e-7, 0.1))
```
```scala mdoc
pop.take(8).toList
```

## Plot

```scala mdoc:evilplot:sir.png
import com.cibo.evilplot._
import com.cibo.evilplot.plot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import com.cibo.evilplot.plot.renderers.PointRenderer
import com.cibo.evilplot.numeric.Point
import com.cibo.evilplot.colors.HTMLNamedColors._


val spoints = pop.zipWithIndex.
  map{case (pt, t) => Point(t, pt.S)}.
  take(100).toList
val ipoints = pop.zipWithIndex.
  map{case (pt, t) => Point(t, pt.I)}.
  take(100).toList
val rpoints = pop.zipWithIndex.
  map{case (pt, t) => Point(t, pt.R)}.
  take(100).toList

Overlay(
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
```


## Log plot

```scala mdoc:evilplot:lsir.png
val lspoints = pop.zipWithIndex.
  map{case (pt, t) => Point(t, math.log(pt.S))}.
  take(100).toList
val lipoints = pop.zipWithIndex.
  map{case (pt, t) => Point(t, math.log(pt.I))}.
  take(100).toList
val lrpoints = pop.zipWithIndex.
  map{case (pt, t) => Point(t, math.log(pt.R))}.
  take(100).toList

Overlay(
 ScatterPlot.series(lspoints, "S", dodgerBlue),
 ScatterPlot.series(lipoints, "I", crimson),
 ScatterPlot.series(lrpoints, "R", green)
 ).xAxis()
  .yAxis()
  .overlayLegend()
  .frame()
  .title("SIR model (log scale)")
  .xLabel("time")
  .yLabel("log(number of individuals)")
  .render()
```



