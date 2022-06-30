package srowntree;

import org.jboss.weld.context.WeldAlterableContext;
import org.jboss.weld.context.api.ContextualInstance;
import org.jboss.weld.context.bound.*;
import org.jboss.weld.manager.api.WeldManager;

import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContextRunnableWrapper implements Runnable {

  Runnable task;
  Map<Class<? extends Annotation>, Collection<ContextualInstance<?>>> scopeToContextualInstances;

  public ContextRunnableWrapper(Runnable task) {
    this.task = task;
    scopeToContextualInstances = new HashMap<>();
    for (WeldAlterableContext context : CDI.current().select(WeldManager.class).get().getActiveWeldAlterableContexts()) {
      scopeToContextualInstances.put(context.getScope(), context.getAllContextualInstances());
    }
  }

  @Override
  public void run() {
    WeldManager weldManager = CDI.current().select(WeldManager.class).get();
    BoundRequestContext requestContext = weldManager.instance().select(BoundRequestContext.class, BoundLiteral.INSTANCE).get();
    BoundSessionContext sessionContext = weldManager.instance().select(BoundSessionContext.class, BoundLiteral.INSTANCE).get();
    BoundConversationContext conversationContext = weldManager.instance().select(BoundConversationContext.class, BoundLiteral.INSTANCE).get();

    // we will be using bound contexts, prepare backing structures for contexts
    Map<String, Object> sessionMap = new HashMap<>();
    Map<String, Object> requestMap = new HashMap<>();
    BoundRequest boundRequest = new MutableBoundRequest(requestMap, sessionMap);

    // activate contexts
    requestContext.associate(requestMap);
    requestContext.activate();
    sessionContext.associate(sessionMap);
    sessionContext.activate();
    conversationContext.associate(boundRequest);
    conversationContext.activate();

    // propagate all contexts that have some bean in them
    if (scopeToContextualInstances.get(requestContext.getScope()) != null) {
      requestContext.clearAndSet(scopeToContextualInstances.get(requestContext.getScope()));
    }
    if (scopeToContextualInstances.get(sessionContext.getScope()) != null) {
      sessionContext.clearAndSet(scopeToContextualInstances.get(sessionContext.getScope()));
    }
    if (scopeToContextualInstances.get(conversationContext.getScope()) != null) {
      conversationContext.clearAndSet(scopeToContextualInstances.get(conversationContext.getScope()));
    }

    task.run();

    requestContext.deactivate();
    conversationContext.deactivate();
    sessionContext.deactivate();
  }

}
