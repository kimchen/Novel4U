package st.kimsmik.novel4u.htmlparser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import st.kimsmik.novel4u.utility.Config;
import st.kimsmik.novel4u.utility.Tuple;

/**
 * Created by chenk on 2015/8/10.
 */
public class CK101Parser {
    private static String HOME_URL = "http://ck101.com/forum.php?gid=1180";

    private static CK101Parser mIns = null;
    private CK101Parser(){}

    private Hashtable<String,List<Tuple<String,String>>> linkTable = new Hashtable<>();

    public static CK101Parser ins(){
        if(mIns == null)
            mIns = new CK101Parser();
        return mIns;
    }

    public List<Tuple<String,String>> parseHome(boolean force){
        if(linkTable.containsKey(Config.CK101_HOME_URL) && !force){
            return linkTable.get(Config.CK101_HOME_URL);
        }
        List<Tuple<String,String>> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Config.CK101_HOME_URL).get();
            Elements eles = doc.select("[class=title_top]");
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseHome=====");
            for(Element ele : eles){
                Element a = ele.select("a").first();
                String link = a.attr("href");
                String name = a.text();
                list.add(new Tuple<String, String>(name,link));
                Log.d(Config.DEBUG_TAG,name + " : " + link);
            }
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseHome=====");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Tuple<String,String>> parseCategory(String url, boolean force){
        if(linkTable.containsKey(url) && !force){
            return linkTable.get(url);
        }
        List<Tuple<String,String>> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements eles = doc.select("[class=title_top]");
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseCategory=====");
            for(Element ele : eles){
                Element a = ele.select("a").first();
                String link = a.attr("href");
                String name = a.text();
                list.add(new Tuple<String, String>(name,link));
                Log.d(Config.DEBUG_TAG,name + " : " + link);
            }
            Log.d(Config.DEBUG_TAG, "=====CK101Parser.parseCategory=====");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
