import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Flowable使用示例中，背压策略示例
 * 参考：https://www.jianshu.com/p/9b1304435564
 */
public class RxJava7 {

    public static void main(String[] args) {
//        invoke(); // BackpressureStrategy.ERROR策略
//        invoke1(); // BackpressureStrategy.BUFFER策略
//        invoke2(); // BackpressureStrategy.DROP策略
        invoke3(); // BackpressureStrategy.LATEST策略

    }

    static Subscription subscription1;
    static Subscription subscription2;
    static Subscription subscription3;

    /**
     * 流量超负荷时抛出异常策略
     * BackpressureStrategy.ERROR
     */
    public static void invoke() {
        System.out.println("----------invoke-----------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    subscription1.request(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }).start();
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 129; i++) {//最多只缓存128个事件
                    System.out.println("emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        subscription1 = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError:" + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    /**
     * 扩大缓存策略
     * BackpressureStrategy.BUFFER
     */
    public static void invoke1() {
        System.out.println("----------invoke1-----------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    subscription2.request(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }).start();
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 200; i++) {//最多只缓存128个事件
                    System.out.println("emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        subscription2 = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError:" + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }


    /**
     * BackpressureStrategy.DROP
     * 超过缓存抛弃策略,抛弃超过缓存的
     */
    public static void invoke2() {
        System.out.println("----------invoke2-----------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(110);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscription3.request(128);
            }
        }).start();
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 5000000; i++) {//最多只缓存128个事件
//                    System.out.println("emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        subscription3 = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError:" + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    static Subscription subscription4;

    /*
     * BackpressureStrategy.LATEST
     * 超过缓存抛弃策略,抛弃之前的，保留最新的
     */
    public static void invoke3() {
        System.out.println("----------invoke3-----------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscription4.request(128);
            }
        }).start();
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 6000000; i++) {//最多只缓存128个事件
//                    System.out.println("emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.LATEST)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        subscription4 = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError:" + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
}
