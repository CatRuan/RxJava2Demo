import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Rxjava基本用法使用示例
 * 参考：https://www.jianshu.com/p/464fa025229e
 */
public class RxJava1 {
    public static void main(String[] args) {
        invoke1();//演示最基本调用
        /**
         * onError和onComplete互斥，只能发送一个
         */
        invoke2();//调用onComplete后调用onError

        invoke3();//调用onError后调用onComplete

        invoke4();//链式调用演示

        invoke5();//多个观察者演示
    }

    /**
     * 链式调用演示
     */
    private static void invoke4() {
        System.out.println("invoke4-------------------------------");

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onComplete");
                emitter.onComplete();//调用onComplete后调用onError会报异常：UndeliverableException（无法交互异常）

            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext：" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

    }

    /**
     * 调用onError后调用onComplete
     */
    private static void invoke3() {
        System.out.println("invoke3-------------------------------");
        //被观察者
        Observable<String> observable3 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 事件发射器
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onError");
                emitter.onError(new Throwable("error"));

                System.out.println("emit event3");
                emitter.onNext("event3");

                System.out.println("emit onComplete");
                emitter.onComplete();//调用onError后调用onComplete不会报异常，但无法接收到complete事件
            }
        });

        //观察者
        Observer<String> observer3 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext：" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError：" + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        //订阅
        observable3.subscribe(observer3);
    }

    /**
     * 调用onComplete后调用onError
     */
    private static void invoke2() {
        System.out.println("invoke2-------------------------------");
        //被观察者
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 事件发射器
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onComplete");
                emitter.onComplete();//调用onComplete后调用onError会报异常：UndeliverableException（无法交互异常）

                System.out.println("emit event3");
                emitter.onNext("event3");

                System.out.println("emit error");
                emitter.onError(new Throwable("error"));
            }
        });

        //观察者
        Observer<String> observer2 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext：" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError：" + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        //订阅
        observable2.subscribe(observer2);
    }


    /**
     * 最基本调用，不使用链式调用
     */
    public static void invoke1() {
        System.out.println("invoke1-------------------------------");
        //被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 事件发射器
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onComplete");
                emitter.onComplete();// 调用onComplete以后发送的事件，观察者将不再接收

                System.out.println("emit event3");
                emitter.onNext("event3");

            }
        });

        //观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(" onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println(" onNext：" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(" onError");
            }

            @Override
            public void onComplete() {
                System.out.println(" onComplete");
            }
        };
        //订阅
        observable.subscribe(observer);
    }


    /**
     * 同一个被观察者，多个观察者，事件会被重复发射多次（=观察者数量）
     */
    public static void invoke5() {
        System.out.println("invoke5-------------------------------");
        //被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 事件发射器
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onComplete");
                emitter.onComplete();// 调用onComplete以后发送的事件，观察者将不再接收

//                System.out.println("emit event3");
//                emitter.onNext("event3");

            }
        });

        //观察者
        Observer<String> observer1 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("observer1 onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("observer1 onNext：" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("observer1 onError");
            }

            @Override
            public void onComplete() {
                System.out.println("observer1 onComplete");
            }
        };


        //观察者
        Observer<String> observer2 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("observer2 onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("observer2 onNext：" + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("observer2 onError");
            }

            @Override
            public void onComplete() {
                System.out.println("observer2 onComplete");
            }
        };
        //订阅
        observable.subscribe(observer1);
        observable.subscribe(observer2);
    }
}
