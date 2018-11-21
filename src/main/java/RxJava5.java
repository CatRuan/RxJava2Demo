import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zip使用示例
 * 参考：https://www.jianshu.com/p/bb58571cdb64
 */
public class RxJava5 {

    public static void main(String[] args) {
        invoke1();//zip的使用1
        invoke2();//zip的使用2
    }


    /**
     * 被观察者不在同一个线程
     * 发射顺序不受控制
     */
    public static void invoke2() {
        System.out.println("invoke2-------------------------------");
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("observable1->emit : 1" + "|" + Thread.currentThread());
                emitter.onNext(1);

                System.out.println("observable1->emit : 2" + "|" + Thread.currentThread());
                emitter.onNext(2);

                System.out.println("observable1->emit : 3" + "|" + Thread.currentThread());
                emitter.onNext(3);

                System.out.println("observable1->emit : onComplete" + "|" + Thread.currentThread());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("observable2->emit : aa" + "|" + Thread.currentThread());
                emitter.onNext("aa");

                System.out.println("observable2->emit : bb" + "|" + Thread.currentThread());
                emitter.onNext("bb");

                System.out.println("observable2->emit : cc" + "|" + Thread.currentThread());
                emitter.onNext("cc");

                System.out.println("observable2->emit : dd" + "|" + Thread.currentThread());
                emitter.onNext("dd");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s; // zip事件时，以发射事件少的为目标zip数量
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext:" + s + "|" + Thread.currentThread());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });


    }

    /**
     * 被观察者在同一个线程
     * 一个被观察者发射完事件另一个被观察者才发射
     */
    public static void invoke1() {
        System.out.println("invoke1-------------------------------");
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("observable1->emit : 1");
                emitter.onNext(1);

                System.out.println("observable1->emit : 2");
                emitter.onNext(2);

                System.out.println("observable1->emit : 3");
                emitter.onNext(3);

                System.out.println("observable1->emit : onComplete");
                emitter.onComplete();
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("observable2->emit : aa");
                emitter.onNext("aa");

                System.out.println("observable2->emit : bb");
                emitter.onNext("bb");

                System.out.println("observable2->emit : cc");
                emitter.onNext("cc");

                System.out.println("observable2->emit : dd");
                emitter.onNext("dd");
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s; // zip事件时，以发射事件少的为目标zip数量
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext:" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

    }
}
