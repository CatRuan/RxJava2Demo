import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Flowable使用示例下， emitter.requested() 和  s.request(x)
 * 参考：https://www.jianshu.com/p/9b1304435564
 */
public class RxJava8 {

    public static void main(String[] args) {
        invoke1(); // 同一个线程
        invoke2(); // 不同线程

    }

    public static void invoke1() {
        System.out.println("----------invoke1-----------");
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1 : requested" + emitter.requested());
                emitter.onNext(1);

                System.out.println("emit 2 : requested" + emitter.requested());
                emitter.onNext(2);

                System.out.println("emit 3 : requested" + emitter.requested());
                emitter.onNext(3);

                System.out.println("emit 4 : requested" + emitter.requested());
                emitter.onNext(4);
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        s.request(1000);
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

    public static void invoke2() {
        System.out.println("----------invoke2-----------");
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1 : requested" + emitter.requested());
                emitter.onNext(1);

                System.out.println("emit 2 : requested" + emitter.requested());
                emitter.onNext(2);

                System.out.println("emit 3 : requested" + emitter.requested());
                emitter.onNext(3);

                System.out.println("emit 4 : requested" + emitter.requested());
                emitter.onNext(4);
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        s.request(1000);
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
