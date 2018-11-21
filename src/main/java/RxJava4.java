import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Map使用示例
 * 参考：https://www.jianshu.com/p/128e662906af
 */
public class RxJava4 {
    static List<String> xiaoming = Arrays.asList("小明数学：90", "小明语文：100");
    static List<String> xiaohong = Arrays.asList("小红数学：101", "小红语文：60");
    static List<String> xiaoxue = Arrays.asList("小雪数学：112", "小雪语文：108");

    static Map<String, List<String>> students = new HashMap<>();

    static {
        students.put("小明", xiaoming);
        students.put("小红", xiaohong);
        students.put("小雪", xiaoxue);
    }


    public static void main(String[] args) {
//        invoke1();//Map的使用
//        invoke2();//FlatMap的使用
        invoke3();//ConcatMap的使用
    }

    /**
     * concatMap
     * 保证接收顺序
     */
    public static void invoke3() {
        System.out.println("invoke3-------------------------------");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("小明");
                emitter.onNext("小红");
                emitter.onNext("小雪");
            }
        }).concatMap(new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(String name) throws Exception {
                return Observable.fromIterable(students.get(name));//再次返回被观察者
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String str) throws Exception {
                System.out.println("accept ：" + str);
            }
        });
    }


    /**
     * FlatMap 不保证接收顺序
     */
    public static void invoke2() {
        System.out.println("invoke2-------------------------------");

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("小明");
                emitter.onNext("小红");
                emitter.onNext("小雪");
            }
        }).flatMap(new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(String name) throws Exception {
                return Observable.fromIterable(students.get(name));//再次返回被观察者
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String str) throws Exception {
                System.out.println("accept ：" + str);
            }
        });
    }

    /**
     * 基本使用
     */
    public static void invoke1() {
        System.out.println("invoke1-------------------------------");
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 原始数据：1");
                emitter.onNext(1);

                System.out.println("emit 原始数据：2");
                emitter.onNext(2);

                System.out.println("emit 原始数据：3");
                emitter.onNext(3);
            }
        }).map(new Function<Integer, Integer>() {

            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * 2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("accept 计算结果：" + integer);
            }
        });
    }
}
