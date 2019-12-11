package com.fincher.thread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Test;

public class MyThreadTest {

    @Test
    public void testThreadWithExceptionContinueExecution() throws InterruptedException {
        
        MyRunnableIfc runnable = new MyRunnableIfc() {
            @Override
            public void run() {
                try {
                    System.out.println("Thread running");
                    Thread.sleep(500);
                    throw new Error("Test Exception");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public boolean continueExecution() {
                return true;
            }

            @Override
            public void terminate() {
            }
         
        };
        
        MyThreadIfc thread = new MyThread("TestThread", runnable);
        assertEquals(runnable, thread.getRunnable().get());
        thread.start();
        Thread.sleep(2000);
        assertFalse("Thread did not continue after Exception", thread.isTerminated());

        thread.terminate();
        
        MyCallableIfc<Boolean> callable = new MyCallableIfc<Boolean>() { 
            @Override
            public Boolean call() {
                try {
                    System.out.println("Thread running");
                    Thread.sleep(500);
                    throw new Error("Test Exception");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                return true;

            }

            @Override
            public boolean continueExecution() {
                return true;
            }

            @Override
            public void terminate() {
            }
        };
        
        thread = new MyThread("TestThread", callable);
        assertEquals(callable, thread.getCallable().get());
        thread.start();
        Thread.sleep(2000);
        assertFalse("Thread did not continue after Exception", thread.isTerminated());

        thread.terminate();
    }

    @Test
    public void testThreadWithExceptionStopExecution() throws InterruptedException {

        MyThreadIfc thread = new MyThread("TestThread", new MyRunnableIfc() {

            @Override
            public void run() {
                try {
                    System.out.println("Thread running");
                    Thread.sleep(100);
                    throw new Error("Test Exception");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public boolean continueExecution() {
                return true;
            }

            @Override
            public void terminate() {
            }
        });

        thread.setContinueAfterException(false);
        thread.setExceptionHandler(new ExceptionHandlerIfc() {

            @Override
            public void onException(Throwable t) {
                t.printStackTrace();

            }
        });

        thread.start();

        thread.join(1000);

        if (!thread.isTerminated()) {
            Assert.fail("Thread continued after Exception");
        }
    }
    

    @Test
    public void testWithRunnable() throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        testThread(new TestRunnable(queue), null, queue);
    }

    @Test
    public void testWithCallable() throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        MyRunnableIfc runnable = new TestRunnable(queue);

        MyCallableIfc<Boolean> callable = new MyCallableIfc<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                runnable.run();
                return true;
            }

            @Override
            public void terminate() {
                runnable.terminate();
            }

            @Override
            public boolean continueExecution() {
                return runnable.continueExecution();
            }
        };
        testThread(null, callable, queue);
    }

    private void testThread(MyRunnableIfc runnable, MyCallableIfc<?> callable,
            BlockingQueue<Integer> queue) throws InterruptedException {

        MyThreadIfc thread;
        if (runnable != null) {
            thread = new MyThread("TestThread", runnable);
        } else {
            thread = new MyThread("TestThread", callable);
        }

        thread.start();
        thread.join();
        assertEquals(10, queue.size());
        System.out.println("Thread terminated");

        thread.terminate();
    }

    private static class TestRunnable implements MyRunnableIfc {
        private int count = 0;

        final BlockingQueue<Integer> queue;

        public TestRunnable(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                System.out.println("Thread running");
                queue.add(count++);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public boolean continueExecution() {
            return count < 10;
        }

        @Override
        public void terminate() {
        }
    }

}
