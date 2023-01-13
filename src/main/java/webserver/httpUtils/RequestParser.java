package webserver.httpUtils;

import webserver.Paths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

public class RequestParser {

    private String currentLine;

    public RequestParser(){currentLine = new String();}

    public Request parseRequestFromInputStream(InputStream in) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        currentLine = br.readLine();

        Request req = new Request();
        req.setReqLine(parseRequestLine(currentLine)); currentLine = br.readLine();
        req.setReqHeader(getHeaderKeyValues(currentLine, br));
        //req.setReqBody(getBody(currentLine, br)); ?? 이거 넣으면 왜 안댐?

        return req;
    }

    private Map<String, String> parseRequestLine(String currentLine)
    {
        Map<String, String> parsedRequestLine = new HashMap<String, String>();

        String tokens[] = currentLine.split(" ");
        parsedRequestLine.put(Request.REQLINE_METHOD, tokens[0]);
        parsedRequestLine.put(Request.REQLINE_QUERY, tokens[1].equals("/") ?
                Paths.HOME_PATH :
                URLDecoder.decode(tokens[1]));
        parsedRequestLine.put(Request.REQLINE_VERSION, tokens[2]);

        return parsedRequestLine;
    }

    private Map<String, String> getHeaderKeyValues(String currentLine, BufferedReader br) throws IOException{
        Map<String, String> ret = new HashMap<String, String>();

        while(!currentLine.isBlank())
        {
            String keyVal[] = currentLine.split(": ");
            ret.put(keyVal[0], keyVal[1]);

            currentLine = br.readLine();
        }
        return ret;
    }

    private List<String> getBody(String currLine, BufferedReader br) throws IOException {
        List<String> ret = new ArrayList<String>();
        while(!currLine.isBlank())
        {
            ret.add(currLine);
            currLine = br.readLine();
        }
        return ret;
    }
}
