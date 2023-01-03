package ex2014.a02.e1;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class SchedulerImpl<T> implements Scheduler<T>{

    private Queue<T> waitingTasks = new LinkedList<>();
    private Queue<T> stoppedTasks = new LinkedList<>(); 
    private Optional<T> execution = Optional.empty();

    @Override
    public void add(T t) {
        if (this.waitingTasks.contains(t)) {
            throw new IllegalArgumentException();
        } else {
            this.waitingTasks.add(t);
        }
    }

    @Override
    public boolean isExecuting() {
        return this.execution.isPresent();
    }

    @Override
    public T getExecutingTask() {
        if(isExecuting()) {
            return this.execution.get();
        } else {
            throw new NoSuchElementException();
        }
        
    }

    @Override
    public void executeNext() {
        if(isExecuting()) {
            throw new IllegalArgumentException();
        } else {
            if (this.waitingTasks.isEmpty()) {
                throw new NoSuchElementException();
            } else {
                this.execution = this.waitingTasks.stream()
                    .findFirst();
                this.waitingTasks.remove(getExecutingTask());
            }
        }
    }

    @Override
    public void complete() {
        if (isExecuting()) {
            this.execution = Optional.empty();
        } else {
            throw new IllegalStateException();
        }
        
    }

    @Override
    public void stop() {
        if(isExecuting()) {
            stoppedTasks.add(getExecutingTask());
            this.execution = Optional.empty();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void preempt() {
        if(isExecuting()) {
            waitingTasks.add(getExecutingTask());
            this.execution = Optional.empty();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void unStop(T t) {
        if (this.stoppedTasks.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            this.waitingTasks.add(this.stoppedTasks.poll());
        }
    }

    @Override
    public Set<T> allStopped() {
        return this.stoppedTasks.stream().collect(Collectors.toSet());
    }

    @Override
    public Set<T> allWaiting() {
        return this.waitingTasks.stream().collect(Collectors.toSet());
    }


    
}
