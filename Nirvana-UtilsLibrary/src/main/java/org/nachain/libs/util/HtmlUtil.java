package org.nachain.libs.util;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {


    public static String addBr(String source) {
        String makeContent = new String();

        int Mode = 2;

        if (Mode == 1) {
            StringTokenizer strToken = new StringTokenizer(source, "\n");
            while (strToken.hasMoreTokens()) {
                makeContent = makeContent + "<br>" + strToken.nextToken();
            }
        } else if (Mode == 2) {
            makeContent = CommUtil.replace(source, "\n", "<br>");
        }

        return makeContent;
    }


    public static final String escapeLtGtHtml(String source) {
        if (source == null) {
            source = "";
            return source;
        }
        String newSource = source;
        newSource = newSource.trim().replaceAll("<", "&lt;");
        newSource = newSource.trim().replaceAll(">", "&gt;");

        return newSource;
    }


    public static final String escapeHTMLTag(String source) {
        if (source == null) {
            source = "";
            return source;
        }
        String newSource = source;
        newSource = newSource.trim().replaceAll("&", "&amp;");
        newSource = newSource.trim().replaceAll("<", "&lt;");
        newSource = newSource.trim().replaceAll(">", "&gt;");
        newSource = newSource.trim().replaceAll("\t", "    ");
        newSource = newSource.trim().replaceAll("\r\n", "\n");
        newSource = newSource.trim().replaceAll("\n", "<br>");
        newSource = newSource.trim().replaceAll("  ", " &nbsp;");
        newSource = newSource.trim().replaceAll("'", "&#39;");
        newSource = newSource.trim().replaceAll("\\\\", "&#92;");

        return newSource;
    }


    public static String HTMLEncode(String source) {
        if (source == null) {
            source = "";
            return source;
        }
        String newSource = source;
        newSource = newSource.replaceAll("&", "&amp;");
        newSource = newSource.replaceAll("<", "&lt;");
        newSource = newSource.replaceAll(">", "&gt;");
        newSource = newSource.replaceAll("\"", "&quot;");
        newSource = newSource.replaceAll("'", "&#146;");

        return newSource;
    }


    public static String clearXHTML(String source) {
        if (source == null) {
            source = "";
            return source;
        }

        String newSource = source;


        newSource = newSource.replaceAll("<style[^>]*?>[\\s\\S]*?</style>", "");


        newSource = newSource.replaceAll("<script[^>]*?>[\\s\\S]*?</script>", "");


        newSource = newSource.replaceAll("<!--[^-->]*?[\\s\\S]*?-->", "");


        newSource = newSource.replaceAll("<.[^>]*>", "");


        newSource = newSource.replaceAll("&nbsp;", "");

        return newSource;
    }


    public static List<String> findByEmail(String content) {
        List<String> returnValue = new ArrayList<String>();
        String pEmail = "\\w@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern emailPattern = Pattern.compile(pEmail);
        Matcher emailMatcher = emailPattern.matcher(content);
        while (emailMatcher.find()) {

            String find = emailMatcher.group().toString();
            returnValue.add(find);
        }
        return returnValue;
    }


    public static List<String> findByTagA(String content) {
        List<String> returnValue = new ArrayList<String>();

        String pTagA = "(?i)<a[^>]*>.*?<\\/a>";
        Pattern tagAPattern = Pattern.compile(pTagA);
        Matcher tagAMatcher = tagAPattern.matcher(content);
        while (tagAMatcher.find()) {

            String find = tagAMatcher.group().toString();
            returnValue.add(find);
        }
        return returnValue;
    }


    public static String clearByTagA(String content) {
        String returnValue = content;

        List<String> tagList = findByTagA(content);
        for (String tag : tagList) {
            returnValue = CommUtil.replace(returnValue, tag, "");
        }

        return returnValue;
    }


    public static List<String> findByTagImg(String content) {
        List<String> returnValue = new ArrayList<String>();

        {


            String pTagImg = "(?i)<img \\b[^>]*(?:/>|>(?:[^<]*</img>)?)";
            Pattern tagImgPattern = Pattern.compile(pTagImg);
            Matcher tagImgMatcher = tagImgPattern.matcher(content);
            while (tagImgMatcher.find()) {

                String find = tagImgMatcher.group().toString();
                returnValue.add(find);
            }
        }

        return returnValue;
    }


    public static List<String> findByURL(String content) {
        List<String> returnValue = new ArrayList<String>();

        String pURL = "(?i)(?:http\\://)?[ \\da-z][-\\da-z]*(?:\\.[\\da-z][-\\da-z]*)+[-\\w.%#?&=/!]*";

        Pattern urlPattern = Pattern.compile(pURL);
        Matcher urlMatcher = urlPattern.matcher(content);
        while (urlMatcher.find()) {

            String find = urlMatcher.group().toString();
            returnValue.add(find);
        }
        return returnValue;
    }


    public static List<String> getImages(String sourceCode) {
        List<String> imageList = new ArrayList<String>();

        MicrosoftTagTypes.register();
        MasonTagTypes.register();
        Source source = new Source(sourceCode);
        List<?> list = source.getAllElements("img");
        for (int i = 0; i < list.size(); i++) {
            String v = ((Element) list.get(i)).getAttributeValue("src");
            imageList.add(v);
        }

        return imageList;
    }


    public static String[] getImages(String sourceCode, String startsWith) {
        String[] imagess;


        List<String> imageList = getImages(sourceCode);


        ListIterator<String> listIter = imageList.listIterator();
        while (listIter.hasNext()) {
            String v = (String) listIter.next();
            if (!v.startsWith(startsWith)) {
                listIter.remove();
            }
        }


        imagess = new String[imageList.size()];
        imageList.toArray(imagess);

        return imagess;
    }


}