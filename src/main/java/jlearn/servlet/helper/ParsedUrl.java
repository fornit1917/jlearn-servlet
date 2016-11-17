package jlearn.servlet.helper;


import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ParsedUrl
{
    private String base;
    private Map<String, String> queryParams;

    public ParsedUrl(String url)
    {
        queryParams = new HashMap<>();
        String[] parts = url.split("\\?", 2);
        base = parts[0];
        if (parts.length == 2 && !parts[1].trim().isEmpty()) {
            String[] queryParts = parts[1].split("&");
            for (String queryPart: queryParts) {
                String[] paramParts = queryPart.split("=", 2);
                queryParams.put(paramParts[0], paramParts.length == 2 ? paramParts[1] : "");
            }
        }
    }

    public ParsedUrl setQueryParam(String param, String value)
    {
        queryParams.put(param, value);
        return this;
    }

    public ParsedUrl setQueryParam(String param, int value)
    {
        queryParams.put(param, Integer.toString(value));
        return this;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(queryParams.size() > 0 ? queryParams.size() + 2 : 1);
        sb.append(base);
        if (queryParams.size() > 0) {
            sb.append("?");
            boolean addAmp = false;
            for (String param: queryParams.keySet()) {
                if (addAmp) {
                    sb.append("&");
                }
                sb.append(param);
                sb.append("=");
                sb.append(queryParams.get(param));
                addAmp = true;
            }
        }
        return sb.toString();
    }
}
