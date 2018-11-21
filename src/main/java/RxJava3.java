import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程切换使用示例
 * 参考：https://www.jianshu.com/p/8818b98c44e2
 */
public class RxJava3 {

    public static void main(String[] args) {
        invoke1();//基本使用
//        invoke2();//多次设置订阅线程，只有第一次有效
//        invoke3();//多次设置观察线程，每一次都有效
    }

    /**
     * 多次设置订阅线程，只有第一次有效
     * 不知道为什么这个链式调用有时候不成功，待排查
     */
    public static void invoke3() {
        System.out.println("invoke3-------------------------------");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1:" + Thread.currentThread());
                emitter.onNext("event1");

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("doOnNext accept " + s + ":" + Thread.currentThread());
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("subscribe accept " + s + ":" + Thread.currentThread());
                    }
                });

    }

    /**
     * 多次设置订阅线程，只有第一次有效
     */
    public static void invoke2() {
        System.out.println("invoke2-------------------------------");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1:" + Thread.currentThread());
                emitter.onNext("event1");

            }
        }).subscribeOn(Schedulers.newThread())//多次设置订阅线程
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())//设置观察线程
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("accept " + s + ":" + Thread.currentThread());
                    }
                });

    }

    /**
     * 基本使用
     */
    public static void invoke1() {
        System.out.println("invoke1-------------------------------"+ Thread.currentThread());
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit event1:" + Thread.currentThread());
                emitter.onNext("event1");

            }
        })//设置订阅的线程，即被观察者发送事件的线程
                .observeOn(Schedulers.newThread())//设置观察线程
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 请注意，无论是观察者还是被观察者线程切换，这个方法永远不会切换线程，一定是它当前所在的线程
                        System.out.println("onSubscribe :" + Thread.currentThread());
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext " + s + ":" + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
