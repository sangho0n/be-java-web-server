package webserver.service;

import db.mysql.DB_Users;
import db.UserIdSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constants.InBody;
import webserver.httpUtils.Request;
import webserver.httpUtils.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class AlreadyLoggedInService implements Service{
    private static final Logger logger = LoggerFactory.getLogger(AlreadyLoggedInService.class);
    private String sid_usrid;
    private byte[] changedBody;
    public AlreadyLoggedInService(String sid_usrid){
        this.sid_usrid = sid_usrid;
    }
    public AlreadyLoggedInService(String sid_usrid, byte[] changedBody){
        this.sid_usrid = sid_usrid;
        this.changedBody = changedBody;
    }


    @Override
    public Response exec(Request req) throws IOException
    {
        String reqQuery = req.getReqLine().getQuery();
        String contentType = Files.probeContentType(new File(reqQuery).toPath());
        byte bodyByte[] = Service.urlToByte(reqQuery);
        if(null != changedBody)
        {
            logger.debug("changedBody is not null");
            bodyByte = changedBody;
        }

        String bodyString = new String(bodyByte);
        String disabledLoginButt = generateUserNameButt();
        String articleModal = generateArticleModal();

        bodyString = bodyString.replace(InBody.beforeReplaced_LogInButton1, disabledLoginButt);
        bodyString = bodyString.replace(InBody.beforeReplaced_LogInButton2, disabledLoginButt);
        bodyString = bodyString.replace(InBody.beforeReplaced_ArticleModal, articleModal);

        bodyByte = bodyString.getBytes();

        return new Response()
                .withVersion(req.getReqLine().getVersion())
                .withStatCodeAndText(200, "OK")
                .withHeaderKeyVal("Content-Type", contentType + ";charset=utf-8")
                .withHeaderKeyVal("Content-Length", Integer.toString(bodyByte.length))
                .withBodyBytes(bodyByte);
    }

    private String generateUserNameButt() {
        StringBuffer sb = new StringBuffer();
        String userName = null;
        try {
            userName = DB_Users.findUserById(UserIdSession.getUserId(sid_usrid)).getName();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return sb.append("<li role=\"button\"><a>"+ userName  +"</a></li>").toString();
    }

    private String generateArticleModal()
    {
        return new String("href=\"#articleModal\"");
    }

    public String getSid_usrid(){return sid_usrid;}
}
