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

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.WorkerExecutorInternal;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @author <a href="mailto:sercan_karaoglu@yahoo.com">Sercan Karaoglu</a>
 */
public class ContextScheduler extends Scheduler {

  private static final Handler<AsyncResult<Object>> NOOP = result -> {};

  private final Vertx vertx;
  private final boolean ordered;
  private final Supplier<Context> context;
  private final Supplier<WorkerExecutor> workerExecutor;

  public ContextScheduler(Context context, boolean blocking) {
    this(context, blocking, true);
  }

  public ContextScheduler(Context context, boolean blocking, boolean ordered) {
    this.vertx = context.owner();
    this.context = () -> context;
    this.ordered = ordered;
    this.workerExecutor = blocking ? ((ContextInternal) context)::createWorkerExecutor : null;
  }

  public ContextScheduler(Vertx vertx, boolean blocking) {
    this(vertx, blocking, true);
  }

  public ContextScheduler(Vertx vertx, boolean blocking, boolean ordered) {
    this.vertx = vertx;
    this.context = vertx::getOrCreateContext;
    this.ordered = ordered;
    this.workerExecutor = blocking ? () -> ((ContextInternal) context.get()).createWorkerExecutor() : null;
  }

  public ContextScheduler(WorkerExecutor workerExecutor) {
    this(workerExecutor, true);
  }

  public ContextScheduler(WorkerExecutor workerExecutor, boolean ordered) {
    this.workerExecutor = () -> workerExecutor;
    this.ordered = ordered;
    this.vertx = ((WorkerExecutorInternal) workerExecutor).getContext().owner();
    this.context = null;
  }

  @Override
  public Worker createWorker() {
    return new WorkerImpl();
  }

  private static final Object DUMB = new JsonObject();

  private class WorkerImpl extends Worker {

    private final ConcurrentHashMap<TimedAction, Object> actions = new ConcurrentHashMap<>();
    private final AtomicBoolean cancelled = new AtomicBoolean();

    @Override
    public Disposable schedule(Runnable action) {
      return schedule(action, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public Disposable schedule(Runnable action, long delayTime, TimeUnit unit) {
      action = RxJavaPlugins.onSchedule(action);
      TimedAction timed = new TimedAction(action, unit.toMillis(delayTime), 0);
      actions.put(timed, DUMB);
      return timed;
    }

    @Override
    public Disposable schedulePeriodically(Runnable action, long initialDelay, long period, TimeUnit unit) {
      action = RxJavaPlugins.onSchedule(action);
      TimedAction timed = new TimedAction(action, unit.toMillis(initialDelay), unit.toMillis(period));
      actions.put(timed, DUMB);
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

    class TimedAction implements Disposable, Runnable {

      private long id;
      private final Runnable action;
      private final long periodMillis;
      private boolean cancelled;

      public TimedAction(Runnable action, long delayMillis, long periodMillis) {
        this.cancelled = false;
        this.action = action;
        this.periodMillis = periodMillis;
        if (delayMillis > 0) {
          schedule(delayMillis);
        } else {
          id = -1;
          execute();
        }
      }

      private void schedule(long delay) {
        this.id = vertx.setTimer(delay, l -> execute());
      }

      private void execute() {
        if (workerExecutor != null) {
          workerExecutor.get().executeBlocking(fut -> run(), ordered, NOOP);
        } else {
          context.get().runOnContext(v -> run());
        }
      }

      @Override
      public void run() {
        synchronized (TimedAction.this) {
          if (cancelled) {
            return;
          }
        }
        action.run();
        synchronized (TimedAction.this) {
          if (periodMillis > 0) {
            schedule(periodMillis);
          }
        }
      }

      @Override
      public synchronized void dispose() {
        if (!cancelled) {
          actions.remove(this);
          if (id > 0) {
            vertx.cancelTimer(id);
          }
          cancelled = true;
        }
      }

      @Override
      public synchronized boolean isDisposed() {
        return cancelled;
      }
    }
  }
}
