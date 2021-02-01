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
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.*;
import io.vertx.core.impl.WorkerExecutorInternal;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @author <a href="mailto:sercan_karaoglu@yahoo.com">Sercan Karaoglu</a>
 * @author <a href="mailto:greg@thetracys.net">Gregory Tracy</a>
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

  @NonNull
  @Override
  public ContextWorker createWorker() {
    return new ContextWorker();
  }

  private static final Object DUMB = new JsonObject();

  public class ContextWorker extends Worker {

    private final ConcurrentLinkedQueue<TimedAction> blockingQueue;
    private final AtomicBoolean runningBlocking;
    private final ConcurrentHashMap<TimedAction, Object> actions;
    private final AtomicBoolean isDisposed;

    private ContextWorker(){
      this.blockingQueue = ContextScheduler.this.blocking ? new ConcurrentLinkedQueue<>() : null;
      this.runningBlocking = new AtomicBoolean(false);
      this.actions = new ConcurrentHashMap<>();
      this.isDisposed = new AtomicBoolean(false);
    }

    public int countActions() {
      return actions.size();
    }

    @NonNull
    @Override
    public Disposable schedule(@NonNull Runnable action) {
      return this.schedulePeriodically(action, 0, 0, TimeUnit.MILLISECONDS);
    }

    @NonNull
    @Override
    public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
      return this.schedulePeriodically(action, delayTime, 0, unit);
    }

    @NonNull
    @Override
    public Disposable schedulePeriodically(@NonNull Runnable action, long initialDelay, long period, @NonNull TimeUnit unit) {
      if(this.isDisposed()){
        return Disposables.disposed();
      }
      TimedAction timed = new TimedAction(RxJavaPlugins.onSchedule(action));
      this.actions.put(timed, DUMB);
      timed.schedule(unit.toMillis(initialDelay), unit.toMillis(period));
      return timed;
    }

    @Override
    public void dispose() {
      if (this.isDisposed.compareAndSet(false, true)) {
        this.actions.keySet().forEach(TimedAction::dispose);
      }
    }

    @Override
    public boolean isDisposed() {
      return this.isDisposed.get();
    }

    private void runBlocking(TimedAction action){
      if(action != null){
        this.blockingQueue.offer(action);
      }
      this.runBlocking();
    }

    private void runBlocking(){

      if(this.blockingQueue.isEmpty() || !this.runningBlocking.compareAndSet(false, true)){
        return;
      }
      TimedAction action = this.blockingQueue.poll();
      if(action == null){
        this.runningBlocking.set(false);
        this.runBlocking();
        return;
      }
      Handler<Promise<Void>> blockingHandler = promise -> {
        action.callRunnable();
        promise.complete();
      };
      boolean ordered = ContextScheduler.this.ordered;
      Handler<AsyncResult<Void>> resultHandler = result -> {
        this.runningBlocking.set(false);
        this.runBlocking();
      };
      if(ContextScheduler.this.workerExecutor == null){
        action.context.executeBlocking(blockingHandler, ordered, resultHandler);
      }else{
        action.context.runOnContext(ignored -> {
          ContextScheduler.this.workerExecutor.executeBlocking(blockingHandler, ordered, resultHandler);
        });
      }

    }

    private static final long NULL_TIMER = -1;

    private class TimedAction implements Disposable {

      private Runnable action;
      private final AtomicLong initialTimerId;
      private final AtomicLong periodicTimerId;
      private final AtomicBoolean isDisposed;
      private final Context context;


      TimedAction(@NonNull Runnable action){
        this.action = action;
        this.initialTimerId = new AtomicLong(NULL_TIMER);
        this.periodicTimerId = new AtomicLong(NULL_TIMER);
        this.isDisposed = new AtomicBoolean(false);
        this.context = ContextScheduler.this.context != null ?
          ContextScheduler.this.context :
          vertx.getOrCreateContext();
      }

      void schedule(long initialDelay, long periodicDelay){
        if(periodicDelay < 1){
          Runnable runnable = this.action;
          this.action = () -> {
            runnable.run();
            this.dispose();
          };
        }
        if(initialDelay < 1){
          this.schedulePeriodic(periodicDelay);
          this.executeOn();
        }else {
          long id = this.context.owner().setTimer(initialDelay, ignored -> {
            this.schedulePeriodic(periodicDelay);
            this.executeOn();
          });
          this.initialTimerId.set(id);
          this.testDisposeTimer(initialTimerId);
        }
      }

      void schedulePeriodic(long periodicDelay){
        if(periodicDelay > 0){
          long id = this.context.owner().setPeriodic(periodicDelay, ignored -> this.executeOn());
          this.periodicTimerId.set(id);
          this.testDisposeTimer(periodicTimerId);
        }
      }

      void executeOn(){
        if(ContextScheduler.this.blocking){
          ContextWorker.this.runBlocking(this);
        }else {
          this.context.runOnContext(ignored -> this.callRunnable());
        }
      }

      void callRunnable(){
        if(this.isDisposed()){
          return;
        }
        this.action.run();
      }

      @Override
      public void dispose() {
        if(this.isDisposed.compareAndSet(false, true)){
          ContextWorker.this.actions.remove(this);
          this.testDisposeTimer(this.initialTimerId);
          this.testDisposeTimer(this.periodicTimerId);
        }
      }

      private void testDisposeTimer(AtomicLong timerId){
        if(this.isDisposed()){
          long id = timerId.getAndSet(NULL_TIMER);
          if(id != NULL_TIMER){
            ContextScheduler.this.vertx.cancelTimer(id);
          }
        }
      }

      @Override
      public boolean isDisposed() {
        return this.isDisposed.get() || ContextWorker.this.isDisposed();
      }
    }

  }
}
