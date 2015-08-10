package st.kimsmik.novel4u;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chenk on 2015/8/10.
 */
public class HtmlParser {
    public static void parse(){
        try {

            Document doc = Jsoup.connect("http://ck101.com/thread-3322756-1-1.html").get();
            Elements eles = doc.select("[class=bm_c]");
            Log.i("HtmlParser","=====Parse=====");
            for(Element ele : eles){
                Element a = ele.select("a").first();
                String link = a.attr("href");
                String name = a.text();
                Log.i("HtmlParser",name + " : " + link);
            }
            Log.i("HtmlParser","=====Parse=====");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
