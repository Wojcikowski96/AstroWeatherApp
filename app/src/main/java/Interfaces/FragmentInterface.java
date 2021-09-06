package Interfaces;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import java.util.Map;

public interface FragmentInterface {
    Map<String, Object> getDataFromJson(String location, int isCelsius, Activity activity);
    void updateCurrentFragment(Map<String, Object> jsonData, Activity activity);

}
