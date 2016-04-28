package org.raml.jaxrs.codegen.test.documentation;


import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;
import org.raml.jaxrs.codegen.model.MethodDocInfo;


public class MethodDocmentTest {
    private Log log = new SystemStreamLog();
    @Test
    public void testCreate(){
        String comment =
                "\t *\n" +
                "\t * 修改国家的一个状态.\n" +
                "\t * 这个方法用来修改指定国家的指定状态，一般用于XX场景\n" +
                "\t * If you are behind a firewall, you will need to let the bootstrap process know.\n" +
                "\t * To do this, create a file at ~/.m2/settings.xml and paste in the XML below,\n" +
                "\t * substituting your settings for those provided. You can safely skip the\n" +
                "\t * username, password and nonProxyHost elements if they are not relevant to you.\n" +
                "\t * @example\n" +
                "\t * \"{name:'China',birthday:1949-10-01,place:960,population:14E,province:[\n" +
                "\t * \t{name:'四川',population:8000W},{name:'上海',population:2000W}\n" +
                "\t * ]}\"\n" +
                "\t * @param countryId\n" +
                "\t * \t\t\t\t国家id\n" +
                "\t * @param stateId\n" +
                "\t * \t\t\t\t状态id\n" +
                "\t * @return\n" +
                "\t * \t\t\t如果参数传入不合法，将抛出异常，并返回null，参数合法，将返回id关联的国家\n" +
                "\t * @throws IllegalArgumentException\n" +
                "\t * \t\t\t当参数不合法将抛出异常\n" ;

        MethodDocInfo doc = MethodDocInfo.create(comment);
        log.warn(doc.getDocumentation());
        log.warn(doc.getExample());
        log.warn(doc.getDocumentation("countryId"));
        log.warn(doc.getDocumentation("stateId"));
        log.warn(doc.getReturnInfo());
        log.warn(doc.getThrowComment("IllegalArgumentException"));
    }
    @Test
    public void testCreate2(){
        String comment =
                "\t * 删除状态\n" +
                "\t * @param uriContext uri上下文\n" +
                "\t * @param stateId 状态id" ;

        MethodDocInfo doc = MethodDocInfo.create(comment);
        log.warn(doc.getDocumentation());
        log.warn(doc.getExample());
        log.warn(doc.getDocumentation("uriContext"));
        log.warn(doc.getDocumentation("stateId"));
        log.warn(doc.getReturnInfo());
        log.warn(doc.getThrowComment("IllegalArgumentException"));
    }
}
