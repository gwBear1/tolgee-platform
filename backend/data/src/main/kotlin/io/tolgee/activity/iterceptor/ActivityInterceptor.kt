package io.tolgee.activity.iterceptor

import io.tolgee.activity.data.RevisionType
import org.hibernate.EmptyInterceptor
import org.hibernate.type.Type
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class ActivityInterceptor : EmptyInterceptor() {

  @Autowired
  lateinit var applicationContext: ApplicationContext

  override fun onSave(
    entity: Any?,
    id: Serializable?,
    state: Array<out Any>?,
    propertyNames: Array<out String>?,
    types: Array<out Type>?
  ): Boolean {
    interceptedEventsManager.onFieldModificationsActivity(
      entity, state, null, propertyNames, RevisionType.ADD
    )
    return true
  }

  override fun onDelete(
    entity: Any?,
    id: Serializable?,
    state: Array<out Any>?,
    propertyNames: Array<out String>?,
    types: Array<out Type>?
  ) {
    interceptedEventsManager.onFieldModificationsActivity(
      entity, null, state, propertyNames, RevisionType.DEL
    )
  }

  override fun onFlushDirty(
    entity: Any?,
    id: Serializable?,
    currentState: Array<out Any>?,
    previousState: Array<out Any>?,
    propertyNames: Array<out String>?,
    types: Array<out Type>?
  ): Boolean {
    interceptedEventsManager.onFieldModificationsActivity(
      entity,
      currentState,
      previousState,
      propertyNames,
      RevisionType.MOD
    )
    return true
  }

  override fun onCollectionRemove(collection: Any?, key: Serializable?) {
    interceptedEventsManager.onCollectionModification(collection, key)
  }

  override fun onCollectionRecreate(collection: Any?, key: Serializable?) {
    interceptedEventsManager.onCollectionModification(collection, key)
  }

  override fun onCollectionUpdate(collection: Any?, key: Serializable?) {
    interceptedEventsManager.onCollectionModification(collection, key)
  }

  val interceptedEventsManager: InterceptedEventsManager
    get() = applicationContext.getBean(InterceptedEventsManager::class.java)
}