package hello.advanced.trace.strategy.strategy;

public class StrategyLogic2 implements Strategy{
    @Override
    public void call() {
        System.out.println("비지니스 로직2 실행");
    }
}
