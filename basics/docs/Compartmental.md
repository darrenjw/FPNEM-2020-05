---
title: "Part 2: Compartmental models"
author: "Darren Wilkinson"
date: "darrenjw.github.io"
---

# SIR model

## The SIR model

One of the simplest infectious disease models is the [*Susceptible-Infectious-Removed (SIR) model*](https://en.wikipedia.org/wiki/Compartmental_models_in_epidemiology). As with growth models, individuals can be considered discrete or continuous, time can be considered discrete or continuous, and the dynamics can be deterministic or stochastic. Here, again, we will consider the case of discrete time, continuous individuals and deterministic dynamics in order to keep the maths as simple as possible.

The important extension relative to the simple growth models we looked at is that we now have three compartments: *S*, *I* and *R*.

*S* denotes the susceptibles and *I* the infectious, as before, and *R* represents the individuals who are neither *S* nor *I*. These are typically individuals who were *I* but are now no longer infectious, because they have been isolated, have recovered, or died.

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
val beta = 5.0e-8; val gamma = 0.1
val pop = Stream.iterate(p0)(update(beta, gamma))
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
 LinePlot.series(spoints, "S", dodgerBlue),
 LinePlot.series(ipoints, "I", crimson),
 LinePlot.series(rpoints, "R", green)
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
 LinePlot.series(lspoints, "S", dodgerBlue),
 LinePlot.series(lipoints, "I", crimson),
 LinePlot.series(lrpoints, "R", green)
 ).xAxis()
  .yAxis()
  .overlayLegend()
  .frame()
  .title("SIR model (log scale)")
  .xLabel("time")
  .yLabel("log(number of individuals)")
  .render()
```


## Basic reproduction number, $R_0$

If we think about how the number of infectious individuals, $I$, evolves, it is clearly increased by the infection process and decreased by the removal process. If these two processes are applied in parallel (as opposed to sequentially, as we did), then the change in $I$ at time $t$ is
$$ I_{t+1}-I_t = \beta S_tI_t - \gamma I_t = \left(\frac{\beta S_t}{\gamma} - 1\right)\gamma I_t $$
Near the start of the epidemic, $S_t\simeq N$, the total population size, and so
$$ I_{t+1}-I_t \simeq \left(\frac{\beta N}{\gamma} - 1\right)\gamma I_t $$
So, if $\beta N/\gamma > 1$, then the number of infectious individuals will increase exponentially.

$\beta N/\gamma$ is known as the [*basic reproduction number*](https://en.wikipedia.org/wiki/Basic_reproduction_number), and is often denoted $R_0$, which is confusing, since it is not the initial number of removed. It can be interpreted as the average number of individuals that each infectious person will infect before removal.

## Flattening the curve

There are different ways to parameterise the SIR model, but the way we have done it, our basic reproduction number is
$$
R_0 = \frac{\beta N}{\gamma}
$$
```scala mdoc
val R0 = beta * p0.S / gamma
```
Since $N$ is fixed, $R_0$ is reduced by reducing the infection rate $\beta$, or increasing the removal rate $\gamma$.

So-called "social distancing" policies reduce $\beta$, and strict self-isolation policies increase $\gamma$. Note that $1/\gamma$ is the average time before an infectious individual is removed from the population.

```scala mdoc:silent
val popF = Stream.iterate(p0)(
    update(0.5 * beta, 1.3 * gamma))
```

## Reduced $\beta$ and increased $\gamma$ (note change in time axis)

```scala mdoc:evilplot:fsir.png
val fspoints = popF.zipWithIndex.
  map{case (pt, t) => Point(t, pt.S)}.
  take(300).toList
val fipoints = popF.zipWithIndex.
  map{case (pt, t) => Point(t, pt.I)}.
  take(300).toList
val frpoints = popF.zipWithIndex.
  map{case (pt, t) => Point(t, pt.R)}.
  take(300).toList

Overlay(
 LinePlot.series(fspoints, "S", dodgerBlue),
 LinePlot.series(fipoints, "I", crimson),
 LinePlot.series(frpoints, "R", green)
 ).xAxis()
  .yAxis()
  .overlayLegend()
  .frame()
  .title("SIR model (flattened)")
  .xLabel("time")
  .yLabel("Number of individuals")
  .render()
```


## Herd immunity

You can see from the previous plot that not everyone gets infected in this flattened scenario. This is because the number of infection events is proportional to the number of susceptibles as well as the number of infectious individuals. So, if the number of susceptibles is sufficiently small, infections won't be able to take off. We previously observed that
$$ I_{t+1}-I_t = \left(\frac{\beta S_t}{\gamma} - 1\right)\gamma I_t, $$
and so if $S_t < \gamma/\beta$, the epidemic should not be able to take off, and we have [*herd immunity*](https://en.wikipedia.org/wiki/Herd_immunity).
For our original simulation we had
```scala mdoc
gamma / beta
```
but we later modified this to
```scala mdoc
1.3 * gamma / (0.5 * beta)
```

# Extensions

## SEIR model

One potential issue with the SIR model is that it assumes that individuals become infectious as soon as they are infected. This may be a reasonable approximation for some diseases, but some diseases have a significant latent period between when an individual becomes infected and when they become infectious. 

The SEIR model addresses this problem by introducing an additional population class, *Exposed* (*E*), between *S* and *I*. So infected individuals initially transition from *S* to *E*, at rate $\beta S I$, as previously discussed. Then *E* individuals transition to *I* at rate $a E$, where $1/a$ is the average incubation period. Transitions from *I* to *R* are as previously. This four-class model often has qualitatively similar behaviour to the SIR model, but the extra flexibility can help it to better capture the quantitative behaviour of a disease with a long latent period.

## Other variations and extensions

There are plenty of other variants of these basic epidemic models. For example, the SIS and SEIS models model the behaviour of diseases which do not convey immunity (with the SIS being essentially logistic growth). Similarly the SIRS and SEIRS model diseases (such as the common cold) where immunity is temporary. Additionally, the removed class, R, can be split into different components, such as *hospitalised*, *recovered* and *died*.

Additional realism can be introduced by creating age structured models, where each class is split into different age groups. Such models are much more realistic, but require a very large number of rate parameters to be specified and/or estimated from data.

All of the models we have been discussing so far apply only to "well-mixed" populations. But in reality, individuals interact in 2d space, and spatial effects (especially associated with varying population density) can sometimes be important.
