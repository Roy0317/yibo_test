package com.yibo.yiboapp.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ZhuanyunActivity extends BaseActivity {
    List betNums = new ArrayList<String>();
    List addBonus = new ArrayList<String>();
    List Levels = new ArrayList<Map.Entry>();
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_zhuanyun);
        tableLayout = findViewById(R.id.table_layout);
        loadData();
    }


    //加载数据
    protected void loadData() {
        ApiParams params = new ApiParams();
        HttpUtil.postForm(this, Urls.BONUS_JIAJIANG, params, true, getString(R.string.get_recording), new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
//                if (result.isSuccess()) {
                JSONObject jsonObject = null;
                try {
//                    jsonObject = new JSONObject("{\n" +
//                            "  \"success\": true,\n" +
//                            "  \"accessToken\": \"7fa01108-3d8a-4382-b453-42ba0cf31e2a\",\n" +
//                            "  \"content\": {\n" +
//                            "    \"firstBonus\": {\n" +
//                            "      \"betNum\": 100,\n" +
//                            "      \"id\": 11,\n" +
//                            "      \"levelId\": 22,\n" +
//                            "      \"levelName\": \"VIP3\",\n" +
//                            "      \"multiple\": 1.00,\n" +
//                            "      \"numScale\": 0.100,\n" +
//                            "      \"stationId\": 25,\n" +
//                            "      \"status\": 2\n" +
//                            "    },\n" +
//                            "    \"betNum\": [\n" +
//                            "      100,\n" +
//                            "      50000,\n" +
//                            "      500000\n" +
//                            "    ],\n" +
//                            "    \"level\": [\n" +
//                            "      {\n" +
//                            "       \"30\": \"VIP10\"\n" +
//                            "      },\n" +
//                            "      {\n" +
//                            "        \"22\": \"VIP3\"\n" +
//                            "      },\n" +
//                            "      {\n" +
//                            "        \"24\": \"VIP4\"\n" +
//                            "      },\n" +
//                            "      {\n" +
//                            "        \"25\": \"VIP5\"\n" +
//                            "      },\n" +
//                            "      {\n" +
//                            "        \"26\": \"VIP6\"\n" +
//                            "      },\n" +
//                            "      {\n" +
//                            "        \"27\": \"VIP7\"\n" +
//                            "      },\n" +
//                            "      {\n" +
//                            "        \"28\": \"VIP8\"\n" +
//                            "      },\n" +
//                            "      {\n" +
//                            "        \"29\": \"VIP9\"\n" +
//                            "      }\n" +
//                            "    ],\n" +
//                            "    \"scale\": {\n" +
//                            "      \"25_500000\": 0.500,\n" +
//                            "      \"26_100\": 0.400,\n" +
//                            "      \"24_50000\": 0.300,\n" +
//                            "      \"22_50000\": 0.200,\n" +
//                            "      \"29_50000\": 0.800,\n" +
//                            "      \"28_50000\": 0.700,\n" +
//                            "      \"28_100\": 0.600,\n" +
//                            "      \"30_500000\": 0.900,\n" +
//                            "      \"22_100\": 0.100,\n" +
//                            "      \"22_500000\": 0.300,\n" +
//                            "      \"24_500000\": 0.400,\n" +
//                            "      \"26_500000\": 0.600,\n" +
//                            "      \"24_100\": 0.200,\n" +
//                            "      \"27_500000\": 0.700,\n" +
//                            "      \"28_500000\": 0.800,\n" +
//                            "      \"29_500000\": 0.900,\n" +
//                            "      \"27_100\": 0.500,\n" +
//                            "      \"26_50000\": 0.500,\n" +
//                            "      \"27_50000\": 0.600,\n" +
//                            "      \"30_50000\": 0.800,\n" +
//                            "      \"29_100\": 0.700,\n" +
//                            "      \"30_100\": 0.700,\n" +
//                            "      \"25_50000\": 0.400,\n" +
//                            "      \"25_100\": 0.300\n" +
//                            "    },\n" +
//                            "    \"curLevelName\": \"VIP1\",\n" +
//                            "    \"recordData\": {\n" +
//                            "      \n" +
//                            "    }\n" +
//                            "  }\n" +
//                            "}");
                    JSONObject obj = jsonObject.getJSONObject("content");
                    JSONObject firstBonus = obj.getJSONObject("firstBonus");
                    JSONObject scale = obj.getJSONObject("scale");
                    JSONArray betNum = obj.getJSONArray("betNum");
                    JSONArray level = obj.getJSONArray("level");
                    //解析出投注金额分档
                    for (int i = 0; i < betNum.length(); i++) {
                        betNums.add(betNum.get(i));
                    }
                    //解析出vip等级和对应的数字编号
                    for (int i = 0; i < level.length(); i++) {
                        JSONObject obj1 = (JSONObject) level.get(i);
                        Iterator<String> keys = obj1.keys();
                        String key = "";
                        String value = "";
                        if (keys.hasNext()) {
                            key = keys.next();
                            value = (String) obj1.get(key);
                        }
                        Map.Entry<String, String> entry = new MyEntry<String, String>(key, value);
                        Levels.add(entry);
                    }
                    for (int i = 0; i < Levels.size(); i++) {
                        Map.Entry entry = (Map.Entry) Levels.get(i);
                        for (int j = 0; j < betNums.size(); j++) {
                            addBonus.add(scale.get(entry.getKey() + "_" + betNums.get(j)) + "‰");
                        }
                    }
                    makeTabLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                }
            }
        });
    }

    void makeTabLayout() {
        //计算有多少行多少列
        int rows = Levels.size() + 1;
        int cloums = betNums.size() + 1;
        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < cloums; j++) {
                if (i == 0 && j == 0) {
                    TextView text = new TextView(getBaseContext());
//                    text.setBackgroundResource(R.drawable.table_shape);//使用shape文件将表格设置成边框显示效果。
                    text.setPadding(15, 15, 15, 15);
                    //给每以列填充显示的内容
                    text.setText("等级/投注额");
                    tableRow.addView(text, j);
                    text.setTextSize(18);
                    text.setBackgroundColor(getResources().getColor(R.color.tv_blue));
                    continue;
                }
                if (i == 0) {
                    TextView text = new TextView(getBaseContext());
//                    text.setBackgroundResource(R.drawable.table_shape);
                    text.setPadding(15, 15, 15, 15);
                    text.setBackgroundColor(getResources().getColor(R.color.tv_blue));
                    text.setGravity(Gravity.CENTER);
                    text.setTextSize(18);
                    text.setText(betNums.get(j - 1) + "+");
                    tableRow.addView(text, j);
                    continue;
                }

                if (j == 0) {
                    TextView text = new TextView(getBaseContext());
//                    text.setBackgroundResource(R.drawable.table_shape);
                    text.setPadding(15, 15, 15, 15);
                    Map.Entry entry = (Map.Entry) Levels.get(i - 1);
                    text.setTextSize(18);
                    text.setGravity(Gravity.CENTER);
                    text.setBackgroundColor(getResources().getColor(R.color.white));
                    text.setText(entry.getValue().toString());
                    tableRow.addView(text, j);
                    continue;
                }
                TextView text = new TextView(getBaseContext());
//                    text.setBackgroundResource(R.drawable.table_shape);
                text.setPadding(15, 15, 15, 15);
                int position = j - 1 + (i - 1) * betNums.size(); //计算出利率所处的坐标
                text.setText(addBonus.get(position).toString());
                text.setBackgroundColor(getResources().getColor(R.color.white));
                text.setTextSize(18);
                text.setGravity(Gravity.CENTER);
                tableRow.addView(text, j);

            }
            tableLayout.addView(tableRow);
        }
    }


    final class MyEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }


}