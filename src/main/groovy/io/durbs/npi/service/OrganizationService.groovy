package io.durbs.npi.service

import com.google.inject.Singleton
import io.durbs.npi.domain.Organization
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO

import javax.inject.Inject

@Singleton
class OrganizationService extends AbstractService<Organization> {

  @Inject
  private BasicDAO<Organization, ObjectId> organizationDao

  @Override
  BasicDAO getDao() {
    organizationDao
  }
}
