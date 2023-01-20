package webserver.service;

import db.Database;
import db.UserIdSession;
import webserver.httpUtils.Request;
import webserver.httpUtils.Response;

import java.io.IOException;

public class ShowUserListService extends AlreadyLoggedInService{

    private String sid_userid;

    public ShowUserListService(String sid_usrid) {
        super(sid_usrid);
        this.sid_userid = sid_usrid;
    }

    @Override
    public Response exec(Request req) throws IOException
    {
        Response res = super.exec(req);

        byte[] bodyByte = Service.urlToByte(req.getReqLine().getQuery());

        if(req.getReqLine().getQuery().endsWith(".html"))
        {
            String bodyStr = new String(bodyByte);
            bodyStr = replaceUserList(bodyStr);

            )
        }
    }

    private String replaceUserList(String bodyString)
    {
        return bodyString.replace(
            "<tbody>\n" +
                    "                <tr>\n" +
                    "                    <th scope=\"row\">1</th>\n" +
                    "                    <td>javajigi</td>\n" +
                    "                    <td>자바지기</td>\n" +
                    "                    <td>javajigi@sample.net</td>\n" +
                    "                    <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <th scope=\"row\">2</th>\n" +
                    "                    <td>slipp</td>\n" +
                    "                    <td>슬립</td>\n" +
                    "                    <td>slipp@sample.net</td>\n" +
                    "                    <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                    "                </tr>\n" +
                    "                </tbody>",
            new StringBuffer()
                    .append("<tbody>\n")
                    .append("<tr>\n")
                    .append("<th scope=\"row\">1</th>\n")
                    .append("<td>"+ Database.findUserById(UserIdSession.getUserId(sid_userid)) +"</td>\\n")
                    .toString()
        );
    }
}
