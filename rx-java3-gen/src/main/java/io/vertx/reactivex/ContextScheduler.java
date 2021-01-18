/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.vertx.reactivex;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.impl.WorkerExecutorInternal;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.vertx.core.WorkerExecutor;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @author <a href="mailto:sercan_karaoglu@yahoo.com">Sercan Karaoglu</a>
 */
public class ContextScheduler extends Scheduler {

  private final Vertx vertx;
  private final boolean blocking;
  private final boolean ordered;
  private final Context context;
  private final WorkerExecutor workerExecutor;

  public ContextScheduler(Context context, boolean blocking) {
    this(context, blocking, true);
  }

  public ContextScheduler(Context context, boolean blocking, boolean ordered) {
    this.vertx = context.owner();
    this.context = context;
    this.blocking = blocking;
    this.ordered = ordered;
    this.workerExecutor = null;
  }

  public ContextScheduler(Vertx vertx, boolean blocking) {
    this(vertx, blocking, true);
  }

  public ContextScheduler(Vertx vertx, boolean blocking, boolean ordered) {
    this.vertx = vertx;
    this.context = null;
    this.blocking = blocking;
    this.ordered = ordered;
    this.workerExecutor = null;
  }

  public ContextScheduler(WorkerExecutor workerExecutor) {
    this(workerExecutor, true);
  }

  public ContextScheduler(WorkerExecutor workerExecutor, boolean ordered) {
    Objects.requireNonNull(workerExecutor, "workerExecutor is null");
    this.vertx = ((WorkerExecutorInternal) workerExecutor).vertx();
    this.context = null;
    this.workerExecutor = workerExecutor;
    this.blocking = true;
    this.ordered = ordered;
  }

  @Override
  public ContextWorker createWorker() {
    return new ContextWorker();
  }

  private static final Object DUMB = new JsonObject();

  public class ContextWorker extends Worker {

    private final ConcurrentHashMap<TimedAction, Object> actions = new ConcurrentHashMap<>();
    private final AtomicBoolean cancelled = new AtomicBoolean();

    public int countActions() {
      return actions.size();
    }

    @Override
    public Disposable schedule(Runnable action) {
      return schedule(action, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public Disposable schedule(Runnable action, long delayTime, TimeUnit unit) {
      action = RxJavaPlugins.onSchedule(action);
      long delayMillis = unit.toMillis(delayTime);
      TimedAction timed = new TimedAction(action, 0);
      actions.put(timed, DUMB);
      timed.schedule(delayMillis);
      return timed;
    }

    @Override
    public Disposable schedulePeriodically(Runnable action, long initialDelay, long period, TimeUnit unit) {
      action = RxJavaPlugins.onSchedule(action);
      long delayMillis = unit.toMillis(initialDelay);
      TimedAction timed = new TimedAction(action, unit.toMillis(period));
      actions.put(timed, DUMB);
      timed.schedule(delayMillis);
      return timed;
    }

    @Override
    public void dispose() {
      if (cancelled.compareAndSet(false, true)) {
        actions.keySet().forEach(TimedAction::dispose);
      }
    }

    @Override
    public boolean isDisposed() {
      return cancelled.get();
    }

    class TimedAction implements Disposable {

      private final Context context;
      private long id;
      private final Runnable action;
      private final long periodMillis;
      private boolean disposed;

      TimedAction(Runnable action, long periodMillis) {
        this.context = ContextScheduler.this.context != null ? ContextScheduler.this.context : vertx.getOrCreateContext();
        this.disposed = false;
        this.action = action;
        this.periodMillis = periodMillis;
      }

      private synchronized void schedule(long delayMillis) {
        if (delayMillis > 0) {
          id = vertx.setTimer(delayMillis, this::execute);
        } else {
          id = -1;
          execute(null);
        }
      }

      private void execute(Object o) {
        if (workerExecutor != null) {
          workerExecutor.executeBlocking(fut -> {
            run(null);
            fut.complete();
          }, ordered, null);
        } else {
          Context ctx = context != null ? context : vertx.getOrCreateContext();
          if (blocking) {
            ctx.executeBlocking(fut -> {
              run(null);
              fut.complete();
            }, ordered, null);
          } else {
            ctx.runOnContext(this::run);
          }
        }
      }

      private void run(Object arg) {
        synchronized (TimedAction.this) {
          if (disposed) {
            return;
          }
        }
        action.run();
        synchronized (TimedAction.this) {
          if (!disposed) {
            if (periodMillis > 0) {
              schedule(periodMillis);
            } else {
              disposed = true;
              actions.remove(this);
            }
          }
        }
      }

      @Override
      public synchronized void dispose() {
        if (!disposed) {
          actions.remove(this);
          if (id > 0) {
            vertx.cancelTimer(id);
          }
          disposed = true;
        }
      }

      @Override
      public synchronized boolean isDisposed() {
        return disposed;
      }
    }
  }
}
