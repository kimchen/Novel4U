package st.kimsmik.novel4u;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import st.kimsmik.novel4u.htmlparser.CK101Parser;
import st.kimsmik.novel4u.object.NodeInfo;
import st.kimsmik.novel4u.object.NodeInfoAdapter;
import st.kimsmik.novel4u.utility.Config;
import st.kimsmik.novel4u.utility.Tuple;


public class MainActivity extends Activity {

    private Handler parseHandler = null;
    private ListView listView = null;

    private Config.SOURCE_TYPE nowSrcType = Config.SOURCE_TYPE.CK101;
    private List<NodeInfo> nowNodeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)this.findViewById(R.id.listView);

        parseHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        fillList(nowNodeList);
                        break;
                    case 1:
                        fillList(nowNodeList);
                        break;
                }

            }
        };

        initList();
    }

    private void initList(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(nowSrcType == Config.SOURCE_TYPE.CK101){
                    nowNodeList = CK101Parser.ins().parseHome(true);
                }
                parseHandler.sendEmptyMessage(0);
            }
        });
        t.start();
    }

    private void fillList(final List<NodeInfo> list){
        NodeInfoAdapter nia = new NodeInfoAdapter(MainActivity.this,list);
        listView.setAdapter(nia);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NodeInfo info = list.get(position);
                final String link = info.link;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(nowSrcType == Config.SOURCE_TYPE.CK101){
                            nowNodeList = CK101Parser.ins().parseSubejct(link, true);
                        }
                        parseHandler.sendEmptyMessage(1);
                    }
                });
                t.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
