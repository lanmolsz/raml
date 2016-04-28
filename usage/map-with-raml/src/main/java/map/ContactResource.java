package map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Map;

/**
 * Created by E355 on 2016/4/20.
 */
@Path("/contacts")
public class ContactResource {

    /**
     * 获得联系人的名字，测试raml工具对map类型的支持
     */
    @Path("/names")
    @GET
    @Produces("application/json")
    public Map<String,String> getContactNames(){
        return null;
    }

    @Path("/")
    @GET
    @Produces("application/json")
    public Map<String,Contact> getContacts(){
        return null;
    }

    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Contact getContact(@PathParam("id") String id){
        return null;
    }
}
