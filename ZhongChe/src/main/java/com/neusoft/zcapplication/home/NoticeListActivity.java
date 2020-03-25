package com.neusoft.zcapplication.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewActivity;
import com.neusoft.zcapplication.TicketService.SearchFlightTicketActivity;
import com.neusoft.zcapplication.api.CarApi;
import com.neusoft.zcapplication.api.NoticeApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetAllNotice;
import com.neusoft.zcapplication.entity.GetNotice;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.MarqueeDownTextView;
import com.neusoft.zcapplication.widget.MarqueeTextViewClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeListActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout btn_back;
    private ListView notice_list;
    private NoticeListAdapter mNoticeAdapter;
    private List<GetNotice> dataList;
    private String [] textArrays = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        dataList = new ArrayList<>();

        initView();
        getNoticeList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }


    private void getNoticeList(){
        showLoading();
        Map<String,Object> params = new HashMap<>();
        RetrofitFactory.getInstance().createApi(NoticeApi.class).getNoticeList()
                .enqueue(new CallBack<List<GetNotice>>() {
                    @Override
                    public void success(List<GetNotice> response) {
                        dismissLoading();
                        if (response.size() == 0){
                            ToastUtil.toast("暂无公告");
                        }
                        dataList.clear();
                        dataList.addAll(response);
                        mNoticeAdapter.setList(dataList);
                        mNoticeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        AppUtils.setStateBar(mContext,findViewById(R.id.frg_status_bar));
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        notice_list = (ListView) findViewById(R.id.lv_notice_list);
        mNoticeAdapter = new NoticeListAdapter(mContext,dataList);
        notice_list.setAdapter(mNoticeAdapter);
        notice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String url = "http://www.customs.gov.cn/customs/302249/302266/302267/2032701/index.html";
                GetNotice notice = dataList.get(position);
                Intent it = new Intent(NoticeListActivity.this,ShowViewActivity.class);
                it.putExtra("url",notice.getURL());
                startActivity(it);
            }
        });
    }
}
