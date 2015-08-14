package st.kimsmik.novel4u.htmlparser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import st.kimsmik.novel4u.object.NodeInfo;
import st.kimsmik.novel4u.utility.Config;
import st.kimsmik.novel4u.utility.ImgManager;
import st.kimsmik.novel4u.utility.Tuple;

/**
 * Created by chenk on 2015/8/10.
 */
public class CK101Parser {

    private static CK101Parser mIns = null;
    private CK101Parser(){}

    private Hashtable<String,List<NodeInfo>> linkTable = new Hashtable<>();

    public static CK101Parser ins(){
        if(mIns == null)
            mIns = new CK101Parser();
        return mIns;
    }

    private String addNoMoblieFix(String str){
        if(str.contains(Config.CK101_NO_MOBLE_FIX))
            return str;
        if(str.contains("?"))
            str += "&" + Config.CK101_NO_MOBLE_FIX;
        else
            str += "?" + Config.CK101_NO_MOBLE_FIX;
        return str;
    }

    public List<NodeInfo> parseHome(boolean force){
        if(linkTable.containsKey(Config.CK101_HOME_URL) && !force){
            return linkTable.get(Config.CK101_HOME_URL);
        }

        List<NodeInfo> list = new ArrayList<>();
        Connection con = null;
        try {
            con = Jsoup.connect(Config.CK101_HOME_URL);
            Document doc = con.get();
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseHome=====");

            Elements nameEles = doc.select("[class = title_top]");
            List<String> names = parseNodes(nameEles, "a", "title");
            List<String> links = parseNodes(nameEles, "a", "href");
            List<String> imgSrcs = parseNodes(doc, "[class = fl_icn]", "img", "src");

            for(int i = 0; i<names.size(); i++){
                NodeInfo info = new NodeInfo();
                info.name = names.get(i);
                if(links.size() > i) {
                    info.link = links.get(i);
                }
                if(imgSrcs.size() > i){
                    info.imgSrc = imgSrcs.get(i);
                    info.img = ImgManager.ins().getImg(info.imgSrc);
                }
                list.add(info);
            }

            linkTable.put(Config.CK101_HOME_URL,list);
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseHome=====");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return list;
    }

    public List<NodeInfo> parseSubejct(String url, boolean force){
        url = addNoMoblieFix(url);
        if(linkTable.containsKey(url) && !force){
            return linkTable.get(url);
        }
        List<NodeInfo> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseSubejct=====");

            Elements nameEles = doc.select("tbody[id ^= normalthread] div[class = l_sPic]");
            List<String> names = parseNodes(nameEles, "a", "title");
            List<String> links = parseNodes(nameEles, "a", "href");
            List<String> imgSrcs = parseNodes(doc, "tbody[id ^= normalthread] div[class = l_sPic]", "img", "src");
            List<String> cates = parseNodesText(doc, "tbody[id ^= normalthread]  div[class = titleBox]", "a", true);
            List<String> pageNums = parseNodesText(doc, "tbody[id ^= normalthread] [class = tps]", "a", false);
            for(int i = 0; i<names.size(); i++){
                NodeInfo info = new NodeInfo();
                info.name = names.get(i);
                if(links.size() > i) {
                    info.link = links.get(i);
                }
                if(imgSrcs.size() > i){
                    info.imgSrc = imgSrcs.get(i);
                    info.img = ImgManager.ins().getImg(info.imgSrc);
                }
                if(cates.size() > i) {
                    info.category = cates.get(i);
                }
                if(pageNums.size() > i) {
                    info.pageNum = pageNums.get(i);
                }
                list.add(info);
            }

            linkTable.put(url, list);
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseSubejct=====");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> parseNodes(Element root, String query, String type, String attr)throws MalformedURLException,IOException{
        Elements eles = root.select(query);
        return parseNodes(eles,type,attr);
    }
    private List<String> parseNodes(Elements eles, String type, String attr)throws MalformedURLException,IOException{
        List<String> list = new ArrayList<>();
        for(Element ele : eles){
            Element a = ele.select(type).first();
            String str = a.attr(attr);
            if(str != null && str != "")
                list.add(str);
        }
        return list;
    }

    private List<String> parseNodesText(Element root, String query, String type, boolean isFirst)throws MalformedURLException,IOException{
        Elements eles = root.select(query);
        return parseNodesText(eles, type, isFirst);
    }
    private List<String> parseNodesText(Elements eles, String type, boolean isFirst)throws MalformedURLException,IOException{
        List<String> list = new ArrayList<>();
        for(Element ele : eles){
            Element a;
            if(isFirst)
                a = ele.select(type).first();
            else
                a = ele.select(type).last();
            String str = a.text();
            if(str != null && str != "")
                list.add(str);
        }
        return list;
    }
}
