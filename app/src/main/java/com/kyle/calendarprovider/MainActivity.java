package com.kyle.calendarprovider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kyle.calendarprovider.calendar.CalendarEvent;
import com.kyle.calendarprovider.calendar.CalendarProviderManager;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button btnMainAdd;
    private Button btnMainDelete;
    private Button btnMainUpdate;
    private Button btnMainQuery;
    private TextView tvEvent;
    private Button btnEdit;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化视图
        initViews();
        // 设置点击事件
        setupClickListeners();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR,
                            Manifest.permission.READ_CALENDAR}, 1);
        }
    }

    private void initViews() {
        btnMainAdd = findViewById(R.id.btn_main_add);
        btnMainDelete = findViewById(R.id.btn_main_delete);
        btnMainUpdate = findViewById(R.id.btn_main_update);
        btnMainQuery = findViewById(R.id.btn_main_query);
        tvEvent = findViewById(R.id.tv_event);
        btnEdit = findViewById(R.id.btn_edit);
        btnSearch = findViewById(R.id.btn_search);
    }
    
    private void setupClickListeners() {
        View.OnClickListener clickListener = this::onViewClicked;
        btnMainAdd.setOnClickListener(clickListener);
        btnMainDelete.setOnClickListener(clickListener);
        btnMainUpdate.setOnClickListener(clickListener);
        btnMainQuery.setOnClickListener(clickListener);
        btnEdit.setOnClickListener(clickListener);
        btnSearch.setOnClickListener(clickListener);
    }
    
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_add:
                Long start =  System.currentTimeMillis();
                Log.d("MainActivity","start ="+start);
                CalendarEvent calendarEvent = new CalendarEvent(
                        "马上吃饭yyyyy",
                        "吃好吃的",
                        "南信院二食堂",
                        1750464000L *1000,
                        1750464000L *1000+60000,
                        0, null
                );

                // 添加事件
                int result = CalendarProviderManager.addCalendarEvent(this, calendarEvent);
                if (result == 0) {
                    Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
                } else if (result == -1) {
                    Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
                } else if (result == -2) {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_main_delete:
                // 删除事件
                long calID2 = CalendarProviderManager.obtainCalendarAccountID(this);
                List<CalendarEvent> events2 = CalendarProviderManager.queryAccountEvent(this, calID2);
                if (null != events2) {
                    if (events2.size() == 0) {
                        Toast.makeText(this, "没有事件可以删除", Toast.LENGTH_SHORT).show();
                    } else {
                        long eventID = events2.get(0).getId();
                        int result2 = CalendarProviderManager.deleteCalendarEvent(this, eventID);
                        if (result2 == -2) {
                            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_main_update:
                // 更新事件
                long calID = CalendarProviderManager.obtainCalendarAccountID(this);
                List<CalendarEvent> events = CalendarProviderManager.queryAccountEvent(this, calID);
                if (null != events) {
                    if (events.size() == 0) {
                        Toast.makeText(this, "没有事件可以更新", Toast.LENGTH_SHORT).show();
                    } else {
                        long eventID = events.get(0).getId();
                        int result3 = CalendarProviderManager.updateCalendarEventTitle(
                                this, eventID, "改吃晚饭的房间第三方监督司法");
                        if (result3 == 1) {
                            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_main_query:
                // 查询事件
                long calID4 = CalendarProviderManager.obtainCalendarAccountID(this);
                List<CalendarEvent> events4 = CalendarProviderManager.queryAccountEvent(this, calID4);
                StringBuilder stringBuilder4 = new StringBuilder();
                if (null != events4) {
                    for (CalendarEvent event : events4) {
                        stringBuilder4.append(events4.toString()).append("\n");
                    }
                    tvEvent.setText(stringBuilder4.toString());
                    Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit:
                // 启动系统日历进行编辑事件
                CalendarProviderManager.startCalendarForIntentToInsert(this, System.currentTimeMillis(),
                        System.currentTimeMillis() + 60000, "哈", "哈哈哈哈", "蒂埃纳",
                        false);
                break;
            case R.id.btn_search:
                if (CalendarProviderManager.isEventAlreadyExist(this, 1552986006309L,
                        155298606609L, "马上吃饭")) {
                    Toast.makeText(this, "存在", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "不存在", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
