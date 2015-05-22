package org.weyoung.xianbicycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.CommonParams;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.mapcontrol.MapParams;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.IRouteResultObserver;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.model.datastruct.RoutePlanNode;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.nplatform.comapi.map.MapGLSurfaceView;

import org.weyoung.xianbicycle.data.Place;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {
    public static final String FROM = "from";
    public static final String TO = "to";

    private MapGLSurfaceView mapView;

    private RoutePlanModel mRoutePlanModel = null;
    private IRouteResultObserver mRouteResultObserver = new IRouteResultObserver() {

        @Override
        public void onRoutePlanYawingSuccess() {
        }

        @Override
        public void onRoutePlanYawingFail() {
        }

        @Override
        public void onRoutePlanSuccess() {
            BNMapController.getInstance().setLayerMode(
                    MapParams.Const.LayerMode.MAP_LAYER_MODE_ROUTE_DETAIL);
            mRoutePlanModel = (RoutePlanModel) NaviDataEngine.getInstance()
                    .getModel(CommonParams.Const.ModelName.ROUTE_PLAN);
        }

        @Override
        public void onRoutePlanFail() {
        }

        @Override
        public void onRoutePlanCanceled() {
        }

        @Override
        public void onRoutePlanStart() {
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = BaiduNaviManager.getInstance().createNMapView(this);
        BNRoutePlaner.getInstance().setRouteResultObserver(mRouteResultObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ViewGroup) (findViewById(R.id.map_content))).addView(mapView);
        BNMapController.getInstance().onResume();

        Place from = (Place) getIntent().getSerializableExtra(FROM);
        Place to = (Place) getIntent().getSerializableExtra(TO);

        if (from != null && to != null) {
            routePlan(from, to);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BNRoutePlaner.getInstance().setRouteResultObserver(null);
        ((ViewGroup) (findViewById(R.id.map_content))).removeAllViews();
        BNMapController.getInstance().onPause();
    }

    private void routePlan(Place from, Place to) {
        RoutePlanNode startNode = new RoutePlanNode(from.getLat(), from.getLon(),
                RoutePlanNode.FROM_MAP_POINT, from.getName(), from.getDescription());
        RoutePlanNode endNode = new RoutePlanNode(to.getLat(), to.getLon(),
                RoutePlanNode.FROM_MAP_POINT, to.getName(), to.getDescription());
        ArrayList<RoutePlanNode> nodeList = new ArrayList<>(2);
        nodeList.add(startNode);
        nodeList.add(endNode);
        BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(this, null));
        BNRoutePlaner.getInstance().setCalcMode(RoutePlanParams.NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME);
        BNRoutePlaner.getInstance().setRouteResultObserver(mRouteResultObserver);
        boolean ret = BNRoutePlaner.getInstance().setPointsToCalcRoute(
                nodeList, CommonParams.NL_Net_Mode.NL_Net_Mode_OnLine);
        if(!ret){
            Toast.makeText(this, R.string.route_plan_failed, Toast.LENGTH_SHORT).show();
        }
    }

}
