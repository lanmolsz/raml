package array;

import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Li Yongchun on 2016/7/6.
 */
@Path("push")
@Produces(MediaType.APPLICATION_JSON)
public class PushResource {


    @PUT
    public void pushMessage(@FormParam("userId") String userId, @FormParam("message") String message) {

    }
}