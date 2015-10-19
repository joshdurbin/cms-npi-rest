import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.durbs.npi.NPIRestModule
import io.durbs.npi.chain.IndividualChain
import io.durbs.npi.chain.OrganizationChain
import io.durbs.npi.chain.ParametersChain
import io.durbs.npi.config.RequestLimitsConfig
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import ratpack.config.ConfigData
import ratpack.groovy.template.MarkupTemplateModule
import ratpack.hystrix.HystrixMetricsEventStreamHandler
import ratpack.hystrix.HystrixModule
import ratpack.rx.RxRatpack
import ratpack.server.Service
import ratpack.server.StartEvent

import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {

    ConfigData configData = ConfigData.of { c ->
      c.yaml("$serverConfig.baseDir.file/application.yaml")
      c.env()
      c.sysProps()
    }

    bindInstance(RxMongoPersistenceServiceConfig, configData.get("/persistenceservice", RxMongoPersistenceServiceConfig))
    bindInstance(RequestLimitsConfig, configData.get("/requestlimits", RequestLimitsConfig))

    bindInstance(ObjectMapper, new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .setSerializationInclusion(JsonInclude.Include.NON_EMPTY))

    module MarkupTemplateModule
    module NPIRestModule
    module new HystrixModule().sse()

    bindInstance Service, new Service() {

      @Override
      void onStart(StartEvent event) throws Exception {
        RxRatpack.initialize()
      }
    }
  }

  handlers {

    get {
      redirect('/api/v0/individual')
    }

    all chain(registry.get(ParametersChain))

    prefix('api/v0/individual') {
      all chain(registry.get(IndividualChain))
    }

    prefix('api/v0/organization') {
      all chain(registry.get(OrganizationChain))
    }

    get("hystrix.stream", new HystrixMetricsEventStreamHandler())
  }
}
