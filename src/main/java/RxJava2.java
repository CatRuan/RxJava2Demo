import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 订阅方法subscribe的重载方法使用示例
 * 参考：https://www.jianshu.com/p/464fa025229e
 */
public class RxJava2 {

    public static void main(String[] args) {
        invoke1();//无参数

        invoke2();//一个参数

        invoke3();//两个参数

        invoke4();//三个参数

        invoke5();//最常见的调用
    }

    /**
     * 最常见的调用
     */
    private static void invoke5() {
        System.out.println("invoke5-------------------------------");
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onComplete");
                emitter.onComplete();
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
                System.out.println("onError：" + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    /**
     * 三个参数：消费onNext、onComplete、onError事件
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
                emitter.onComplete();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept ：" + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("accept ：" + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Action");
            }
        });
    }

    /**
     * 两个参数，消费onNext事件和onError事件
     */
    private static void invoke3() {
        System.out.println("invoke3-------------------------------");
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onError");
                emitter.onError(new Throwable("error"));
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept ：" + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("accept ：" + throwable.getMessage());
            }
        });
    }

    /**
     * 一个参数（这里叫做Consumer-消费者，只消费onNext事件
     */
    private static void invoke2() {
        System.out.println("invoke2-------------------------------");
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onComplete");
                emitter.onComplete();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept ：" + s);
            }
        });
    }

    /**
     * 无参数，没有订阅者
     */
    private static void invoke1() {
        System.out.println("invoke1-------------------------------");
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1");
                emitter.onNext("event1");

                System.out.println("emit event2");
                emitter.onNext("event2");

                System.out.println("emit onComplete");
                emitter.onComplete();
            }
        }).subscribe();
    }

}
