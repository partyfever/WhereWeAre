package rest;

import java.util.ArrayList;
/**
 * CORS Filter for handling cross domain conflicts
 * Simply adds access-controll parameters to the http headers
 */
import java.util.List;

import javax.ws.rs.ext.Provider;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
@Provider
public class CORSFilter implements ContainerResponseFilter {

  @Override
  public ContainerResponse filter(ContainerRequest creq, ContainerResponse cresp) {

    System.out.println("filter");

    cresp.getHttpHeaders().putSingle("Access-Control-Allow-Origin", "*");
    cresp.getHttpHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");

    List<Object> reqHead = cresp.getHttpHeaders().get("Access-Control-Request-Headers");

    if(reqHead==null){
      reqHead=new ArrayList<Object>();
    }

    reqHead.add("X-Auth-Token");
    reqHead.add("Origin");
    reqHead.add("X-Requested-With");
    reqHead.add("Content-Type");
    reqHead.add("Accept");

    //cresp.getHttpHeaders().put("Access-Control-Allow-Headers", new ArrayList<Object>(reqHead));

    cresp.getHttpHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token");

    return cresp;
  }
}
