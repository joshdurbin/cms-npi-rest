package io.durbs.npi.service.morphia

import com.google.inject.Singleton
import com.netflix.hystrix.HystrixCommandGroupKey
import io.durbs.npi.domain.Organization
import io.durbs.npi.service.OrganizationService
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO

import javax.inject.Inject

@Singleton
class OrganizationMorphiaService extends AbstractMorphiaService<Organization> implements OrganizationService {

  @Inject
  private BasicDAO<Organization, ObjectId> organizationDao

  @Override
  BasicDAO getDao() {
    organizationDao
  }

  @Override
  HystrixCommandGroupKey getCommandGroupKey() {
    HystrixCommandGroupKey.Factory.asKey('OrganizationMorphiaService')
  }
}
