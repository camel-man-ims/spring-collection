package hello.proxy.code.pureproxy;

public class ProxyPatternClient {

    private Subject subject;

    public ProxyPatternClient(Subject subject){
        this.subject = subject;
    }

    public void execute(){
        subject.opertaion();
    }
}
