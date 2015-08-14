package st.kimsmik.novel4u.object;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import st.kimsmik.novel4u.R;

/**
 * Created by chenk on 2015/8/11.
 */
public class NodeInfoAdapter extends BaseAdapter {

    private Context context = null;
    private List<NodeInfo> mList = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public NodeInfoAdapter(Context c, List<NodeInfo> list){
        context = c;
        mList = list;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutComponent lc = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.node_info_cell, null);
            lc = new LayoutComponent((TextView)convertView.findViewById(R.id.nodeName),
                    (ImageView)convertView.findViewById(R.id.nodeImg),
                    (TextView)convertView.findViewById(R.id.nodeCategoryName),
                    (TextView)convertView.findViewById(R.id.nodePageNum));
            convertView.setTag(lc);
        }else{
            lc = (LayoutComponent)convertView.getTag();
        }
        NodeInfo info = mList.get(position);
        //lc.title.setText(mi.title);
        lc.name.setText(info.name);
        lc.img.setImageBitmap(info.img);
        lc.category.setText(info.category);
        if(info.pageNum != null && !info.pageNum.equals("")){
            String str = context.getResources().getText(R.string.page_num_fix).toString();
            str = String.format(str, info.pageNum);
            lc.pageNum.setText(str);
        }else{
            lc.pageNum.setText("");
        }

        return convertView;
    }

    class LayoutComponent{
        public TextView name = null;
        public ImageView img = null;
        public TextView category = null;
        public TextView pageNum = null;
        public LayoutComponent(TextView v1,ImageView v2,TextView v3,TextView v4){
            this.name = v1;
            this.img = v2;
            this.category = v3;
            this.pageNum = v4;
        }
    }
}
