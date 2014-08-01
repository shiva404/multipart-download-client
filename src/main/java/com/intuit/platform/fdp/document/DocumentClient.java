package com.intuit.platform.fdp.document;


import com.intuit.platform.fdp.document.provider.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;

/**
 * Created with IntelliJ IDEA.
 * User: sn1
 * Date: 5/21/14
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentClient {

    private static final String V2_DOCUMENT_BASE_PATH = "getData/user";
    public static WebResource resource = getResource();

    public static WebResource getResource(){
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(cc);
        client.addFilter(new LoggingFilter());
        return client.resource("http://localhost:8082/trackStuff/v1/");
    }

    public static void main(String[] args) throws Exception {
        DocumentClient documentClient = new DocumentClient();
        documentClient.getDocumentWithSource("123");
    }

    //There is a small bug in our code we will fix asap : it will not work as expected(Content length issue)
    private void getDocumentWithSource(String userId) throws Exception {
        ClientResponse response = resource.path(V2_DOCUMENT_BASE_PATH).path(userId).path("download").queryParam("includeSource", "true").accept("multipart/related").get(ClientResponse.class);
        MultiPart multiPart = response.getEntity(MultiPart.class);
        BodyPart documentBodyPart = multiPart.getBodyParts().get(0);
        //to
        String document = documentBodyPart.getEntityAs(String.class);
        BodyPart sourceBodyPart = multiPart.getBodyParts().get(1);
        BodyPartEntity bodyPartEntity = (BodyPartEntity) sourceBodyPart.getEntity();
        String bodyPartContentType = sourceBodyPart.getMediaType().toString();
        System.out.println(bodyPartContentType);
    }
}
