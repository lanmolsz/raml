package array;

/**
 * Created by E355 on 2016/4/20.
 */
public class Contact<E> {

    private String name;
    private String mobile;
    private E spec;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public E getSpec() {
        return spec;
    }

    public void setSpec(E spec) {
        this.spec = spec;
    }
}
