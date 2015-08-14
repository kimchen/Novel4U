package st.kimsmik.novel4u.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chenk on 2015/8/11.
 */
public class ImgManager {
    private static ImgManager mIns = null;
    private LruCache<String, Bitmap> imgCache = null;

    private ImgManager(){
        long maxMemory = Runtime.getRuntime().maxMemory();
        imgCache = new LruCache<String, Bitmap>((int)maxMemory/16){
            @Override
            protected int sizeOf(String key, Bitmap bmp) {
                return bmp.getByteCount();
            }
        };
    }
    public static ImgManager ins(){
        if(mIns == null)
            mIns = new ImgManager();
        return mIns;
    }

    public Bitmap getImg(String src) throws IOException{
        Bitmap img = imgCache.get(src);
        if(img == null){
            URL url = new URL(src);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            InputStream is = con.getInputStream();
            img = BitmapFactory.decodeStream(is);
            imgCache.put(src, img);
            is.close();
        }
        return img;
    }
}
