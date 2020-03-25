package com.neusoft.zcapplication.OtherService;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.BannerView.Banner;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.TravelApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetTravelProductDetail;
import com.neusoft.zcapplication.entity.GetTravelProducts;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.GlideImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: TenzLiu
 * Time: 2018/6/19 14:45
 * Desc:旅游产品详情
 */

public class TravelProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btn_back;
    private Banner banner_travel_product;
    private ImageView iv_qq,iv_wechat;
    private TextView tv_title,tv_adult_price,tv_child_price,tv_from_city,tv_travel_day,tv_travel_time,
            tv_mobile,tv_email,tv_qq,tv_wechat,tv_product_feature,tv_product_introduce,tv_price_include,
            tv_price_not_include,tv_book_introduction,tv_travel_must_know,tv_shopping_introduction,
            tv_flight_change_introduction,tv_other_introduction,tv_travel_tool_introduction,
            tv_live_introduction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_product_detail);
        initView();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            GetTravelProducts getTravelProducts = (GetTravelProducts) bundle.getSerializable("getTravelProducts");
            getTravelProductDetail(getTravelProducts.getProductId());
        }
    }

    private void initView() {
        AppUtils.setStateBar(mContext,findViewById(R.id.frg_status_bar));
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        banner_travel_product = (Banner) findViewById(R.id.banner_travel_product);
        iv_qq = (ImageView) findViewById(R.id.iv_qq);
        iv_wechat = (ImageView) findViewById(R.id.iv_wechat);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_adult_price = (TextView) findViewById(R.id.tv_adult_price);
        tv_child_price = (TextView) findViewById(R.id.tv_child_price);
        tv_from_city = (TextView) findViewById(R.id.tv_from_city);
        tv_travel_day = (TextView) findViewById(R.id.tv_travel_day);
        tv_travel_time = (TextView) findViewById(R.id.tv_travel_time);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_qq = (TextView) findViewById(R.id.tv_qq);
        tv_wechat = (TextView) findViewById(R.id.tv_wechat);
        tv_product_feature = (TextView) findViewById(R.id.tv_product_feature);
        tv_product_introduce = (TextView) findViewById(R.id.tv_product_introduce);
        tv_price_include = (TextView) findViewById(R.id.tv_price_include);
        tv_price_not_include = (TextView) findViewById(R.id.tv_price_not_include);
        tv_book_introduction = (TextView) findViewById(R.id.tv_book_introduction);
        tv_travel_must_know = (TextView) findViewById(R.id.tv_travel_must_know);
        tv_shopping_introduction = (TextView) findViewById(R.id.tv_shopping_introduction);
        tv_flight_change_introduction = (TextView) findViewById(R.id.tv_flight_change_introduction);
        tv_other_introduction = (TextView) findViewById(R.id.tv_other_introduction);
        tv_travel_tool_introduction = (TextView) findViewById(R.id.tv_travel_tool_introduction);
        tv_live_introduction = (TextView) findViewById(R.id.tv_live_introduction);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    /**
     * 获取我的待办列表
     */
    private void getTravelProductDetail(int productId) {
        Map<String,Object> params = new HashMap<>();
        params.put("productId", productId);

        showLoading();
        RetrofitFactory.getInstance().createApi(TravelApi.class).getTravelProductDetail(params)
                .enqueue(new CallBack<GetTravelProductDetail>() {
                    @Override
                    public void success(GetTravelProductDetail getTravelProductDetail) {
                        dismissLoading();
                        initDetailData(getTravelProductDetail);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 设置数据
     * @param getTravelProductDetail
     */
    private void initDetailData(GetTravelProductDetail getTravelProductDetail) {
        List<String> bannerImageList = new ArrayList<>();
        bannerImageList.addAll(getTravelProductDetail.getPicture());
        banner_travel_product.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner_travel_product.setImages(bannerImageList);
        //banner设置方法全部调用完毕时最后调用
        banner_travel_product.start();
        GetTravelProductDetail.ProductBean productBean = getTravelProductDetail.getProduct();
        Picasso.with(this)
                .load(productBean.getQqQrcode())
                .fit()
                .centerCrop()
                .into(iv_qq);
        Picasso.with(this)
                .load(productBean.getWechatQrcode())
                .fit()
                .centerCrop()
                .into(iv_wechat);
        tv_title.setText(productBean.getTitle());
        tv_adult_price.setText("￥"+productBean.getAdultPrice()+"起");
        tv_child_price.setText("￥"+productBean.getChildPrice()+"起");
        tv_from_city.setText(productBean.getFromCityName());
        tv_travel_day.setText(productBean.getTripDay());
        tv_travel_time.setText(productBean.getTravelTime());
        tv_mobile.setText(productBean.getPhone());
        tv_email.setText(productBean.getEmail());
        tv_qq.setText(productBean.getQq());
        tv_wechat.setText(productBean.getWechat());
        tv_product_feature.setText(productBean.getFeature());
        tv_product_introduce.setText(productBean.getIntroduce());
        tv_price_include.setText(productBean.getCostInclude());
        tv_price_not_include.setText(productBean.getCostNotInclude());
        tv_book_introduction.setText(productBean.getBookNotes());
        tv_travel_must_know.setText(productBean.getTravelNotes());
        tv_shopping_introduction.setText(productBean.getShopNotes());
        tv_flight_change_introduction.setText(productBean.getFlightNotes());
        tv_other_introduction.setText(productBean.getOther());
        tv_travel_tool_introduction.setText(productBean.getTrafficNotes());
        tv_live_introduction.setText(productBean.getHotelNotes());
    }


}
