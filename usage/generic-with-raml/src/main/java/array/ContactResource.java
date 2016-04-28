package array;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by E355 on 2016/4/20.
 */
@Path("/contacts")
public class ContactResource {

    /**
     * 获得联系人的名字，测试raml工具对数组的支持
     */
    @Path("/names")
    @Produces("application/json")
    @GET
    public String[] getContactNames(){
        return null;
    }

    @Path("/")
    @GET
    @Produces("application/json")
    public Contact<String>[] getContacts(){
        return null;
    }

    /**
     * 哈哈哈哈
     * @param id 这是contact的id
     * @return
     */
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Contact<String> getContact(@PathParam("id") String id){
        return null;
    }

    /**
     * 通过id获取一些东西
     * @param id 这是一个Spec ID1
     * @return
     */
    @Path("/{id}/spec1")
    @GET
    @Produces("application/json")
    public Contact<List<Location>> getContactSpec1(@PathParam("id") String id){
        return null;
    }

    /**
     * 通过id获取一些东西
     * @param id 这是一个ID2
     * @return
     */
    @Path("/spec2")
    @GET
    @Produces("application/json")
    public Contact<List<Location>> getContactSpec2(@QueryParam("id") String id){
        return null;
    }
    /**
     * 通过id获取一些东西
     * @param id 这是一个ID3
     * @param name 这是名字
     * @return
     */
    @Path("/spec3")
    @POST
    @Produces("application/json")
    public Contact<List<Location>> getContactSpec3(@FormParam("id") String id, @FormParam("name") String name){
        return null;
    }
}
