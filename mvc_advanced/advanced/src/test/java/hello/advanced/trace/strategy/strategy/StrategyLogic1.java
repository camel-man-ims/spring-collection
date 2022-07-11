package hello.advanced.trace.strategy.strategy;

public class StrategyLogic1 implements Strategy{

    @Override
    public void call() {
        System.out.println("비지니스 로직 1 실행");
    }
}
