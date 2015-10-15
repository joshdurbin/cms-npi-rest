package io.durbs.npi.renderer

import groovy.transform.CompileStatic
import io.durbs.npi.domain.Individual
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.render.GroovyRendererSupport
import ratpack.jackson.Jackson

@CompileStatic
class IndividualRenderer extends GroovyRendererSupport<Individual> {

  @Override
  void render(GroovyContext context, Individual individual) throws Exception {
    context.byContent {
      json {
        context.render Jackson.json(individual)
      }
    }
  }
}