import io.reactivex.disposables.Disposable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

public class Test {


    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("1111->" + Thread.currentThread());
                return "ok";
            }
        });
        new Thread(futureTask).start();
        try {
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("2222->" + Thread.currentThread());
//        System.out.println("222222->" + Thread.currentThread());
    }

}
